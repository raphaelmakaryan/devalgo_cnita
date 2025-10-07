package fr.raphaelmakaryan.tictactoe.games;

import fr.raphaelmakaryan.tictactoe.configurations.InteractionUtilisateur;
import fr.raphaelmakaryan.tictactoe.configurations.Player;
import fr.raphaelmakaryan.tictactoe.tools.Tools;
import fr.raphaelmakaryan.tictactoe.configurations.View;
import fr.raphaelmakaryan.tictactoe.configurations.ArtificialPlayer;
import fr.raphaelmakaryan.tictactoe.configurations.Cell;

import java.util.*;

public class TicTacToe {
    private int size = 3;
    private Cell[][] board;
    public boolean started = false;
    public List<String> players = new ArrayList<>();
    public String whoPlayNow = "null";
    public String mode;
    public String[] listRepresentation = {" O ", " X "};
    int[] leftRight = {0, 1, 2};
    int[] rightLeft = {2, 1, 0};

    public Player player1;
    public Player player2;
    public ArtificialPlayer bot1;
    public ArtificialPlayer bot2;

    public InteractionUtilisateur interactionUtilisateur = new InteractionUtilisateur();
    public Tools tools = new Tools();
    public View view = new View();

    /**
     * Création du tableau
     */
    public TicTacToe() {
        this.board = new Cell[this.size][this.size];
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                this.board[i][j] = new Cell();
            }
        }
    }

    /**
     * Affichage au choix de l'user pour quel mode de jeu
     */
    public void chooseGame() {
        interactionUtilisateur.chooseGame(this);
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
        view.println("-------------");
        for (int i = 0; i < this.size; i++) {
            view.print("|");
            for (int j = 0; j < this.size; j++) {
                Cell c = this.board[i][j];
                view.print(c.getRepresentation());
                view.print("|");
            }
            System.out.print("\n");
            view.println("-------------");
        }
        tools.clearLine();
        if (whoPlayNow.contains("J")) {
            getMoveFromPlayer(interactionUtilisateur.userInterfaceMessage("Quelle case souhaiteriez-vous capturer ? (exemple : '1 1')"));
        } else {
            getMoveFromPlayer("bot");
        }
    }

    /**
     * Getter pour avoir le plateau
     *
     * @return Retourne le tableau
     */
    public Cell[][] getBoard() {
        return board;
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
        if (valueUser[0] < 0 || valueUser[0] > 2 || valueUser[1] < 0 || valueUser[1] > 2) {
            view.println("Une des valeur des cases définis et sois inférieur a 0 ou supérieur a 2 !");
            return false;
        }
        return true;
    }

    /**
     * Deuxieme fonction de vérification avant modification du plateau
     *
     * @param choice Choix du joueur
     */
    public void getMoveFromPlayer(String choice) {
        if (!Objects.equals(choice, "bot")) {
            if (verificationChoiceUser(choice)) {
                int[] valueUser = returnValueUser(choice);
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
        } else {
            int lineRandomBot = new Random().nextInt(0, 3);
            int columnRandomBot = new Random().nextInt(0, 3);
            int[] valueBot = returnValueUser(lineRandomBot + " " + columnRandomBot);
            if (verificationHavePlayer(board, valueBot)) {
                view.println("Vous avez choisi une case deja prise !");
                display();
            } else {
                setOwner(valueBot[0], valueBot[1], "bot");
            }
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
        } else if (type.equals("bot")) {
            ArtificialPlayer bot = getBotPlayNow();
            board[ligne][colonne].setRepresentation(bot.getRepresentation());
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
     * Retourne quel bot est actuellement en train de jouer
     *
     * @return Retourne le bot actuel
     */
    public ArtificialPlayer getBotPlayNow() {
        if (whoPlayNow.equals("B1")) {
            return bot1;
        } else {
            return bot2;
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
            if (checkCellFilled() == 9) {
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
                } else if (valueEqualsPlayer == 3) {
                    result = true;
                } else {
                    valueEqualsPlayer = 0;
                }
            }
            valeurColonne = valeurColonne + 1;
            valueEqualsPlayer = 0;
            checkValue = checkValue + 1;
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
                } else if (valueEqualsPlayer == 3) {
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
        if (valueEqualsPlayer == 3) {
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
}