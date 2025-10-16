package fr.raphaelmakaryan.bibliotheque.view;

import fr.raphaelmakaryan.bibliotheque.modeles.InteractionUtilisateur;

public interface MenuObserver {
    void onLeaveGame(InteractionUtilisateur interactionUtilisateur, String message);
}