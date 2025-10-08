package fr.raphaelmakaryan.bibliotheque.games;

import fr.raphaelmakaryan.bibliotheque.configurations.Player;
import fr.raphaelmakaryan.bibliotheque.configurations.ArtificialPlayer;
import fr.raphaelmakaryan.bibliotheque.configurations.Cell;

import java.util.*;

public class TicTacToe extends Game {
    public TicTacToe gameTTT;
    public Game gameAll;
    public List<String> players = new ArrayList<>();
    int[] leftRight = {0, 1, 2};
    int[] rightLeft = {2, 1, 0};

    public TicTacToe(int size, int victoryValue) {
        super(size, victoryValue);
    }

    /**
     * Choix du joueur random au debut du jeu
     */
    public void randomPlayer() {
        int value = new Random().nextInt(0, 1);
        if (players.get(0).contains("J") && players.get(1).contains("J")) {
            if (value == 0) {
                whoPlayNow = "J1";
            } else {
                whoPlayNow = "J2";
            }
        } else if (players.get(0).contains("J") && players.get(1).contains("B")) {
            if (value == 0) {
                whoPlayNow = "J1";
            } else {
                whoPlayNow = "B2";
            }
        } else if (players.get(0).contains("B") && players.get(1).contains("B")) {
            if (value == 0) {
                whoPlayNow = "B1";
            } else {
                whoPlayNow = "B2";
            }
        }
    }


    /**
     * Changement de joueur
     */
    public void nextPlayer() {
        if (players.get(0).contains("J") && players.get(1).contains("J")) {
            if (Objects.equals(whoPlayNow, "J1")) {
                whoPlayNow = "J2";
            } else if (Objects.equals(whoPlayNow, "J2")) {
                whoPlayNow = "J1";
            }
        }
        if (players.get(0).contains("J") && players.get(1).contains("B")) {
            if (Objects.equals(whoPlayNow, "J1")) {
                whoPlayNow = "B1";
            } else if (Objects.equals(whoPlayNow, "B1")) {
                whoPlayNow = "J1";
            }
        }
        if (players.get(0).contains("B") && players.get(1).contains("B")) {
            if (whoPlayNow.equals("B1")) {
                whoPlayNow = "B2";
            } else if (Objects.equals(whoPlayNow, "B2")) {
                whoPlayNow = "B1";
            }
        }
    }

    /**
     * Vérifie si tout le plateau est rempli
     *
     * @return Retourne le nombre de case rempli
     */
    public int checkCellFilled() {
        int valueRempli = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (!board[i][j].isEmpty()) {
                    valueRempli = valueRempli + 1;
                }
            }
        }
        return valueRempli;
    }

    /**
     * Fonctions principal si y'a victoire ou non
     *
     * @return Si il a gagné ou pas
     */
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
     * Vérification si le joueur a gagné à l'horizontal
     *
     * @return Si il a gagné a horizontal
     */
    public boolean checkHorizontal() {
        boolean result = false;
        int valeurLigne = 0;
        int checkValue = 0;
        int valueEqualsPlayer = 0;
        while (size * 3 != checkValue) {
            for (int i = 0; i < size; i++) {
                Cell c = board[valeurLigne][i];
                if (whoPlayNow.contains("J") && c.getRepresentation().equals(getPlayerPlayNow().representation)) {
                    valueEqualsPlayer = valueEqualsPlayer + 1;
                } else if (whoPlayNow.contains("B") && c.getRepresentation().equals(getBotPlayNow().representation)) {
                    valueEqualsPlayer = valueEqualsPlayer + 1;
                } else if (valueEqualsPlayer == victoryValue) {
                    result = true;
                } else {
                    valueEqualsPlayer = 0;
                }
                checkValue = checkValue + 1;
            }
            valeurLigne = valeurLigne + 1;
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

    /**
     * Crée les joueurs selon le choix du mode de jeu
     *
     * @param value Valeur du joueur
     */
    public void createPlayer(int[] value) {
        for (int i = 0; i < value.length; i++) {
            if (value[i] == 10) {
                player1 = new Player(this, 1);
                players.add("J1");
            }
            if (value[i] == 11) {
                player2 = new Player(this, 2);
                players.add("J2");
            }
            if (value[i] == 20) {
                bot1 = new ArtificialPlayer(this, 1);
                players.add("B1");
            }
            if (value[i] == 21) {
                bot2 = new ArtificialPlayer(this, 2);
                players.add("B1");
            }
        }
        play();
    }

    /**
     * Permet de récupérer le symbole du joueur actuel
     *
     * @return Retourne son symbole
     */
    public String getCurrentPlayerRepresentation() {
        if (whoPlayNow.equals("J1")) {
            return player1.getRepresentation();
        } else if (whoPlayNow.equals("J2")) {
            return player2.getRepresentation();
        } else if (whoPlayNow.equals("B1")) {
            return bot1.getRepresentation();
        } else if (whoPlayNow.equals("B2")) {
            return bot2.getRepresentation();
        }
        return "UNDEFINED";
    }

    public void setGameAll(Game gameAll) {
        this.gameAll = gameAll;
    }

    public void setGameTTT(TicTacToe gameTTT) {
        this.gameTTT = gameTTT;
    }
}