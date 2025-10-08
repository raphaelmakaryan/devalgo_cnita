package fr.raphaelmakaryan.bibliotheque.modeles;

import fr.raphaelmakaryan.bibliotheque.configurations.*;
import fr.raphaelmakaryan.bibliotheque.controllers.GomokuController;
import fr.raphaelmakaryan.bibliotheque.controllers.Puissance4Controller;
import fr.raphaelmakaryan.bibliotheque.controllers.TicTacToeController;
import fr.raphaelmakaryan.bibliotheque.tools.Tools;
import fr.raphaelmakaryan.bibliotheque.view.GameView;

import java.util.ArrayList;
import java.util.List;

public class GameModele {
    public int size;
    protected int victoryValue;
    public Cell[][] board;
    public boolean started;
    public String whoPlayNow;

    public String gameSelected;
    public String[] listRepresentation;
    public String mode;

    public List<String> players = new ArrayList<>();
    public Player player1;
    public Player player2;
    public ArtificialPlayer bot1;
    public ArtificialPlayer bot2;

    public Tools tools = new Tools();
    public GameView gameView = new GameView();
    public InteractionUtilisateur interactionUtilisateur = new InteractionUtilisateur();

    /**
     * Constructeur des jeux
     *
     * @param size         Taille des plateaux
     * @param victoryValue Valeur de victoire
     */
    public GameModele(int size, int victoryValue) {
        this.size = size;
        this.victoryValue = victoryValue;
        this.board = new Cell[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.board[i][j] = new Cell();
            }
        }
        this.started = false;
        this.whoPlayNow = "null";
        this.listRepresentation = new String[]{" O ", " X "};
    }

    /**
     * Input de début pour le choix de jeu
     */
    public void chooseGame() {
        String value = interactionUtilisateur.chooseGame();
        switch (value) {
            case "tictactoe":
                TicTacToe modeleTTT = new TicTacToe(3, 3);
                TicTacToeController controllerTTT = new TicTacToeController(modeleTTT);
                interactionUtilisateur.chooseGameTicTacToe(controllerTTT);
                break;

            case "p4":
                Puissance4 modeleP4 = new Puissance4(7, 4);
                Puissance4Controller controllerP4 = new Puissance4Controller(modeleP4);
                interactionUtilisateur.chooseGameP4(controllerP4);
                break;

            case "gomoku":
                Gomoku modeleGO = new Gomoku(15, 5);
                GomokuController controllerGO = new GomokuController(modeleGO);
                interactionUtilisateur.chooseGameGomoku(controllerGO);
                break;
            default:
                System.exit(0);
                break;
        }
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
                gameView.println("Veuillez récrire des chiffres avec un espace !");
                return false;
            }
        }
        if (choice.length() != 3) {
            gameView.println("Veuillez récrire !");
            return false;
        }
        if (valueUser[0] < 0 || valueUser[0] > size || valueUser[1] < 0 || valueUser[1] > size) {
            gameView.println("Une des valeur des cases définis et sois inférieur a 0 ou supérieur a" + size + " !");
            return false;
        }
        return true;
    }

    public void setOwner(int ligne, int colonne, String type) {
        Cell[][] board = getBoard();

        if (type.equals("player")) {
            Player player = getPlayerPlayNow();
            board[ligne][colonne].setRepresentation(player.getRepresentation());
        } else if (type.equals("bot")) {
            ArtificialPlayer bot = getBotPlayNow();
            board[ligne][colonne].setRepresentation(bot.getRepresentation());
        }
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
                } else {
                    valueEqualsPlayer = 0;
                }
                checkValue = checkValue + 1;
            }
            if (valueEqualsPlayer == victoryValue) {
                result = true;
            }
            valeurLigne = valeurLigne + 1;
        }
        return result;
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

    /**
     * Renvoie le string selon le jeu
     *
     * @return Retourne la séparation
     */
    public String separationBoardGame() {
        return switch (getGameSelected()) {
            case "tictactoe" -> "-------------";
            case "p4" -> "-----------------------------";
            case "gomoku" -> "-------------------------------------------------------------";
            default -> "---";
        };
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
    }


    /**
     * Récupere le board
     *
     * @return Retourne le board
     */
    public Cell[][] getBoard() {
        return board;
    }

    /**
     * Met à jour le mode de jeu (TicTacToe)
     *
     * @param mode mode choisi
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     * Récupere le mode
     *
     * @return Retourne le mode
     */
    public String getMode() {
        return mode;
    }

    /**
     * Met a jour le jeu selectionné
     * @param gameSelected Jeu selectionné
     */
    public void setGameSelected(String gameSelected) {
        this.gameSelected = gameSelected;
    }

    /**
     * Récupere le jeu selectionné
     * @return Le retourne
     */
    public String getGameSelected() {
        return gameSelected;
    }
}
