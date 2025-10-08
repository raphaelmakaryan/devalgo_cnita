package fr.raphaelmakaryan.bibliotheque.modeles;

import fr.raphaelmakaryan.bibliotheque.configurations.Cell;

public class TicTacToe extends GameModele {
    int[] leftRight = {0, 1, 2};
    int[] rightLeft = {2, 1, 0};

    public TicTacToe(int size, int victoryValue) {
        super(size, victoryValue);
    }

    /**
     * Fonctions principal si y'a victoire ou non
     *
     * @return Si il a gagné ou pas
     */
    @Override
    public boolean checkWin() {
        if (checkVertical() || checkHorizontal() || checkSide(leftRight) || checkSide(rightLeft)) {
            return true;
        }
        return false;
    }

    /**
     * Vérification si le joueur à gagner en vertical
     *
     * @return Si il a gagné a la vertical
     */
    @Override
    public boolean checkVertical() {
        int valeurColonne = 0;
        int checkValue = 0;
        int valueEqualsPlayer = 0;
        boolean result = false;
        while (size != checkValue) {
            for (int i = 0; i < board.length; i++) {
                Cell c = board[i][valeurColonne];
                if (whoPlayNow.contains("J") && c.getRepresentation().equals(getPlayerPlayNow().representation)) {
                    valueEqualsPlayer = valueEqualsPlayer + 1;
                } else if (whoPlayNow.contains("B") && c.getRepresentation().equals(getBotPlayNow().representation)) {
                    valueEqualsPlayer = valueEqualsPlayer + 1;
                } else {
                    valueEqualsPlayer = 0;
                }
            }
            if (valueEqualsPlayer == victoryValue) {
                result = true;
            } else {
                valeurColonne = valeurColonne + 1;
                valueEqualsPlayer = 0;
                checkValue = checkValue + 1;
            }
        }
        return result;
    }


    /**
     * Vérification si le joueur a gagné de droite à gauche
     *
     * @return si il a gagné
     */
    public boolean checkSide(int[] value) {
        int valueEqualsPlayer = 0;
        int valueCross = 0;
        boolean result = false;
        for (int j = 0; j < value.length; j++) {
            Cell c = board[valueCross][j];
            if (whoPlayNow.contains("J") && c.getRepresentation().equals(getPlayerPlayNow().representation)) {
                valueEqualsPlayer = valueEqualsPlayer + 1;
            } else if (whoPlayNow.contains("B") && c.getRepresentation().equals(getBotPlayNow().representation)) {
                valueEqualsPlayer = valueEqualsPlayer + 1;
            } else {
                valueEqualsPlayer = 0;
            }
            valueCross = valueCross + 1;
        }
        if (valueEqualsPlayer == victoryValue) {
            result = true;
        }
        return result;
    }
}