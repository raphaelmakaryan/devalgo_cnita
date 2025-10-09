package fr.raphaelmakaryan.bibliotheque.controllers;

import fr.raphaelmakaryan.bibliotheque.configurations.Cell;
import fr.raphaelmakaryan.bibliotheque.modeles.GameModele;

public class Minimax {
    protected GameModele game;

    public Minimax(GameModele game) {
        this.game = game;
    }

    /**
     * Fonction minimax, simule tous les coups possibles
     *
     * @param board        le plateau sur lequel on simule
     * @param depth        profondeur actuelle (0 au début)
     * @param isMaximizing true si c’est au tour du bot (on veut maximiser le score)
     * @param botSymbol    symbole du bot
     * @param playerSymbol symbole du joueur
     * @return un score estimé pour cette position
     */
    private int minimax(Cell[][] board, int depth, boolean isMaximizing,
                        String botSymbol, String playerSymbol) {

        int score = evaluate(board, botSymbol, playerSymbol);
        if (score == 10) {
            return score - depth;  // victoire bot
        }
        if (score == -10) {
            return score + depth;  // victoire joueur
        }
        if (!isMovesLeft(board)) {
            return 0;  // match nul
        }

        if (isMaximizing) {
            int best = Integer.MIN_VALUE;
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[0].length; j++) {
                    if (board[i][j].isEmpty()) {
                        // simule le coup du bot
                        board[i][j].setRepresentation(botSymbol);

                        int val = minimax(board, depth + 1, false, botSymbol, playerSymbol);

                        // annule le coup
                        board[i][j].setRepresentation("   ");

                        best = Math.max(best, val);
                    }
                }
            }
            return best;

        } else {
            int best = Integer.MAX_VALUE;
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[0].length; j++) {
                    if (board[i][j].isEmpty()) {
                        // simule le coup du joueur
                        board[i][j].setRepresentation(playerSymbol);

                        int val = minimax(board, depth + 1, true, botSymbol, playerSymbol);

                        // annule le coup
                        board[i][j].setRepresentation("   ");

                        best = Math.min(best, val);
                    }
                }
            }
            return best;
        }
    }

    /**
     * Trouve le meilleur coup pour le bot en utilisant minimax
     */
    public int[] findBestMove() {
        String botSymbol = game.getCurrentPlayerRepresentation();
        String playerSymbol = botSymbol.equals(" O ") ? " X " : " O ";

        int bestVal = Integer.MIN_VALUE;
        int[] bestMove = new int[]{-1, -1};

        for (int i = 0; i < game.size; i++) {
            for (int j = 0; j < game.size; j++) {
                if (game.board[i][j].isEmpty()) {
                    // simule le coup du bot
                    game.board[i][j].setRepresentation(botSymbol);

                    int moveVal = minimax(game.board, 0, false, botSymbol, playerSymbol);

                    // annule le coup
                    game.board[i][j].setRepresentation("   ");

                    if (moveVal > bestVal) {
                        bestMove[0] = i;
                        bestMove[1] = j;
                        bestVal = moveVal;
                    }
                }
            }
        }
        return bestMove;
    }

    /**
     * Évalue le plateau : renvoie +10 si le bot gagne, -10 si le joueur gagne, 0 sinon.
     * Ici, on ne vérifie que les lignes (horizontal).
     */
    private int evaluate(Cell[][] board, String botSymbol, String playerSymbol) {
        for (int i = 0; i < game.size; i++) {
            int consecutiveBot = 0;
            int consecutivePlayer = 0;

            for (int j = 0; j < game.size; j++) {
                String symbol = board[i][j].getRepresentation();

                if (symbol.equals(botSymbol)) {
                    consecutiveBot++;
                    consecutivePlayer = 0;
                } else if (symbol.equals(playerSymbol)) {
                    consecutivePlayer++;
                    consecutiveBot = 0;
                } else {
                    // Case vide : on remet à zéro les compteurs
                    consecutiveBot = 0;
                    consecutivePlayer = 0;
                }

                // Si le bot a une ligne complète
                if (consecutiveBot == game.victoryValue) {
                    return +10;
                }
                // Si le joueur a une ligne complète
                if (consecutivePlayer == game.victoryValue) {
                    return -10;
                }
            }
        }

        // Personne n'a gagné horizontalement
        return 0;
    }

    private boolean isMovesLeft(Cell[][] board) {
        for (int i = 0; i < game.size; i++) {
            for (int j = 0; j < game.size; j++) {
                if (board[i][j].isEmpty()) return true;
            }
        }
        return false;
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


     public void minimax() {
     String botSymbol = game.getCurrentPlayerRepresentation();
     String playerSymbol = botSymbol.equals(" O ") ? " X " : " O ";

     int score = evaluate(game.board, botSymbol, playerSymbol);
     System.out.println("Score : " + score);
     }

     /**
     * Évalue le plateau : renvoie +10 si le bot gagne, -10 si le joueur gagne, 0 sinon.
     * Ici, on ne vérifie que les lignes (horizontal).

     private int evaluate(Cell[][] board, String botSymbol, String playerSymbol) {
     return checkHorizontalMinimax(board, botSymbol, playerSymbol);
     }


     private Cell[][] cloneBoard(Cell[][] original) {
     Cell[][] copy = new Cell[original.length][original[0].length];
     for (int i = 0; i < original.length; i++) {
     for (int j = 0; j < original[0].length; j++) {
     Cell c = new Cell();
     c.setRepresentation(original[i][j].getRepresentation());
     copy[i][j] = c;
     }
     }
     return copy;
     }

     /**
     * Vérifie si un joueur a gagné horizontalement

     public int checkHorizontalMinimax(Cell[][] board, String botSymbol, String playerSymbol) {
     for (int i = 0; i < game.size; i++) {
     int consecutiveBot = 0;
     int consecutivePlayer = 0;

     for (int j = 0; j < game.size; j++) {
     String symbol = board[i][j].getRepresentation();

     if (symbol.equals(botSymbol)) {
     consecutiveBot++;
     consecutivePlayer = 0;
     } else if (symbol.equals(playerSymbol)) {
     consecutivePlayer++;
     consecutiveBot = 0;
     } else {
     // Case vide : on remet à zéro les compteurs
     consecutiveBot = 0;
     consecutivePlayer = 0;
     }

     // Si le bot a une ligne complète
     if (consecutiveBot == game.victoryValue) {
     return +10;
     }
     // Si le joueur a une ligne complète
     if (consecutivePlayer == game.victoryValue) {
     return -10;
     }
     }
     }
     return 0;
     }

     /**
     * Vérifie si une cellule est vide (true : case libre, false : plateau plein)
     *
     * @return

    private boolean isMovesLeftMinimax() {
    for (int i = 0; i < game.size; i++) {
    for (int j = 0; j < game.size; j++) {
    if (game.board[i][j].isEmpty()) return true;
    }
    }
    return false;
    }


     */


}
