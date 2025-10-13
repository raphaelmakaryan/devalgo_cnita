package fr.raphaelmakaryan.bibliotheque.configurations;

import fr.raphaelmakaryan.bibliotheque.modeles.GameModele;

public class Player {
    public String representation;
    public String idDatabase;

    /**
     * Création d'un joueur "joueur"
     *
     * @param gameModele Le jeu
     * @param value      Valeur de la place
     */
    public Player(GameModele gameModele, int value, String idDatabase) {
        if (gameModele.getMode().equals("JvB")) {
            this.representation = gameModele.listRepresentation[0];
        } else {
            this.representation = (value == 1)
                    ? gameModele.listRepresentation[0]
                    : gameModele.listRepresentation[1];
        }
        this.idDatabase = idDatabase;
    }

    /**
     * Récupère le signe
     *
     * @return retourne le signe
     */
    public String getRepresentation() {
        return representation;
    }

    public String getIdDatabase() {
        return idDatabase;
    }

    public void setIdDatabase(String idDatabase) {
        this.idDatabase = idDatabase;
    }

    public void setRepresentation(String representation) {
        this.representation = representation;
    }

    @Override
    public String toString() {
        return "Player{" + "representation='" + representation + '\'' + '}';
    }
}