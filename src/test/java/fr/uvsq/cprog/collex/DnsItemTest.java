package fr.uvsq.cprog.collex;

import static org.junit.Assert.assertSame;

import org.junit.Test;

public class DnsItemTest {

    @Test(expected = NullPointerException.class)
    public void constructeurRefuseNomNull() {
        AdressIP ip = new AdressIP();
        new DnsItem(null, ip);
    }

    @Test(expected = NullPointerException.class)
    public void constructeurRefuseIpNull() {
        NomMachine nom = new NomMachine();
        new DnsItem(nom, null);
    }

    @Test
    public void gettersRendentLesObjetsPasses() {
        NomMachine nom = new NomMachine();
        AdressIP ip = new AdressIP();
        DnsItem item = new DnsItem(nom, ip);

        assertSame(nom, item.getNom());
        assertSame(ip, item.getIp());
    }
}
