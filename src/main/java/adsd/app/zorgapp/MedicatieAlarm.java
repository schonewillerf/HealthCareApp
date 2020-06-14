package adsd.app.zorgapp;

import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class MedicatieAlarm
{
    public static void checkAlarm(final int uren, final int minuten)
    {

        Thread t = new Thread(() ->
        {
            while (true)
            {
                Calendar c = new GregorianCalendar();
                int urenNu = c.get(Calendar.HOUR_OF_DAY);
                int minutenNu = c.get(Calendar.MINUTE);

                if (uren == urenNu && minuten == minutenNu)
                {
                    Platform.runLater(MedicatieAlarm::showPopup);
                    break;
                }
                else
                {
                    //Pauzeer de thread om rekenkracht te besparen
                    try
                    {
                        Thread.sleep(2500);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
        t.setPriority(Thread.MIN_PRIORITY);
        t.start();
    }

    public static void showPopup()
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notificatie");
        alert.setHeaderText("Het is tijd om medicijnen in te nemen");
        alert.showAndWait();
    }
}
