package fr.raphaelmakaryan.bibliotheque.configurations;

import fr.raphaelmakaryan.bibliotheque.modeles.GameModele;

public class Player {
    public String representation;

    /**
     * Création d'un joueur "joueur"
     *
     * @param gameModele Le jeu
     * @param value      Valeur de la place
     */
    public Player(GameModele gameModele, int value) {
        if (gameModele.getMode().equals("JvB")) {
            this.representation = gameModele.listRepresentation[0];
        } else {
            this.representation = (value == 1)
                    ? gameModele.listRepresentation[0]
                    : gameModele.listRepresentation[1];
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
        return "Player{" + "representation='" + representation + '\'' + '}';
    }
}