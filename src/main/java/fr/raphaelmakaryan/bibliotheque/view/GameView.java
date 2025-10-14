package fr.raphaelmakaryan.bibliotheque.view;

import java.util.ArrayList;
import java.util.List;

public class GameView implements MenuObserver {
    public List<MenuObserver> observers = new ArrayList<>();

    /**
     * Fonction d'affichage dans la console à la suite
     *
     * @param message Message a envoyé
     */
    public void print(String message) {
        System.out.print(message);
    }

    /**
     * Fonction d'affichage dans la console à la ligne
     *
     * @param message Message a envoyé
     */
    public void println(String message) {
        System.out.println(message);
    }

    @Override
    public void onLeaveGame(String message) {
        System.out.println(message);
    }
}
