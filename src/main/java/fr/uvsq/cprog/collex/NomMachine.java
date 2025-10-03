package fr.uvsq.cprog.collex;

import java.util.Objects;

public class NomMachine {

    private final String nomMachine;

    public NomMachine() {
        this.nomMachine = null;
    }


    public NomMachine(String nomMachine) {
        String trimmed = Objects.requireNonNull(nomMachine, "Le nom ne doit pas être nul").trim();
        this.nomMachine = trimmed;
    }

    public String getMachine() {
        if (this.nomMachine == null) {
            return "";
        }
        int idx = this.nomMachine.indexOf('.');
        return idx < 0 ? this.nomMachine : this.nomMachine.substring(0, idx);
    }


    public String getDomaine() {
        if (this.nomMachine == null) {
            return "";
        }
        int idx = this.nomMachine.indexOf('.');
        return idx < 0 ? "" : this.nomMachine.substring(idx + 1);
    }

    @Override
    public String toString() {
        return this.nomMachine == null ? "" : this.nomMachine;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof NomMachine)) {
            return false;
        }
        NomMachine that = (NomMachine) other;
        return Objects.equals(this.nomMachine, that.nomMachine);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.nomMachine);
    }

    private static boolean isValidNomMachine(String s) {
        // Longueur max FQDN classique: 253 caractères (optionnellement appliquée ici)
        if (s.isEmpty() || s.length() > 253) {
            return false;
        }
        int firstDot = s.indexOf('.');
        if (firstDot <= 0 || firstDot == s.length() - 1) {
            // pas de point, point en première/dernière position, ou pas de domaine
            return false;
        }
        String[] labels = s.split("\\.");
        if (labels.length < 2) {
            return false;
        }
        for (String label : labels) {
            if (!isValidLabel(label)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isValidLabel(String label) {
        if (label.isEmpty() || label.length() > 63) {
            return false;
        }

        if (!isAlphaNum(label.charAt(0)) || !isAlphaNum(label.charAt(label.length() - 1))) {
            return false;
        }
        for (int i = 0; i < label.length(); i++) {
            char c = label.charAt(i);
            if (!(isAlphaNum(c) || c == '-')) {
                return false;
            }
        }
        return true;
    }

    private static boolean isAlphaNum(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9');
    }
}
