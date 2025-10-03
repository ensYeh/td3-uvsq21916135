package fr.uvsq.cprog.collex;

import java.util.Objects;

public class DnsItem {
    private final NomMachine nom;
    private final AdressIP ip;

    public DnsItem(NomMachine nom, AdressIP ip) {
        this.nom = Objects.requireNonNull(nom, "Le nom ne doit pas être vide");
        this.ip = Objects.requireNonNull(ip, "L'ip ne doit pas être vide");
    }

    public NomMachine getNom() {
        return this.nom;
    }

    public AdressIP getIp() {
        return this.ip;
    }

    @Override
    public String toString() {
        return this.ip + " " + this.nom;
    }
}
