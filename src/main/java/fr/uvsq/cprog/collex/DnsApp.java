package fr.uvsq.cprog.collex;

import java.io.PrintStream;
import java.util.Scanner;

public class DnsApp {

    private final Dns dns;
    private final DnsTUI tui;

    public DnsApp() {
        this.dns = new Dns();
        this.tui = new DnsTUI(new Scanner(System.in), System.out, dns);
    }

    public DnsApp(Scanner in, PrintStream out) {
        this.dns = new Dns();
        this.tui = new DnsTUI(in, out, dns);
    }

    public void run() {
        while (true) {
            Commande c = tui.nextCommande();
            if (c instanceof CmdQuit) {
                break;
            }
            try {
                c.execute();
            } catch (RuntimeException e) {
                tui.affiche("ERREUR : " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new DnsApp().run();
    }
}


