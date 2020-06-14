package adsd.app.zorgapp.models;

public class Profile
{
    // Encapsulate variables
    private int patientId;
    private String voorNaam;
    private String achterNaam;
    private int leeftijd;
    private double gewicht;
    private double lengte;

    // Constructor with overloading
    // patientid, voornaam, achternaam, leeftijd, gewicht, lengte
    public Profile()
    {
        this(0,null, null, 0, 0, 0.0);
    }

    //
    public Profile(int patientId,
                   String voorNaam,
                   String achterNaam,
                   int leeftijd,
                   double gewicht,
                   double lengte)
    {
        this.patientId = patientId;
        this.voorNaam = voorNaam;
        this.achterNaam = achterNaam;
        this.leeftijd = leeftijd;
        this.gewicht = gewicht;
        this.lengte = lengte;
    }

    public int getPatientId()
    {
        return patientId;
    }

    // Set variable voorNaam
    public void setVoorNaam(String voorNaam)
    {
        this.voorNaam = voorNaam;
    }
    //
    // Get variable voorNaam
    public String getVoorNaam()
    {
        return voorNaam;
    }


    // Set variable achterNaam
    public void setAchterNaam(String achterNaam)
    {
        this.achterNaam = achterNaam;
    }
    //
    // Get variable achterNaam
    public String getAchterNaam()
    {
        return achterNaam;
    }


    // Set variable leeftijd
    public void setLeeftijd(int leeftijd)
    {
        this.leeftijd = leeftijd;
    }
    //
    // Get variable leeftijd
    public int getLeeftijd()
    {
        return this.leeftijd;
    }


    // Set variable gewicht
    public void setGewicht(double gewicht)
    {
        this.gewicht = gewicht;
    }
    //
    // Get variable gewicht
    public double getGewicht()
    {
        return gewicht;
    }


    // Set variable lengte
    public void setLengte(double lengte)
    {
        this.lengte = lengte;
    }
    //
    // Get variable lengte
    public double getLengte()
    {
        return this.lengte;
    }

    // Get calculated bmi
    public String getBmi()
    {
        // return calculated BMI
        double bmi = gewicht / (lengte * lengte);

        return String.format("%.2f", bmi);
    }


}

