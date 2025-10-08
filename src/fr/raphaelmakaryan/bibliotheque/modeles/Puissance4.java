package fr.raphaelmakaryan.bibliotheque.modeles;

import fr.raphaelmakaryan.bibliotheque.configurations.Cell;

public class Puissance4 extends GameModele {

    public Puissance4(int size, int victoryValue) {
        super(size, victoryValue);
    }

    /**
     * Permet de vérifier si il y a deja un jeton tout en bas
     *
     * @param value Valeur écrit de base par le joueur
     * @return Retourne les nouvel valeur
     */
    public int[] tokenDescent(int[] value) {
        int valeurColonne = value[0];
        int[] newValue = new int[2];
        int valueDecrease = size - 1;
        boolean valueFind = false;
        for (int i = 0; i < board.length; i++) {
            Cell c = board[valueDecrease][valeurColonne];
            if (whoPlayNow.contains("J") && c.getRepresentation().equals("   ") && !valueFind) {
                newValue[0] = valueDecrease;
                newValue[1] = valeurColonne;
                valueFind = true;
            } else {
                valueDecrease = valueDecrease - 1;
            }
        }
        return newValue;
    }
}
