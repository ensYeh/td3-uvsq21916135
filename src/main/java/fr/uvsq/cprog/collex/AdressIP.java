package fr.uvsq.cprog.collex;

import java.util.Objects;
public class AdressIP {

    private final String ip;

    public AdressIP() {
        this.ip = null;
    }

    public AdressIP(String address) {
        String trimmed = Objects.requireNonNull(address, "L'adresse IP ne doit pas être nulle").trim();
        if (!isValidIPv4(trimmed)) {
            throw new IllegalArgumentException("Adresse IPv4 invalide: " + address);
        }
        this.ip = trimmed;
    }

    @Override
    public String toString() {
        return this.ip == null ? "" : this.ip;
    }

    private static boolean isValidIPv4(String s) {
        String[] parts = s.split("\\.", -1);
        if (parts.length != 4) {
            return false;
        }
        for (String p : parts) {
            if (p.isEmpty()) {
                return false;
            }
            // pas d'espaces, pas de signes
            for (int i = 0; i < p.length(); i++) {
                char c = p.charAt(i);
                if (c < '0' || c > '9') {
                    return false;
                }
            }
            try {
                int v = Integer.parseInt(p);
                if (v < 0 || v > 255) {
                    return false;
                }
                // éviter les zéros non significatifs ("01") sauf pour 0 lui-même
                if (p.length() > 1 && p.charAt(0) == '0') {
                    return false;
                }
            } catch (NumberFormatException ex) {
                return false;
            }
        }
        return true;
    }
}
