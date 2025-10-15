package fr.raphaelmakaryan.bibliotheque.view;

import fr.raphaelmakaryan.bibliotheque.configurations.InteractionUtilisateur;
import fr.raphaelmakaryan.bibliotheque.modeles.GameModeleInterface;

import java.util.ArrayList;
import java.util.List;

public class GameView implements MenuObserver {
    public List<MenuObserver> observers = new ArrayList<>();

    @Override
    public void onLeaveGame(InteractionUtilisateur interactionUtilisateur, String message) {
        interactionUtilisateur.inputMessage(message);
    }
}
