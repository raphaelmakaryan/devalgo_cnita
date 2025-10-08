package fr.raphaelmakaryan.bibliotheque.controllers;

import fr.raphaelmakaryan.bibliotheque.modeles.GameModele;

public class GomokuController extends GameController {

    public GomokuController(GameModele game) {
        super(game);
    }

    @Override
    public void play() {
        game.play();
    }

    @Override
    public void display() {
        game.display();
    }

    @Override
    public void getMoveFromPlayer(String choice) {
        // tu peux transformer le choix de l’utilisateur en coordonnées de grille
        // puis appeler game.setOwner(ligne, colonne, "player");
    }

    @Override
    public void randomPlayer() {
        game.randomPlayer();
    }

    @Override
    public void nextPlayer() {
        game.nextPlayer();
    }

    @Override
    public void isOver() {
        game.isOver();
    }

    @Override
    public void createPlayer(int[] value) {
        game.createPlayer(value);
    }
}