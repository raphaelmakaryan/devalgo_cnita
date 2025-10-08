package fr.raphaelmakaryan.bibliotheque.controllers;

public enum GameState {
    INITIAL,
    ATTENTE_DEBUT,
    TOUR_JOUEUR,
    ATTENTE_TOUR_SUIVANT,
    VERIFICATION_FIN_PARTIE,
    FIN_PARTIE
}