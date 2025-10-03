package fr.uvsq.cprog.collex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class DnsTest {

    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    @Test
    public void chargeDepuisFichierEtRequieteParNomEtIp() throws IOException {
        Path data = temp.newFile("data.txt").toPath();
        Files.write(data, List.of(
                "ecampus.uvsq.fr 193.51.25.12",
                "poste.uvsq.fr 193.51.31.154",
                "www.uvsq.fr 193.51.31.90"
        ), StandardCharsets.UTF_8);

        Dns dns = new Dns(data);
        assertEquals("193.51.31.90", dns.getItem(new NomMachine("www.uvsq.fr")).getIp().toString());
        assertEquals("www.uvsq.fr", dns.getItem(new AdressIP("193.51.31.90")).getNom().toString());
    }

    @Test
    public void getItemsParDomaineEtTriLocal() throws IOException {
        Path data = temp.newFile("data2.txt").toPath();
        Files.write(data, List.of(
                "a.example.org 10.0.0.1",
                "b.example.org 10.0.0.2",
                "c.example.com 10.0.1.1"
        ), StandardCharsets.UTF_8);

        Dns dns = new Dns(data);
        List<DnsItem> items = dns.getItems("example.org");
        assertEquals(2, items.size());
        assertTrue(items.stream().anyMatch(i -> i.getNom().toString().equals("a.example.org")));
        assertTrue(items.stream().anyMatch(i -> i.getNom().toString().equals("b.example.org")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addRefuseNomExistant() throws IOException {
        Path data = temp.newFile("data3.txt").toPath();
        Files.write(data, List.of(
                "www.uvsq.fr 193.51.31.90"
        ), StandardCharsets.UTF_8);

        Dns dns = new Dns(data);
        dns.addItem(new AdressIP("193.51.25.24"), new NomMachine("www.uvsq.fr"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addRefuseIpExistante() throws IOException {
        Path data = temp.newFile("data4.txt").toPath();
        Files.write(data, List.of(
                "www.uvsq.fr 193.51.31.90"
        ), StandardCharsets.UTF_8);

        Dns dns = new Dns(data);
        dns.addItem(new AdressIP("193.51.31.90"), new NomMachine("pikachu.uvsq.fr"));
    }

    @Test
    public void addPersistedansFichier() throws IOException {
        Path data = temp.newFile("data5.txt").toPath();
        Files.write(data, List.of(
                "www.uvsq.fr 193.51.31.90"
        ), StandardCharsets.UTF_8);

        Dns dns = new Dns(data);
        dns.addItem(new AdressIP("193.51.25.24"), new NomMachine("pikachu.uvsq.fr"));

        List<String> lines = Files.readAllLines(data, StandardCharsets.UTF_8);
        assertTrue(lines.stream().anyMatch(l -> l.equals("pikachu.uvsq.fr 193.51.25.24") || l.equals("www.uvsq.fr 193.51.31.90")));
    }
}


