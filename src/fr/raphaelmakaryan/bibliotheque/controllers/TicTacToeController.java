package fr.raphaelmakaryan.bibliotheque.controllers;

import fr.raphaelmakaryan.bibliotheque.configurations.TicTacToeState;
import fr.raphaelmakaryan.bibliotheque.configurations.TicTacToeStateVisitor;
import fr.raphaelmakaryan.bibliotheque.modeles.GameModele;
import fr.raphaelmakaryan.bibliotheque.modeles.TicTacToe;

public class TicTacToeController extends GameController implements TicTacToeStateVisitor {
    private final TicTacToe model;
    private final TicTacToeStateVisitor visitor;

    public TicTacToeController(GameModele game, TicTacToe model, TicTacToeStateVisitor visitor) {
        super(game);
        this.model = model;
        this.visitor = visitor;
    }

    @Override
    public void visit(TicTacToeState state) {
        switch (state) {
            case INITIAL:
                System.out.println("Tour suivant");
                break;
            case TOUR_JOUEUR:
                System.out.println("Action !");
                break;
            case VERIFICATION_GAGNANT:
                System.out.println("Vérification !");
                break;
            case MATCH_NUL:
                System.out.println("Match nul !");
                break;
            case FIN_PARTIE:
                System.out.println("Fin de la partie !");
                break;
        }
    }

    public void handleEvent(String event) {
        model.accept(visitor);
    }
}