package fr.uvsq.cprog.collex;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NomMachineTest {

    @Test(expected = NullPointerException.class)
    public void constructeurStringRefuseNull() {
        new NomMachine(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalideSansPoint() {
        new NomMachine("localhost");
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidePointAuxExtremites() {
        new NomMachine(".uvsq.fr");
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalideLabelsVides() {
        new NomMachine("www..uvsq.fr");
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalideAvecCaractereInterdit() {
        new NomMachine("www.uvsq_fr");
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalideTiretDebutOuFinLabel() {
        new NomMachine("-www.uvsq.fr");
    }

    @Test
    public void valideGettersEtToString() {
        NomMachine nom = new NomMachine("www.uvsq.fr");
        assertEquals("www", nom.getMachine());
        assertEquals("uvsq.fr", nom.getDomaine());
        assertEquals("www.uvsq.fr", nom.toString());
    }

    @Test
    public void gettersVidesPourConstructeurDefaut() {
        NomMachine nom = new NomMachine();
        assertEquals("", nom.getMachine());
        assertEquals("", nom.getDomaine());
        assertEquals("", nom.toString());
    }
}
