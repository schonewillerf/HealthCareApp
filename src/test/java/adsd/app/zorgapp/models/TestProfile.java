package adsd.app.zorgapp.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestProfile
{
    @Test
    public void firstNameTest()
    {
        Profile profile = new Profile();
        profile.setVoorNaam("Raymond");

        assertEquals("Raymond", profile.getVoorNaam());
    }

    @Test
    public void bMITest()
    {
        Profile profile = new Profile();
        profile.setLengte(1.80);

        assertEquals(1.80, profile.getLengte());
    }
}
