package fr.raphaelmakaryan.bibliotheque;

import fr.raphaelmakaryan.bibliotheque.games.Game;

public class Main {
    public static void main(String[] args) {
        Game game = new Game(0, 0);
        game.chooseGame();
    }
}
