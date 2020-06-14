package adsd.app.zorgapp.models;

public class Medicijn
{
    private int id;
    private String medicijnNaam;
    private String omschrijving;
    private String soort;
    private String dosering;

    public Medicijn()
    {
        this(0, null, null, null, null);
    }

    public Medicijn(int id,
                    String medicijnNaam,
                    String omschrijving,
                    String soort,
                    String dosering)
    {
        this.id = id;
        this.medicijnNaam = medicijnNaam;
        this.omschrijving = omschrijving;
        this.soort = soort;
        this.dosering = dosering;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    // Set variable medicijnNaam
    public void setMedicijnNaam(String medicijnNaam)
    {
        this.medicijnNaam = medicijnNaam;
    }
    //
    // Get variable medicijnNaam
    public String getMedicijnNaam()
    {
        return medicijnNaam;
    }

    // Set variable omschrijving
    public void setOmschrijving(String omschrijving)
    {
        this.omschrijving = omschrijving;
    }
    //
    // Get variable omschrijving
    public String getOmschrijving()
    {
        return omschrijving;
    }

    // Set variable soort
    public void setSoort(String soort)
    {
        this.soort = soort;
    }
    //
    // Get variable soort
    public String getSoort()
    {
        return soort;
    }

    // Set variable dosering
    public void setDosering(String dosering)
    {
        this.dosering = dosering;
    }
    //
    // Get variable dosering
    public String getDosering()
    {
        return dosering;
    }
}
