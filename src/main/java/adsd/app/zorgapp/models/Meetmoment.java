package adsd.app.zorgapp.models;

public class Meetmoment
{
    private int id;
    private String date;
    private double gewicht;

    public Meetmoment(int id, String date, double gewicht)
    {
        this.id = id;
        this.date = date;
        this.gewicht = gewicht;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public double getGewicht()
    {
        return gewicht;
    }

    public void setGewicht(double gewicht)
    {
        this.gewicht = gewicht;
    }
}
