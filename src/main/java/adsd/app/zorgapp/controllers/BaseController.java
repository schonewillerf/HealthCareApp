package adsd.app.zorgapp.controllers;

import adsd.app.zorgapp.DatabaseHandler;
import adsd.app.zorgapp.ZorgApp;
import adsd.app.zorgapp.models.Medicijn;
import adsd.app.zorgapp.models.Meetmoment;
import adsd.app.zorgapp.models.Profile;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BaseController<gewichtGrafiek, tijdAs, gewichtAs> implements Initializable
{
    private ResourceBundle bundle;
    private Profile profile;
    private boolean profielOpslaan;

    @FXML
    private Label titleLabel;

    @FXML
    private TextField voorNaamField, achterNaamField, leeftijdField, lengteField, gewichtField, BMIField;

    @FXML
    private Button bewerkenButton;

    @FXML
    private TableView<Medicijn> medicijnTabel;

    @FXML
    private TableColumn<Medicijn, String> medicijnNaam, medicijnOmschrijving, medicijnSoort, medicijnDosering;

    @FXML
    private TableView<Meetmoment> gewichtTabel;

    @FXML
    private TableColumn<Meetmoment, Integer> gewichtId;

    @FXML
    private TableColumn<Meetmoment, String> gewichtDatum;

    @FXML
    private TableColumn<Meetmoment, Double> gewichtGewicht;

    @FXML
    private DatePicker meetpuntDatePicker;

    @FXML
    private TextField meetpuntGewichtField;

    @FXML
    private Button meetpuntAddButton, meetpuntEditButton, meetpuntDeleteButton, meetpuntCancelButton;

    @FXML
    private CategoryAxis tijdAs;

    @FXML
    private NumberAxis gewichtAs;

    @FXML
    private AreaChart<tijdAs, gewichtAs> gewichtGrafiek;

    private XYChart.Series<tijdAs, gewichtAs> series;

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle)
    {
        bundle = resourceBundle;
        DatabaseHandler db = new DatabaseHandler();

        // Profiel Tabblad
        this.profile = db.getProfile();
        voorNaamField.setText(profile.getVoorNaam());
        achterNaamField.setText(profile.getAchterNaam());
        leeftijdField.setText(String.valueOf(profile.getLeeftijd()));
        gewichtField.setText(String.valueOf(profile.getGewicht()));
        lengteField.setText(String.valueOf(profile.getLengte()));
        BMIField.setText(profile.getBmi());

        // Voornaam & Achternaam Boven Tabbladen
        titleLabel.textProperty().bind(Bindings.concat(voorNaamField.textProperty(), " ",
                achterNaamField.textProperty()));

        // Medicijnen Tabblad
        ArrayList<Medicijn> medicijnen = db.getMedicijnen(bundle.getString("taal"));
        ObservableList<Medicijn> medicijndata = FXCollections.observableArrayList(medicijnen);

        medicijnNaam.setCellValueFactory(new PropertyValueFactory<>("medicijnNaam"));
        medicijnOmschrijving.setCellValueFactory(new PropertyValueFactory<>("omschrijving"));
        medicijnSoort.setCellValueFactory(new PropertyValueFactory<>("soort"));
        medicijnDosering.setCellValueFactory(new PropertyValueFactory<>("dosering"));

        medicijnTabel.setItems(medicijndata);

        // Gewicht Tabblad
        ArrayList<Meetmoment> meetmomenten = db.getMeetmomenten();

        // gewicht tabel
        ObservableList<Meetmoment> meetdata = FXCollections.observableArrayList(meetmomenten);

        gewichtId.setCellValueFactory(new PropertyValueFactory<>("id"));
        gewichtDatum.setCellValueFactory(new PropertyValueFactory<>("date"));
        gewichtGewicht.setCellValueFactory(new PropertyValueFactory<>("gewicht"));

        gewichtTabel.setItems(meetdata);
        gewichtTabel.getSelectionModel().selectedIndexProperty().addListener((o, oldVal, newVal) -> {
            mpSelectionChangeAction(newVal.intValue());
        });

        // gewicht grafiek
        series = new XYChart.Series<>();
        gewichtGrafiek.setLegendVisible(false);

        for (Meetmoment meetmoment : meetmomenten)
        {
            series.getData().add(new XYChart.Data(meetmoment.getDate(), meetmoment.getGewicht()));
        }

        gewichtGrafiek.getData().add(series);

    }

    @FXML
    private void profEditButtonClick(ActionEvent e)
    {
        // Check of het profiel moet worden opgeslagen of worden bewerkt
        if (!profielOpslaan)
        {
            // Maak velden voor input bewerkbaar
            voorNaamField.setEditable(true);
            achterNaamField.setEditable(true);
            leeftijdField.setEditable(true);
            lengteField.setEditable(true);
            gewichtField.setEditable(true);

            // Verander de tekst van bewerken naar opslaan
            bewerkenButton.setText(bundle.getString("profile.bewerkenButtonOpslaan"));
        }
        else
        {
            // Maak velden niet bewerkbaar
            voorNaamField.setEditable(false);
            achterNaamField.setEditable(false);
            leeftijdField.setEditable(false);
            lengteField.setEditable(false);
            gewichtField.setEditable(false);

            // Update het profiel
            profile.setVoorNaam(voorNaamField.getText());
            profile.setAchterNaam(achterNaamField.getText());

            try
            {
                // probeer de waarde van het leeftijd veld om te zetten naar getal
                profile.setLeeftijd(Integer.parseInt(leeftijdField.getText()));
            }
            catch (Exception exception)
            {
                // Geef melding van verkeerde waarde
                leeftijdField.setText("Ongeldige waarde voor leeftijd");
            }

            try
            {
                profile.setLengte(Double.parseDouble(lengteField.getText()));
            }
            catch (Exception exception)
            {
                lengteField.setText("Ongeldige waarde voor lengte");
            }

            try
            {
                profile.setGewicht(Double.parseDouble(gewichtField.getText()));
            }
            catch (Exception exception)
            {
                gewichtField.setText("Ongeldige waarde voor gewicht");
            }

            // herbereken de waarde van BMI, en geef weer in profiel
            BMIField.setText(profile.getBmi());

            // Verander de tekst van opslaan naar bewerken
            bewerkenButton.setText(bundle.getString("profile.bewerkenButtonBewerken"));
        }
        profielOpslaan = !profielOpslaan; // Verwissel de waarde van profielOslaan
    }


    private void mpSelectionChangeAction(int selected)
    {
        if (selected == -1) // Als er geen rij geselecteerd is in de tableview
        {
            // Leeg de inhoud van datum en gewicht
            meetpuntDatePicker.getEditor().clear();
            meetpuntGewichtField.setText("");

            // Maak toevoegen knop bruikbaar
            meetpuntAddButton.setDisable(false);

            // Maak knoppen voor Bewerken/Cancel/Verwijderen onbruikbaar
            meetpuntEditButton.setDisable(true);
            meetpuntCancelButton.setDisable(true);
            meetpuntDeleteButton.setDisable(true);
        }
        else // Als er wel een rij geselecteerd is in de tableview
        {
            // geselecteerde rij wordt meetpunt
            Meetmoment meetmoment = gewichtTabel.getSelectionModel().getSelectedItem();

            // Tussenstap om datum van meetmoment in te voeren in datumpicker
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate localDate = LocalDate.parse(meetmoment.getDate(), formatter);

            // Gewicht en datum van geselecteerde meetmoment naar velden voor gewicht en datum overbrengen
            meetpuntGewichtField.setText(String.valueOf(meetmoment.getGewicht()));
            meetpuntDatePicker.setValue(localDate);

            // Maak toevoegen knop onbruikbaar
            meetpuntAddButton.setDisable(true);

            // Maak knoppen voor bewerken/cancel/verwijderen bruikbaar
            meetpuntEditButton.setDisable(false);
            meetpuntCancelButton.setDisable(false);
            meetpuntDeleteButton.setDisable(false);
        }
    }

    @FXML
    private void mpAddButtonClick(ActionEvent e)
    {
        String datum = meetpuntDatePicker.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        try
        {
            double gewicht = Double.parseDouble(meetpuntGewichtField.getText());

            // Gewicht in database bijwerken
            DatabaseHandler db = new DatabaseHandler();
            int meetmomentId = db.addMeetmoment(datum, gewicht);

            // Maak Meetmoment object
            Meetmoment meetmoment = new Meetmoment(
                    meetmomentId,
                    datum,
                    gewicht
            );

            // Gewicht in tableview bijwerken
            gewichtTabel.getItems().add(meetmoment);

            // Gewichten in grafiek bijwerken
            series.getData().add(new XYChart.Data("", 0)); // Een extra gewicht om met bijwerken goed uit te komen

            for (Meetmoment m : db.getMeetmomenten()) // Voor elk nieuwe gewicht een oud gewicht wegschrijven
            {
                series.getData().remove(1);
                series.getData().add(new XYChart.Data(m.getDate(), m.getGewicht()));
            }
        }
        catch (NumberFormatException ex)
        {
            meetpuntGewichtField.setText(bundle.getString("ongeldig")); // Foutmelding van ongeldige waarde
        }
        catch (SQLException sqe)
        {
            sqe.printStackTrace();
        }
    }

    @FXML
    private void mpEditButtonClick(ActionEvent e)
    {
        int index = gewichtTabel.getSelectionModel().getSelectedIndex();

        Meetmoment meetmoment = gewichtTabel.getSelectionModel().getSelectedItem();

        String datum = meetpuntDatePicker.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        meetmoment.setDate(datum);

        try
        {
            double gewicht = Double.parseDouble(meetpuntGewichtField.getText());
            meetmoment.setGewicht(gewicht);
        }
        catch (NumberFormatException ex)
        {
            meetpuntGewichtField.setText(bundle.getString("ongeldig")); // Foutmelding van ongeldige waarde
        }

        // Gewicht in database bijwerken
        DatabaseHandler db = new DatabaseHandler();

        try
        {
            db.updateMeetmoment(meetmoment);
        } catch (SQLException ex)
        {
            ex.printStackTrace();
        }

        // Gewicht in tableview bijwerken
        gewichtTabel.getItems().set(index, meetmoment);

        // Gewicht in grafiek bijwerken
        for (Meetmoment m : db.getMeetmomenten())
        {
            series.getData().remove(1);
            series.getData().add(new XYChart.Data(m.getDate(), m.getGewicht()));
        }
    }

    @FXML
    private void mpDeleteButtonClick(ActionEvent e)
    {
        int index = gewichtTabel.getSelectionModel().getSelectedIndex();
        Meetmoment meetmoment = gewichtTabel.getSelectionModel().getSelectedItem();

        // Gewicht uit database verwijderen
        DatabaseHandler db = new DatabaseHandler();

        try
        {
            db.deleteMeetmoment(meetmoment.getId());
        } catch (SQLException ex)
        {
            ex.printStackTrace();
        }

        // Gewicht uit tableview verwijderen
        gewichtTabel.getItems().remove(index);

        // Gewicht uit grafiek verwijderen
        series.getData().remove(1); // Vooraf een punt uit de grafiek verwijderen

        for (Meetmoment m : db.getMeetmomenten()) // Voor elke punt uit database een uit grafiek verwijderen
        {
            series.getData().remove(1);
            series.getData().add(new XYChart.Data(m.getDate(), m.getGewicht()));
        }
    }

    @FXML
    private void mpCancelButtonClick(ActionEvent e)
    {
        gewichtTabel.getSelectionModel().clearSelection();
    }

    @FXML
    public void logoutButtonClick(ActionEvent e) throws IOException
    {
        ZorgApp.setRoot("login");
    }
}
