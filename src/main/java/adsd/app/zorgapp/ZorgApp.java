package adsd.app.zorgapp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class ZorgApp extends Application
{
    private static Scene scene;
    private static Locale locale = new Locale("nl");

    @Override
    public void start(Stage stage) throws IOException
    {
        scene = new Scene(loadFXML("login"));
        scene.getStylesheets().add(ZorgApp.class.getResource("style.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Zorg Applicatie");
        stage.show();

        stage.setOnCloseRequest(e ->
                                {
                                    Platform.exit();
                                    System.exit(0);
                                });
    }

    public static void setRoot(String fxml) throws IOException
    {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException
    {
        ResourceBundle bundle = ResourceBundle.getBundle("adsd.app.zorgapp.strings", locale);
        FXMLLoader fxmlLoader = new FXMLLoader(ZorgApp.class.getResource(fxml + ".fxml"), bundle);

        return fxmlLoader.load();
    }

    public static void setLocale(String language) throws IOException
    {
        ZorgApp.locale = new Locale(language);
        setRoot("login");
    }


    public ZorgApp()
    {
        MedicatieAlarm.checkAlarm(10, 31);
    }
}

