package fr.raphaelmakaryan.bibliotheque;

import fr.raphaelmakaryan.bibliotheque.games.Game;
import fr.raphaelmakaryan.bibliotheque.games.TicTacToe;

public class Main {
    public static void main(String[] args) {
        Game game = new Game(0);
        game.chooseGame();
    }
}
