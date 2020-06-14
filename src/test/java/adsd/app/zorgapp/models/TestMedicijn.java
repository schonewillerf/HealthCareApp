package adsd.app.zorgapp.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestMedicijn
{
    @Test
    public void medicijnNaamTest()
    {
        Medicijn medicijn = new Medicijn();
        medicijn.setMedicijnNaam("Vitamine C");

        assertEquals("Vitamine C", medicijn.getMedicijnNaam());
    }

    @Test
    public void medicijnOmschrijvingTest()
    {
        Medicijn medicijn = new Medicijn();
        medicijn.setOmschrijving("Supplement");

        assertEquals("Supplement", medicijn.getOmschrijving());
    }
}
