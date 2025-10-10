package fr.raphaelmakaryan.bibliotheque.configurations;

import fr.raphaelmakaryan.bibliotheque.modeles.GameModele;

public class ArtificialPlayer {
    public String representation;

    /**
     * Création d'un joueur "bot"
     *
     * @param gameModele  Le jeu
     * @param value Valeur de la place
     */
    public ArtificialPlayer(GameModele gameModele, int value) {
        if (gameModele.mode.equals("JvB")) {
            this.representation = gameModele.listRepresentation[1];
        } else {
            if (value == 1) {
                this.representation = gameModele.listRepresentation[0];
            } else {
                this.representation = gameModele.listRepresentation[1];
            }
        }
    }

    /**
     * Récupère le signe
     *
     * @return retourne le signe
     */
    public String getRepresentation() {
        return representation;
    }

    @Override
    public String toString() {
        return "ArtificialPlayer{" +
                "representation='" + representation + '\'' +
                '}';
    }
}
