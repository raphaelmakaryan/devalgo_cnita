package fr.raphaelmakaryan.bibliotheque.modeles;

import fr.raphaelmakaryan.bibliotheque.configurations.*;
import fr.raphaelmakaryan.bibliotheque.tools.Tools;
import fr.raphaelmakaryan.bibliotheque.view.GameView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class GameModele {
    protected int size;
    protected int victoryValue;
    protected Cell[][] board;
    protected boolean started;
    protected String whoPlayNow;

    public String gameSelected;
    public String[] listRepresentation;
    public String mode;

    protected List<String> players = new ArrayList<>();
    protected Player player1;
    protected Player player2;
    protected ArtificialPlayer bot1;
    protected ArtificialPlayer bot2;

    protected Tools tools = new Tools();
    protected GameView gameView = new GameView();
    protected InteractionUtilisateur interactionUtilisateur = new InteractionUtilisateur();

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
                TicTacToe ticTacToe = new TicTacToe(3, 3);
                interactionUtilisateur.chooseGameTicTacToe(ticTacToe);
                break;

            case "p4":
                Puissance4 puissance4 = new Puissance4(7, 4);
                interactionUtilisateur.chooseGameP4(puissance4);
                break;

            case "gomoku":
                Gomoku gomoku = new Gomoku(15, 5);
                interactionUtilisateur.chooseGameGomoku(gomoku);
                break;
            default:
                System.exit(0);
                break;
        }
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
        tools.setTimeout(1);
        display();
    }

    /**
     * Affichage du tableau
     */
    public void display() {
        gameView.println("Au tour de " + whoPlayNow + " (" + getCurrentPlayerRepresentation() + ")");
        gameView.println(separationBoardGame());
        for (int i = 0; i < this.size; i++) {
            gameView.print("|");
            for (int j = 0; j < this.size; j++) {
                Cell c = this.board[i][j];
                gameView.print(c.getRepresentation());
                gameView.print("|");
            }
            System.out.print("\n");
            gameView.println(separationBoardGame());
        }
        tools.clearLine();
        if (whoPlayNow.contains("J")) {
            getMoveFromPlayer(interactionUtilisateur.userInterfaceMessage("Quelle case souhaiteriez-vous capturer ? (exemple : '1 1')"));
        } else {
            getMoveFromPlayer("bot");
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

    /**
     * Deuxieme fonction de vérification avant modification du plateau
     *
     * @param choice Choix du joueur
     */
    public void getMoveFromPlayer(String choice) {
        if (!Objects.equals(choice, "bot")) {
            if (verificationChoiceUser(choice)) {
                int[] valueUser = returnValueUser(choice);
                Cell[][] board = this.getBoard();
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
        } else {
            int lineRandomBot = new Random().nextInt(0, size);
            int columnRandomBot = new Random().nextInt(0, size);
            int[] valueBot = returnValueUser(lineRandomBot + " " + columnRandomBot);
            if (verificationHavePlayer(board, valueBot)) {
                gameView.println("Vous avez choisi une case deja prise !");
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
                System.out.println("GG " + whoPlayNow);
                System.exit(0);
            }
            if (checkCellFilled() == (size * size)) {
                System.out.println("Match nul !");
                System.exit(0);
            }
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

    /**
     * Renvoie le string selon le jeu
     *
     * @return Retourne la séparation
     */
    public String separationBoardGame() {
        ;
        return switch (getGameSelected()) {
            case "tictactoe" -> "-------------";
            case "p4" -> "-----------------------------";
            case "gomoku" -> "-------------------------------------------------------------";
            default -> "---";
        };
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

    public void setGameSelected(String gameSelected) {
        this.gameSelected = gameSelected;
    }

    public String getGameSelected() {
        return gameSelected;
    }
}
