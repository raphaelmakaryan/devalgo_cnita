package fr.raphaelmakaryan.bibliotheque.controllers;

import fr.raphaelmakaryan.bibliotheque.configurations.Cell;
import fr.raphaelmakaryan.bibliotheque.modeles.GameModele;

import java.util.Objects;
import java.util.Random;

public abstract class GameControllerBackup {
    protected GameModele game;
    private GameState state;

    public GameControllerBackup(GameModele game) {
        this.game = game;
        this.state = GameState.INITIAL;
    }

    /**
     * Logique de jeu
     */
    public void play() {
        isOver();
        if (!game.started) {
            randomPlayer();
            game.started = true;
        } else {
            handleEvent("PartieContinue");
            handleEvent("TourSuivant");
            nextPlayer();
        }
        game.tools.setTimeout(1);
        display();
    }

    /**
     * Affichage du tableau
     */
    public void display() {
        game.gameView.println("Au tour de " + game.whoPlayNow + " (" + game.getCurrentPlayerRepresentation() + ")");
        game.gameView.println(game.separationBoardGame());
        for (int i = 0; i < game.size; i++) {
            game.gameView.print("|");
            for (int j = 0; j < game.size; j++) {
                Cell c = game.board[i][j];
                game.gameView.print(c.getRepresentation());
                game.gameView.print("|");
            }
            System.out.print("\n");
            game.gameView.println(game.separationBoardGame());
        }
        game.tools.clearLine();
        if (game.whoPlayNow.contains("J")) {
            getMoveFromPlayer(game.interactionUtilisateur.userInterfaceMessage("Quelle case souhaiteriez-vous capturer ? (exemple : '1 1')"));
        } else {
            getMoveFromPlayer("bot");
        }
    }

    /**
     * Deuxieme fonction de vérification avant modification du plateau
     *
     * @param choice Choix du joueur
     */
    public void getMoveFromPlayer(String choice) {
        if (choice == null) {
            game.gameView.println("Vous avez décidé de fermer la page, fermeture du jeu.");
            System.exit(0);
        }
        Cell[][] board = game.getBoard();
        if (!Objects.equals(choice, "bot")) {
            if (game.verificationChoiceUser(choice)) {
                int[] valueUser = game.returnValueUser(choice);
                if (valueUser[0] > game.size || valueUser[1] > game.size || valueUser[0] < -1 || valueUser[1] < -1) {
                    game.gameView.println("Vous êtes sorti du tableau !");
                    display();
                } else if (game.verificationHavePlayer(board, valueUser)) {
                    game.gameView.println("Vous avez choisi une case deja prise !");
                    display();
                } else {
                    handlePlayerMove(valueUser[0], valueUser[1], "player");
                }
            } else {
                display();
            }
        } else {
            /*
            int lineRandomBot = new Random().nextInt(0, game.size);
            int columnRandomBot = new Random().nextInt(0, game.size);
            int[] valueBot = game.returnValueUser(lineRandomBot + " " + columnRandomBot);
            minimax();
            if (game.verificationHavePlayer(board, valueBot)) {
                game.gameView.println("Vous avez choisi une case deja prise !");
                display();
            } else {
                handlePlayerMove(valueBot[0], valueBot[1], "bot");
            }


            int lineRandomBot = new Random().nextInt(0, game.size);
            int columnRandomBot = new Random().nextInt(0, game.size);
            int[] valueBot = game.returnValueUser(lineRandomBot + " " + columnRandomBot);
            //int[] bestMove = findBestMove();
            if (game.verificationHavePlayer(board, bestMove)) {
                game.gameView.println("Vous avez choisi une case deja prise !");
                display();
            } else if (bestMove[0] != -1 && bestMove[1] != -1) {
                handlePlayerMove(bestMove[0], bestMove[1], "bot");
            }

             */
        }
    }

