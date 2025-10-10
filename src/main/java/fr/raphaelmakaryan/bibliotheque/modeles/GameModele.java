package fr.raphaelmakaryan.bibliotheque.modeles;

import com.mongodb.client.MongoDatabase;
import fr.raphaelmakaryan.bibliotheque.configurations.*;
import fr.raphaelmakaryan.bibliotheque.controllers.CustomGameController;
import fr.raphaelmakaryan.bibliotheque.controllers.GomokuController;
import fr.raphaelmakaryan.bibliotheque.controllers.Puissance4Controller;
import fr.raphaelmakaryan.bibliotheque.controllers.TicTacToeController;
import fr.raphaelmakaryan.bibliotheque.pertinent.GameSerialization;
import org.bson.BsonObjectId;

import java.util.ArrayList;
import java.util.List;

public class GameModele implements GameModeleInterface {
    public int size;
    public String idGameDatabase;
    public int victoryValue;
    public Cell[][] board;
    public boolean started;
    public String whoPlayNow;
    public GameSerialization gameSerialization;
    public MongoDatabase database;

    public String gameSelected;
    public String[] listRepresentation;
    public String mode;

    public List<String> players = new ArrayList<>();
    public Player player1;
    public Player player2;
    public ArtificialPlayer bot1;
    public ArtificialPlayer bot2;

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

