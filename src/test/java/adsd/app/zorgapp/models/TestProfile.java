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


}
