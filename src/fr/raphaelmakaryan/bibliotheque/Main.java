package fr.raphaelmakaryan.bibliotheque;

import fr.raphaelmakaryan.bibliotheque.modeles.GameModele;

public class Main {
    public static void main(String[] args) {
        GameModele gameModele = new GameModele(0, 0);
        gameModele.chooseGame();
    }
}