            case "perso":
                int sizeUser = customGameSize();
                int victoryUser = customGameVictory(sizeUser);
                if (sizeUser != 0 && victoryUser != 0) {
                    CustomGame modeleCustomGame = new CustomGame(sizeUser, victoryUser);
                    CustomGameController controllerCG = new CustomGameController(modeleCustomGame);
                    interactionUtilisateur.chooseGameCustomGame(controllerCG);
                }
                break;
            default:
                System.exit(0);
                break;
        }
    }

    public int customGameSize() {
        boolean canLeave = false;
        while (!canLeave) {
            String sizeUser = interactionUtilisateur.userInterfaceMessage("Quelle taille de plateau de jeu voulez-vous ?");
            if (sizeUser == null) {
                gameView.println("Veuillez donner une valeur pour la taille de votre plateau !");
            } else {
                try {
                    int sizeGame = Integer.parseInt(sizeUser);
                    if (sizeGame < 1) {
                        gameView.println("Veuillez donner une valeur supérieur a 1 !");
                    } else {
                        canLeave = true;
                        return sizeGame;
                    }
                } catch (Exception e) {
                    gameView.println("Les valeurs que vous avez définis ne sont pas des nombres !");
                }
            }
        }
        return 0;
    }

    public int customGameVictory(int sizeGame) {
        boolean canLeave = false;
        while (!canLeave) {
            String victoryUser = interactionUtilisateur.userInterfaceMessage("Combien de jetons faudrait-il aligner pour avoir une victoire ?");
            if (victoryUser == null) {
                gameView.println("Veuillez donner une valeur pour le nombre de jetons pour avoir une victoire !");
            } else {
                try {
                    int victoryGame = Integer.parseInt(victoryUser);
                    if (sizeGame < 1) {
                        gameView.println("Veuillez donner une valeur supérieur a 1 !");
                    } else if (sizeGame < victoryGame) {
                        gameView.println("Veuillez spécifier le nombre de jetons pour la victoire qui est inférieur à la taille du plateau que vous avez défini a " + sizeGame + " !");
                    } else {
                        canLeave = true;
                        return victoryGame;
                    }
                } catch (Exception e) {
                    gameView.println("Les valeurs que vous avez définis ne sont pas des nombres !");
                }
            }
        }
        return 0;
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
     * Met a jour le plateau
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
            gameSerialization.dbUpdateGame(database, player.getIdDatabase(), idGameDatabase, colonne, ligne);
        } else if (type.equals("bot")) {
            ArtificialPlayer bot = getBotPlayNow();
            board[ligne][colonne].setRepresentation(bot.getRepresentation());
            gameSerialization.dbUpdateGame(database, bot.getIdDatabase(), idGameDatabase, colonne, ligne);
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
        return checkVertical() || checkHorizontal() || checkDiagonal();
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
        while ((size * size) != checkValue) {
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
            for (Cell[] cells : board) {
                Cell c = cells[valeurColonne];
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
        for (Cell[] cells : board) {
            for (int j = 0; j < board.length; j++) {
                if (!cells[j].isEmpty()) {
                    valueRempli = valueRempli + 1;
                }
            }
        }
        return valueRempli;
    }

    /**
     * Vérifie les diagonal
     *
     * @return Vrai ou faux
     */
    public boolean checkDiagonal() {
        int valueEqualsPlayer = 0;
        boolean result = false;
        for (int i = 0; i < size; i++) {
            if (board[i][i].getRepresentation().equals(getCurrentPlayerRepresentation())) {
                valueEqualsPlayer = valueEqualsPlayer + 1;
            } else if (valueEqualsPlayer == victoryValue) {
                result = true;
            } else {
                valueEqualsPlayer = 0;
            }
        }
        for (int i = 0; i < size; i++) {
            if (board[i][size - 1 - i].getRepresentation().equals(getCurrentPlayerRepresentation())) {
                valueEqualsPlayer = valueEqualsPlayer + 1;
            } else if (valueEqualsPlayer == victoryValue) {
                result = true;
            } else {
                valueEqualsPlayer = 0;
            }
        }
        return result;
    }

    /**
     * Permet de récupérer le symbole du joueur actuel
     *
     * @return Retourne son symbole
     */
    public String getCurrentPlayerRepresentation() {
        return switch (whoPlayNow) {
            case "J1" -> player1.getRepresentation();
            case "J2" -> player2.getRepresentation();
            case "B1" -> bot1.getRepresentation();
            case "B2" -> bot2.getRepresentation();
            default -> "UNDEFINED";
        };
    }

    /**
     * Renvoie le string selon le jeu
     *
     * @return Retourne la séparation
     */
    public String separationBoardGame() {
        return "-----".repeat(size);
    }

    /**
     * Crée les joueurs selon le choix du mode de jeu
     *
     * @param value Valeur du joueur
     */
    public void createPlayer(int[] value) {
        String player1Database = "";
        String player2Database = "";
        for (int j : value) {
            if (j == 10) {
                player1 = new Player(this, 1, "");
                player1Database = gameSerialization.dbCreateUser(database, "Joueur", "J1", player1.getRepresentation());
                player1.setIdDatabase(player1Database);
                players.add("J1");
            }
            if (j == 11) {
                player2 = new Player(this, 2, "");
                player2Database = gameSerialization.dbCreateUser(database, "Joueur", "J2", player2.getRepresentation());
                player2.setIdDatabase(player1Database);
                players.add("J2");
            }
            if (j == 20) {
                bot1 = new ArtificialPlayer(this, 1, "");
                player1Database = gameSerialization.dbCreateUser(database, "BOT", "B1", bot1.getRepresentation());
                bot1.setIdDatabase(player1Database);
                players.add("B1");
            }
            if (j == 21) {
                bot2 = new ArtificialPlayer(this, 2, "");
                player2Database = gameSerialization.dbCreateUser(database, "BOT", "B2", bot2.getRepresentation());
                bot2.setIdDatabase(player1Database);
                players.add("B2");
            }
        }
        setIdGameDatabase(gameSerialization.dbCreateGame(database, getMode(), size, victoryValue, player1Database, player2Database));
    }

    /**
     * Récupère le board
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
     * Récupère le mode
     *
     * @return Retourne le mode
     */
    public String getMode() {
        return mode;
    }

    /**
     * Mets à jour le jeu sélectionné
     *
     * @param gameSelected Jeu sélectionné
     */
    public void setGameSelected(String gameSelected) {
        this.gameSelected = gameSelected;
    }

    /**
     * Récupère le jeu sélectionné
     *
     * @return Le retourne
     */
    public String getGameSelected() {
        return gameSelected;
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    public void setDatabase(MongoDatabase database) {
        this.database = database;
    }

    public GameSerialization getGameSerialization() {
        return gameSerialization;
    }

    public void setGameSerialization(GameSerialization gameSerialization) {
        this.gameSerialization = gameSerialization;
    }

    public String getIdGameDatabase() {
        return idGameDatabase;
    }

    public void setIdGameDatabase(String idGameDatabase) {
        this.idGameDatabase = idGameDatabase;
    }
}
