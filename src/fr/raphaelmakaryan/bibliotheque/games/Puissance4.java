package fr.raphaelmakaryan.bibliotheque.games;

import fr.raphaelmakaryan.bibliotheque.configurations.Cell;
import fr.raphaelmakaryan.bibliotheque.configurations.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Puissance4 extends Game {
    public Puissance4 gamePuissance4;
    public Game gameAll;
    public List<String> players = new ArrayList<>();

    public Player player1;
    public Player player2;

    public Puissance4(int size, int victoryValue) {
        super(size, victoryValue);
    }

    /**
     * Logique de jeu
     */
    public void play() {
        isOver();
        if (!started) {
            randomPlayer();
            started = true;
        } else {
            nextPlayer();
        }
        display();
    }

    /**
     * Affichage du tableau
     */
    public void display() {
        view.println("Au tour de " + whoPlayNow + " (" + getCurrentPlayerRepresentation() + ")");
        view.println("-----------------------------");
        for (int i = 0; i < this.size; i++) {
            view.print("|");
            for (int j = 0; j < this.size; j++) {
                Cell c = this.board[i][j];
                view.print(c.getRepresentation());
                view.print("|");
            }
            System.out.print("\n");
            view.println("-----------------------------");
        }
        tools.clearLine();
        getMoveFromPlayer(interactionUtilisateur.userInterfaceMessage("Quelle case souhaiteriez-vous capturer ? (exemple : '1 1')"));
    }

    /**
     * Retourne la ligne/colonne choisie par l'user
     *
     * @param choice Valeur de l'user récupéré via le texte
     * @return Retourne dans un tableau
     */
    public int[] returnValueUser(String choice) {
        String[] splitValeur = choice.split(" ");
        int[] valueUser = new int[2];
        for (int i = 0; i < splitValeur.length; i++) {
            valueUser[i] = Integer.parseInt(splitValeur[i]);
        }
        return valueUser;
    }

    /**
     * Vérification des conditions avant modification du plateau
     *
     * @param choice Choix du joueur
     * @return Retourne si oui ou non, s'il peut jouer
     */
    public boolean verificationChoiceUser(String choice) {
        String[] splitValeur = choice.split(" ");
        int[] valueUser = new int[2];
        for (int i = 0; i < splitValeur.length; i++) {
            try {
                valueUser[i] = Integer.parseInt(splitValeur[i]);
            } catch (Exception e) {
                view.println("Veuillez récrire des chiffres avec un espace !");
                return false;
            }
        }
        if (choice.length() != 3) {
            view.println("Veuillez récrire !");
            return false;
        }
        if (valueUser[0] < 0 || valueUser[0] > size || valueUser[1] < 0 || valueUser[1] > size) {
            view.println("Une des valeur des cases définis et sois inférieur a 0 ou supérieur a" + size + " !");
            return false;
        }
        return true;
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
    public void getMoveFromPlayer(String choice) {
        if (verificationChoiceUser(choice)) {
            int[] valueUser = tokenDescent(returnValueUser(choice));
            Cell[][] board = getBoard();
            if (valueUser[0] > size || valueUser[1] > size || valueUser[0] < -1 || valueUser[1] < -1) {
                view.println("Vous êtes sorti du tableau !");
                display();
            } else if (verificationHavePlayer(board, valueUser)) {
                view.println("Vous avez choisi une case deja prise !");
                display();
            } else {
                setOwner(valueUser[0], valueUser[1], "player");
            }
        } else {
            display();
        }
    }

    /**
     * Modification du plateau
     *
     * @param ligne   Ligne
     * @param colonne Colonne
     * @param type    Type de joueur
     */
    public void setOwner(int ligne, int colonne, String type) {
        Cell[][] board = getBoard();
        if (type.equals("player")) {
            Player player = getPlayerPlayNow();
            board[ligne][colonne].setRepresentation(player.getRepresentation());
        }
        play();
    }

    /**
     * Verification s'il y a deja un joueur ou non
     *
     * @param board     Tableau
     * @param valueUser Valeur ligne/colonne du joueur
     * @return Retourne s'il y a ou pas un joueur deja dans la case
     */
    public boolean verificationHavePlayer(Cell[][] board, int[] valueUser) {
        String[] listPlayers = this.listRepresentation;
        for (String listPlayer : listPlayers) {
            Cell c = board[valueUser[0]][valueUser[1]];
            if (c.getRepresentation().equals(listPlayer)) {
                return true;
            }
        }
        return false;
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
    }

    /**
     * Retourne quel user est actuellement en train de jouer
     *
     * @return Retourne le joueur actuel
     */
    public Player getPlayerPlayNow() {
        if (whoPlayNow.equals("J1")) {
            return player1;
        } else {
            return player2;
        }
    }

    /**
     * Vérification de fin de jeu
     */
    public void isOver() {
        if (!Objects.equals(whoPlayNow, "null")) {
            if (checkWin()) {
                view.println("GG " + whoPlayNow);
                System.exit(0);
            }
            if (checkCellFilled() == (size * size)) {
                view.println("Vous avez tout rempli du coup fin du match !");
                System.exit(0);
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
        if (checkVertical() || checkHorizontal()) {
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
        while (size - 1 != checkValue) {
            for (int i = 0; i < board.length; i++) {
                Cell c = board[i][valeurColonne];
                if (whoPlayNow.contains("J") && c.getRepresentation().equals(getPlayerPlayNow().representation)) {
                    valueEqualsPlayer = valueEqualsPlayer + 1;
                } else {
                    valueEqualsPlayer = 0;
                }
            }
            if (valueEqualsPlayer == this.victoryValue) {
                result = true;
                checkValue = size - 1;
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
        while (size * size != checkValue) {
            for (int i = 0; i < size; i++) {
                Cell c = board[valeurLigne][i];
                if (whoPlayNow.contains("J") && c.getRepresentation().equals(getPlayerPlayNow().representation)) {
                    valueEqualsPlayer = valueEqualsPlayer + 1;
                } else if (valueEqualsPlayer == this.victoryValue) {
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
        }
        return "UNDEFINED";
    }

    public void setGameAll(Game gameAll) {
        this.gameAll = gameAll;
    }

    public void setGameP4(Puissance4 gamePuissance4) {
        this.gamePuissance4 = gamePuissance4;
    }

    /**
     * Getter pour avoir le plateau
     *
     * @return Retourne le tableau
     */
    public Cell[][] getBoard() {
        return board;
    }
}
