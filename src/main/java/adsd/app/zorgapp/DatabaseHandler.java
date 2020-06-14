package adsd.app.zorgapp;

import adsd.app.zorgapp.models.Medicijn;
import adsd.app.zorgapp.models.Meetmoment;
import adsd.app.zorgapp.models.Profile;
import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseHandler
{
    private static int huidigeGebruiker; // Dit variable wordt later gekoppeld aan de gebruikers id

    public boolean checkLogin(String gebruiker, String wachtwoord)
    {

        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:ZorgDB.SQLITE");

        String SQL = "SELECT id FROM gebruikers WHERE gebruiker = ? AND wachtwoord = ?";

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQL)
        )
        {
            preparedStatement.setString(1, gebruiker);
            preparedStatement.setString(2, wachtwoord);

            try (
                    ResultSet resultSet = preparedStatement.executeQuery()
            )
            {
                if (resultSet.next()) // De gebruiker op basis van gebruikersnaam en wachtwoord bestaat in de DB
                {
                    huidigeGebruiker = resultSet.getInt(1); // De id van de gebruiker bewaren als private static in

                    return true;
                }
            }
        }
        // Opvangen van exceptie
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        // In het geval er geen match is van de gebruikersnaam en het wachtwoord in de DB
        return false;
    }

    public Profile getProfile()
    {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:ZorgDB.SQLITE");

        String SQL = "SELECT profielen.id, voorNaam, achterNaam, leeftijd, lengte, gewicht FROM profielen " +
                     "INNER JOIN gebruikers ON profielen.gebruiker = gebruikers.id " +
                     "WHERE gebruikers.id = ?";

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQL)
        )
        {
            preparedStatement.setInt(1, huidigeGebruiker);

            try (
                    ResultSet resultSet = preparedStatement.executeQuery()
            )
            {
                if (resultSet.next())
                {
                    Profile profile = new Profile(
                            resultSet.getInt("id"),
                            resultSet.getString("voorNaam"),
                            resultSet.getString("achterNaam"),
                            resultSet.getInt("leeftijd"),
                            resultSet.getDouble("gewicht"),
                            resultSet.getDouble("lengte")
                    );

                    return profile;
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

    return null;
    }

    public ArrayList<Medicijn> getMedicijnen(String taal)
    {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:ZorgDB.SQLITE");

        String SQL = "SELECT medicijnen.id, naam, omschrijving, soort, dosering FROM medicijntekst " +
                     "INNER JOIN medicijnen ON medicijntekst.medicijn = medicijnen.id " +
                     "INNER JOIN profielen ON medicijnen.patient = profielen.id " +
                     "WHERE medicijntekst.taal = ? AND profielen.id = ?";

        Profile currentProfile = getProfile();

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQL)
        )
        {
            preparedStatement.setString(1, taal);
            preparedStatement.setInt(2, currentProfile.getPatientId());

            ArrayList<Medicijn> medicijnen = new ArrayList<>();

            try (
                    ResultSet resultSet = preparedStatement.executeQuery()
            )
            {
                while (resultSet.next())
                {
                    int id = resultSet.getInt("id");
                    String naam = resultSet.getString("naam");
                    String omschrijving = resultSet.getString("omschrijving");
                    String soort = resultSet.getString("soort");
                    String dosering = resultSet.getString("dosering");

                    Medicijn medicijn = new Medicijn(id, naam, omschrijving, soort, dosering);
                    medicijnen.add(medicijn);
                }

                return medicijnen;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public ArrayList<Meetmoment> getMeetmomenten()
    {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:ZorgDB.SQLITE");

        String SQL = "SELECT meetmomenten.id, datum, meetmomenten.gewicht " +
                     "FROM meetmomenten " +
                     "INNER JOIN profielen ON meetmomenten.profiel = profielen.id " +
                     "INNER JOIN gebruikers ON profielen.gebruiker = gebruikers.id " +
                     "WHERE gebruikers.id = ?";

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQL)
        )
        {
            preparedStatement.setInt(1, huidigeGebruiker);

            try (
                    ResultSet resultSet = preparedStatement.executeQuery()
            )
            {
                ArrayList<Meetmoment> meetmomenten = new ArrayList<Meetmoment>();

                while (resultSet.next())
                {
                    meetmomenten.add(new Meetmoment(
                            resultSet.getInt("id"),
                            resultSet.getString("datum"),
                            resultSet.getDouble("gewicht")));
                }

                return meetmomenten;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public int addMeetmoment(String datum, double gewicht) throws SQLException
    {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:ZorgDB.SQLITE");

        int meetmomentId = 0;

        String SQL = "INSERT INTO meetmomenten (datum, gewicht, profiel) VALUES (?, ?, ?)";

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)
        )
        {
            preparedStatement.setString(1, datum);
            preparedStatement.setDouble(2, gewicht);
            preparedStatement.setInt(3, getProfile().getPatientId());

            int betrokkenRijen = preparedStatement.executeUpdate();

            if (betrokkenRijen != 1)
            {
                throw new SQLException(
                        String.format("Toevoegen van meetmoment gefaald, %s rijen toegevoegd", betrokkenRijen)
                );
            }

            try (
                    ResultSet generatedKeys = preparedStatement.getGeneratedKeys()
            )
            {
                if (generatedKeys.next())
                {
                    meetmomentId = generatedKeys.getInt(1);
                }
            }
        }

        return meetmomentId;
    }

    public void updateMeetmoment(Meetmoment meetmoment) throws SQLException
    {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:ZorgDB.SQLITE");

        String SQL = "UPDATE meetmomenten SET datum = ?, gewicht = ?, profiel = ? WHERE id = ?";

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQL)
        )
        {
            preparedStatement.setString(1, meetmoment.getDate());
            preparedStatement.setDouble(2, meetmoment.getGewicht());
            preparedStatement.setInt(3, getProfile().getPatientId());
            preparedStatement.setInt(4, meetmoment.getId());

            int betrokkenRijen = preparedStatement.executeUpdate();

            if (betrokkenRijen != 1)
            {
                throw new SQLException(
                        String.format("Updaten van meetmoment gefaald, %s rijen geupdatet", betrokkenRijen)
                );
            }
        }
    }

    public void deleteMeetmoment(int id) throws SQLException
    {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:ZorgDB.SQLITE");

        String SQL = "DELETE FROM meetmomenten WHERE id = ?";

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQL)
        )
        {
            preparedStatement.setInt(1, id);

            int betrokkenRijen = preparedStatement.executeUpdate();

            if (betrokkenRijen != 1)
            {
                throw new SQLException(
                        String.format("Verwijderen van meetmoment gefaald, %s rijen verwijderd", betrokkenRijen)
                );
            }
        }
    }
}
