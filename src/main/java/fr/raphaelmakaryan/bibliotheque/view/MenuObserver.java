package fr.raphaelmakaryan.bibliotheque.view;

import fr.raphaelmakaryan.bibliotheque.configurations.InteractionUtilisateur;

public interface MenuObserver {
    void onLeaveGame(InteractionUtilisateur interactionUtilisateur, String message);
}