    /**
     * Trouve le meilleur coup à jouer pour le bot

     public int[] findBestMove() {
     int bestScore = Integer.MIN_VALUE;
     int[] bestMove = {-1, -1};

     String botSymbol = game.getBotPlayNow().getRepresentation();

     for (int i = 0; i < game.size; i++) {
     for (int j = 0; j < game.size; j++) {
     if (game.board[i][j].isEmpty()) {
     // Simule un coup du bot
     game.board[i][j].setRepresentation(botSymbol);

     // Évalue le score du coup
     int moveScore = minimax(game.board, 0, false);

     // Annule le coup
     game.board[i][j].setRepresentation("   ");

     // Si meilleur coup → on le garde
     if (moveScore > bestScore) {
     bestMove[0] = i;
     bestMove[1] = j;
     bestScore = moveScore;
     }
     }
     }
     }

     return bestMove;
     }

     public void minimax() {
     int score = 0;
     if (game.players.get(0).contains("B") && checkHorizontalMinimax(game.getBotPlayNow().getRepresentation())) {
     score = +10;
     } else if (game.players.get(1).contains("B") && checkHorizontalMinimax(game.getBotPlayNow().getRepresentation())) {
     score = +10;
     } else if (game.players.get(0).contains("J") && checkHorizontalMinimax(game.getPlayerPlayNow().getRepresentation())) {
     score = -10;
     } else if (game.players.get(1).contains("J") && checkHorizontalMinimax(game.getPlayerPlayNow().getRepresentation())) {
     score = -10;
     }
     //System.out.println(isMovesLeftMinimax());
     System.out.println("Score : " + score);
     }


     public void minimax() {


     int score = 0;

     if (checkHorizontalMinimax(botSymbol)) {
     score = +10;
     } else if (checkHorizontalMinimax(playerSymbol)) {
     score = -10;
     }

     System.out.println("Bot : " + botSymbol + " / Player : " + playerSymbol);
     System.out.println("Score : " + score);
     }


     public void minimax() {
     String botSymbol = game.getBotPlayNow() != null ? game.getBotPlayNow().getRepresentation() : " X ";
     String playerSymbol = game.getPlayerPlayNow() != null ? game.getPlayerPlayNow().getRepresentation() : " O ";

     System.out.println("Check bot : " + checkHorizontalMinimax(botSymbol));
     System.out.println("Check player : " + checkHorizontalMinimax(playerSymbol));
     }



     /**
     * Algorithme Minimax de base
     * - Évalue récursivement les coups possibles
     * - Renvoie un score selon qui gagne
     * - Sélectionne le meilleur coup pour le bot

     public int minimax(Cell[][] board, int depth, boolean isMaximizingPlayer) {
     // 1️⃣ On commence par évaluer la position actuelle
     //String botSymbol = game.getBotPlayNow().getRepresentation();
     //String playerSymbol = game.getPlayerPlayNow().getRepresentation();
     String botSymbol = game.getBotPlayNow() != null ? game.getBotPlayNow().getRepresentation() : " X ";
     String playerSymbol = game.getPlayerPlayNow() != null ? game.getPlayerPlayNow().getRepresentation() : " O ";

     // Si le bot gagne → +10
     if (checkHorizontalMinimax(botSymbol)) return +10 - depth; // profondeur pour favoriser victoire rapide
     // Si le joueur gagne → -10
     if (checkHorizontalMinimax(playerSymbol)) return -10 + depth;
     // Si plus de coups possibles → match nul
     if (!isMovesLeftMinimax()) return 0;

     // 2️⃣ Si c’est au tour du bot (maximizing player)
     if (isMaximizingPlayer) {
     int bestScore = Integer.MIN_VALUE;

     // Parcours de toutes les cases possibles
     for (int i = 0; i < game.size; i++) {
     for (int j = 0; j < game.size; j++) {
     // On vérifie si la case est libre
     if (board[i][j].isEmpty()) {
     // On simule un coup du bot
     board[i][j].setRepresentation(botSymbol);

     // On évalue le coup récursivement
     int score = minimax(board, depth + 1, false);

     // On annule le coup (backtracking)
     board[i][j].setRepresentation("   ");

     // On garde le meilleur score
     bestScore = Math.max(score, bestScore);
     }
     }
     }
     return bestScore;

     // 3️⃣ Sinon, c’est au joueur de jouer (minimizing player)
     } else {
     int bestScore = Integer.MAX_VALUE;

     for (int i = 0; i < game.size; i++) {
     for (int j = 0; j < game.size; j++) {
     if (board[i][j].isEmpty()) {
     // Simule un coup du joueur
     board[i][j].setRepresentation(playerSymbol);

     // Évalue récursivement le coup
     int score = minimax(board, depth + 1, true);

     // Annule le coup
     board[i][j].setRepresentation("   ");

     // Garde le pire score (le joueur veut minimiser)
     bestScore = Math.min(score, bestScore);
     }
     }
     }
     return bestScore;
     }
     }

     /**
     * Vérifie si un joueur a gagné horizontalement

     public boolean checkHorizontalMinimax(String representation) {
     for (int i = 0; i < game.size; i++) {
     int consecutive = 0;
     for (int j = 0; j < game.size; j++) {
     Cell c = game.board[i][j];
     if (c.getRepresentation().equals(representation)) {
     consecutive++;
     if (consecutive == (game.victoryValue )) {
     return true;
     }
     } else {
     consecutive = 0;
     }
     }
     }
     return false;
     }

     */

