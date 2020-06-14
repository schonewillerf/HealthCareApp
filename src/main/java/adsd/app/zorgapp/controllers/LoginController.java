package adsd.app.zorgapp.controllers;

import adsd.app.zorgapp.DatabaseHandler;
import adsd.app.zorgapp.ZorgApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable
{
    private ResourceBundle bundle;

    @FXML
    private TextField gebruikerText, wachtwoordText;

    @FXML
    private Label foutmeldingLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        bundle = resourceBundle;
    }

    @FXML
    public void loginButtonClick(ActionEvent e) throws IOException
    {
        DatabaseHandler db = new DatabaseHandler();

        if (db.checkLogin(gebruikerText.getText(), wachtwoordText.getText())) // Geldige login
        {
            ZorgApp.setRoot("base");
        }
        else // Ongeldige login
        {
            foutmeldingLabel.setText(bundle.getString("l.foutmelding"));
        }
    }

    @FXML
    public void selectNL(ActionEvent e) throws IOException
    {
        ZorgApp.setLocale("nl");
    }

    @FXML
    public void selectEN(ActionEvent e) throws IOException
    {
        ZorgApp.setLocale("en");
    }

    @FXML
    public void selectDE(ActionEvent e) throws IOException
    {
        ZorgApp.setLocale("de");
    }
}
