package fr.raphaelmakaryan.bibliotheque.controllers;

import fr.raphaelmakaryan.bibliotheque.modeles.GameModele;

public abstract class GameController {
    protected GameModele game;

    public GameController(GameModele game) {
        this.game = game;
    }

    public abstract void play();

    public abstract void display();

    public abstract void getMoveFromPlayer(String choice);

    public abstract void randomPlayer();

    public abstract void nextPlayer();

    public abstract void isOver();

    public abstract void createPlayer(int[] value);
}