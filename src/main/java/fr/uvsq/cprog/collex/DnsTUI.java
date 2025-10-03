package fr.uvsq.cprog.collex;

import java.io.PrintStream;
import java.util.Scanner;

public class DnsTUI {
    private final Scanner in;
    private final PrintStream out;
    private final Dns dns;

    public DnsTUI(Scanner in, PrintStream out, Dns dns) {
        this.in = in;
        this.out = out;
        this.dns = dns;
    }

    public Commande nextCommande() {
        if (!in.hasNextLine()) {
            return new CmdQuit(dns, this);
        }
        String line = in.nextLine().trim();
        if (line.isEmpty()) {
            return new CmdQuit(dns, this);
        }

        if (line.startsWith("ls ")) {
            String arg = line.substring(3).trim();
            boolean triParIp = false;
            if (arg.startsWith("-a ")) {
                triParIp = true;
                arg = arg.substring(3).trim();
            }
            return new CmdLs(dns, this, arg, triParIp);
        }

        if (line.startsWith("add ")) {
            String rest = line.substring(4).trim();
            String[] parts = rest.split("\\s+");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Usage: add adr.es.se.ip nom.qualifie.machine");
            }
            AdressIP ip = new AdressIP(parts[0]);
            NomMachine nom = new NomMachine(parts[1]);
            return new CmdAdd(dns, this, ip, nom);
        }

        // si ce n'est pas une commande spéciale, essayer de deviner si c'est une IP ou un nom
        if (line.matches("\\d+\\.\\d+\\.\\d+\\.\\d+")) {
            return new CmdIpVersNom(dns, this, new AdressIP(line));
        } else {
            return new CmdNomVersIp(dns, this, new NomMachine(line));
        }
    }

    public void affiche(String s) {
        out.println(s);
    }
}


