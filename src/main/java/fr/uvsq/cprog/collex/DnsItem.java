package fr.uvsq.cprog.collex;

public class DnsItem {
    private NomMachine nom;
    private AdressIP ip;
    
    public DnsItem(NomMachine nom, AdressIP ip) {
        this.nom = nom;
        this.ip = ip;
    }

    public NomMachine getNom(){
        return this.nom;
    }

    public AdressIP getIp(){
        return this.ip;
    }

    public String toString(){
        return this.ip + " " + this.nom;
    }
}
