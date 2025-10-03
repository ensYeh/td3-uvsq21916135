package fr.uvsq.cprog.collex;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * DNS in-memory database with file persistence.
 */
public class Dns {

    private final Map<String, DnsItem> nameToItem;
    private final Map<String, DnsItem> ipToItem;
    private final Path dataFilePath;

    /**
     * Constructeur par défaut. Lit le fichier de propriétés {@code dns.properties}
     * pour obtenir le chemin du fichier de données. Si le fichier n'existe pas
     * sur le système de fichiers, tente de charger depuis le classpath.
     */
    public Dns() {
        this(loadDataFilePathFromProperties());
    }

    /**
     * Constructeur pour tests permettant d'imposer un fichier de données.
     *
     * @param dataFilePath chemin du fichier de données
     */
    public Dns(Path dataFilePath) {
        this.nameToItem = new LinkedHashMap<>();
        this.ipToItem = new LinkedHashMap<>();
        this.dataFilePath = Objects.requireNonNull(dataFilePath);
        load();
    }

    private static Path loadDataFilePathFromProperties() {
        Properties props = new Properties();
        try (InputStream in = Dns.class.getClassLoader().getResourceAsStream("dns.properties")) {
            if (in != null) {
                props.load(in);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Impossible de lire dns.properties", e);
        }
        String value = props.getProperty("dns.dataFile", "data.txt");
        return Paths.get(value);
    }

    private void load() {
        List<String> lines = new ArrayList<>();
        if (Files.exists(this.dataFilePath)) {
            try {
                lines = Files.readAllLines(this.dataFilePath, StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new IllegalStateException("Impossible de lire le fichier de données: " + this.dataFilePath, e);
            }
        } else {
            // Tentative de lecture depuis le classpath si présent
            try (InputStream in = Dns.class.getClassLoader().getResourceAsStream(this.dataFilePath.toString())) {
                if (in != null) {
                    lines = new ArrayList<>(List.of(new String(in.readAllBytes(), StandardCharsets.UTF_8).split("\n")));
                }
            } catch (IOException e) {
                throw new IllegalStateException("Impossible de charger les données", e);
            }
        }

        for (String raw : lines) {
            String line = raw.trim();
            if (line.isEmpty()) {
                continue;
            }
            String[] parts = line.split("\\s+");
            if (parts.length != 2) {
                continue; // ignorer les lignes invalides
            }
            String nomStr = parts[0];
            String ipStr = parts[1];
            NomMachine nom = new NomMachine(nomStr);
            AdressIP ip = new AdressIP(ipStr);
            DnsItem item = new DnsItem(nom, ip);
            this.nameToItem.put(nom.toString(), item);
            this.ipToItem.put(ip.toString(), item);
        }
    }

    /**
     * Retourne un item depuis une adresse IP.
     */
    public DnsItem getItem(AdressIP ip) {
        DnsItem item = this.ipToItem.get(Objects.requireNonNull(ip).toString());
        if (item == null) {
            throw new IllegalArgumentException("Adresse IP inconnue: " + ip);
        }
        return item;
    }

    /**
     * Retourne un item depuis un nom de machine.
     */
    public DnsItem getItem(NomMachine nom) {
        DnsItem item = this.nameToItem.get(Objects.requireNonNull(nom).toString());
        if (item == null) {
            throw new IllegalArgumentException("Nom de machine inconnu: " + nom);
        }
        return item;
    }

    /**
     * Retourne la liste des items d'un domaine donné.
     */
    public List<DnsItem> getItems(String domaine) {
        Objects.requireNonNull(domaine);
        String dom = domaine.trim();
        if (dom.isEmpty()) {
            return Collections.emptyList();
        }
        List<DnsItem> res = new ArrayList<>();
        for (DnsItem item : this.nameToItem.values()) {
            if (dom.equals(item.getNom().getDomaine())) {
                res.add(item);
            }
        }
        return res;
    }

    /**
     * Ajoute un item, lève une exception si l'IP ou le nom existent déjà.
     */
    public void addItem(AdressIP ip, NomMachine nom) {
        Objects.requireNonNull(ip);
        Objects.requireNonNull(nom);
        if (this.ipToItem.containsKey(ip.toString())) {
            throw new IllegalArgumentException("L'adresse IP existe déjà !");
        }
        if (this.nameToItem.containsKey(nom.toString())) {
            throw new IllegalArgumentException("Le nom de machine existe déjà !");
        }
        DnsItem item = new DnsItem(nom, ip);
        this.nameToItem.put(nom.toString(), item);
        this.ipToItem.put(ip.toString(), item);
        persist();
    }

    private void persist() {
        List<String> lines = new ArrayList<>();
        for (DnsItem item : this.nameToItem.values()) {
            lines.add(item.getNom().toString() + " " + item.getIp().toString());
        }
        try {
            Files.write(this.dataFilePath, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Impossible d'écrire le fichier de données: " + this.dataFilePath, e);
        }
    }
}