    /**
     * Choix du joueur random au debut du jeu
     */
    public void randomPlayer() {
        int value = new Random().nextInt(0, 1);
        if (game.players.get(0).contains("J") && game.players.get(1).contains("J")) {
            if (value == 0) {
                game.whoPlayNow = "J1";
            } else {
                game.whoPlayNow = "J2";
            }
        } else if (game.players.get(0).contains("J") && game.players.get(1).contains("B")) {
            if (value == 0) {
                game.whoPlayNow = "J1";
            } else {
                game.whoPlayNow = "B2";
            }
        } else if (game.players.get(0).contains("B") && game.players.get(1).contains("B")) {
            if (value == 0) {
                game.whoPlayNow = "B1";
            } else {
                game.whoPlayNow = "B2";
            }
        }
    }

    /**
     * Changement de joueur
     */
    public void nextPlayer() {
        if (game.players.get(0).contains("J") && game.players.get(1).contains("J")) {
            if (Objects.equals(game.whoPlayNow, "J1")) {
                game.whoPlayNow = "J2";
            } else if (Objects.equals(game.whoPlayNow, "J2")) {
                game.whoPlayNow = "J1";
            }
        }
        if (game.players.get(0).contains("J") && game.players.get(1).contains("B")) {
            if (Objects.equals(game.whoPlayNow, "J1")) {
                game.whoPlayNow = "B1";
            } else if (Objects.equals(game.whoPlayNow, "B1")) {
                game.whoPlayNow = "J1";
            }
        }
        if (game.players.get(0).contains("B") && game.players.get(1).contains("B")) {
            if (game.whoPlayNow.equals("B1")) {
                game.whoPlayNow = "B2";
            } else if (Objects.equals(game.whoPlayNow, "B2")) {
                game.whoPlayNow = "B1";

            }
        }
    }

    /**
     * Vérification de fin de jeu
     */
    public void isOver() {
        if (!Objects.equals(game.whoPlayNow, "null")) {
            if (game.checkWin()) {
                handleEvent("PartieTerminée");
                System.out.println("GG " + game.whoPlayNow);
                System.exit(0);
            }
            if (game.checkCellFilled() == (game.size * game.size)) {
                handleEvent("PartieTerminée");
                System.out.println("Match nul !");
                System.exit(0);
            }
        }
    }

    /**
     * Modification du tableau
     *
     * @param ligne   Ligne
     * @param colonne Colonne
     * @param type    Type de joueur
     */
    public void handlePlayerMove(int ligne, int colonne, String type) {
        handleEvent("ActionJoueur");
        game.setOwner(ligne, colonne, type);
        play();
    }

    /**
     * Création des joueurs
     *
     * @param value Valeur des joueurs
     */
    public void initializePlayers(int[] value) {
        handleEvent("JeuInitialisé");
        game.createPlayer(value);
        handleEvent("DébutDePartie");
        play();
    }

    /**
     * Retourne quel jeu est lancé
     *
     * @return Retourne les informations du jeu
     */
    public GameModele getGame() {
        return game;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public void handleEvent(String event) {
        switch (state) {
            case INITIAL:
                if ("JeuInitialisé".equals(event)) {
                    setState(GameState.ATTENTE_DEBUT);
                }
                break;
            case ATTENTE_DEBUT:
                if ("DébutDePartie".equals(event)) {
                    setState(GameState.TOUR_JOUEUR);
                }
                break;
            case TOUR_JOUEUR:
                if ("TourTerminé".equals(event)) {
                    setState(GameState.ATTENTE_TOUR_SUIVANT);
                } else if ("ActionJoueur".equals(event)) {
                    setState(GameState.VERIFICATION_FIN_PARTIE);
                }
                break;
            case ATTENTE_TOUR_SUIVANT:
                if ("TourSuivant".equals(event)) {
                    setState(GameState.TOUR_JOUEUR);
                } else if ("ActionJoueur".equals(event)) {
                    setState(GameState.VERIFICATION_FIN_PARTIE);
                }
                break;
            case VERIFICATION_FIN_PARTIE:
                if ("PartieTerminée".equals(event)) {
                    setState(GameState.FIN_PARTIE);
                } else if ("PartieContinue".equals(event)) {
                    setState(GameState.TOUR_JOUEUR);
                }
                break;
        }
    }
}