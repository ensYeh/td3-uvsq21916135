package fr.uvsq.cprog.collex;

import java.util.Comparator;
import java.util.List;

abstract class CommandeBase implements Commande {
    protected final Dns dns;
    protected final DnsTUI tui;
    CommandeBase(Dns dns, DnsTUI tui) {
        this.dns = dns;
        this.tui = tui;
    }
}

class CmdNomVersIp extends CommandeBase {
    private final NomMachine nom;
    CmdNomVersIp(Dns dns, DnsTUI tui, NomMachine nom) { super(dns, tui); this.nom = nom; }
    public void execute() {
        DnsItem item = dns.getItem(nom);
        tui.affiche(item.getIp().toString());
    }
}

class CmdIpVersNom extends CommandeBase {
    private final AdressIP ip;
    CmdIpVersNom(Dns dns, DnsTUI tui, AdressIP ip) { super(dns, tui); this.ip = ip; }
    public void execute() {
        DnsItem item = dns.getItem(ip);
        tui.affiche(item.getNom().toString());
    }
}

class CmdLs extends CommandeBase {
    private final String domaine;
    private final boolean triParIp;
    CmdLs(Dns dns, DnsTUI tui, String domaine, boolean triParIp) { super(dns, tui); this.domaine = domaine; this.triParIp = triParIp; }
    public void execute() {
        List<DnsItem> items = dns.getItems(domaine);
        if (triParIp) {
            items.sort(Comparator.comparing(a -> a.getIp().toString()));
        } else {
            items.sort(Comparator.comparing(a -> a.getNom().getMachine()));
        }
        for (DnsItem it : items) {
            tui.affiche(it.getIp().toString() + " " + it.getNom().toString());
        }
    }
}

class CmdAdd extends CommandeBase {
    private final AdressIP ip;
    private final NomMachine nom;
    CmdAdd(Dns dns, DnsTUI tui, AdressIP ip, NomMachine nom) { super(dns, tui); this.ip = ip; this.nom = nom; }
    public void execute() {
        dns.addItem(ip, nom);
    }
}

class CmdQuit extends CommandeBase {
    CmdQuit(Dns dns, DnsTUI tui) { super(dns, tui); }
    public void execute() {
        // rien, la boucle principale sortira sur détection de cette commande
    }
}


