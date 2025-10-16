package fr.raphaelmakaryan.bibliotheque.models;

public class Cell {
    private String representation = "   ";

    /**
     * Récupère la representation de la case
     * @return retourne la representation de la case
     */
    public String getRepresentation() {
        return representation;
    }

    /**
     * Ajoute la representation de la case
     * @param value Valeur de la representation
     */
    public void setRepresentation(String value) {
        this.representation = value;
    }

    /**
     * Si la case est vide
     * @return Vrai ou faux
     */
    public boolean isEmpty() {
        return "   ".equals(this.representation);
    }
}