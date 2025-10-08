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

    /**
     * Deuxieme fonction de vérification avant modification du plateau
     *
     * @param choice Choix du joueur
     */
    @Override
    public void getMoveFromPlayer(String choice) {
        if (verificationChoiceUser(choice)) {
            int[] valueUser = tokenDescent(returnValueUser(choice));
            Cell[][] board = getBoard();
            if (valueUser[0] > size || valueUser[1] > size || valueUser[0] < -1 || valueUser[1] < -1) {
                gameView.println("Vous êtes sorti du tableau !");
                display();
            } else if (verificationHavePlayer(board, valueUser)) {
                gameView.println("Vous avez choisi une case deja prise !");
                display();
            } else {
                setOwner(valueUser[0], valueUser[1], "player");
            }
        } else {
            display();
        }
    }
}
