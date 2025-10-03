package fr.uvsq.cprog.collex;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AdressIPTest {

    @Test(expected = NullPointerException.class)
    public void constructeurStringRefuseNull() {
        new AdressIP(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void formatInvalideMoinsDe4Octets() {
        new AdressIP("192.168.1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void formatInvalidePlusDe4Octets() {
        new AdressIP("192.168.1.1.5");
    }

    @Test(expected = IllegalArgumentException.class)
    public void formatInvalideOctetNonNumerique() {
        new AdressIP("192.abc.1.1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void formatInvalideOctetHorsPlage() {
        new AdressIP("256.168.1.1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void formatInvalideZeroNonSignificatif() {
        new AdressIP("192.168.01.1");
    }

    @Test
    public void formatValideEtToString() {
        AdressIP ip = new AdressIP("193.51.31.90");
        assertEquals("193.51.31.90", ip.toString());
    }

    @Test
    public void toStringVidePourConstructeurDefaut() {
        AdressIP ip = new AdressIP();
        assertEquals("", ip.toString());
    }
}
