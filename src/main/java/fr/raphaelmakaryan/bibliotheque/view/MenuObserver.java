package fr.raphaelmakaryan.bibliotheque.view;

import fr.raphaelmakaryan.bibliotheque.configurations.InteractionUtilisateur;
import fr.raphaelmakaryan.bibliotheque.modeles.GameModeleInterface;

public interface MenuObserver {
    void onLeaveGame(InteractionUtilisateur interactionUtilisateur, String message);
}