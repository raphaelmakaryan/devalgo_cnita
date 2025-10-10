package fr.raphaelmakaryan.bibliotheque.modeles;

import fr.raphaelmakaryan.bibliotheque.Tools;
import fr.raphaelmakaryan.bibliotheque.configurations.ArtificialPlayer;
import fr.raphaelmakaryan.bibliotheque.configurations.Cell;
import fr.raphaelmakaryan.bibliotheque.configurations.InteractionUtilisateur;
import fr.raphaelmakaryan.bibliotheque.configurations.Player;
import fr.raphaelmakaryan.bibliotheque.view.GameView;

public interface GameModeleInterface {
    Tools tools = new Tools();
    GameView gameView = new GameView();
    InteractionUtilisateur interactionUtilisateur = new InteractionUtilisateur();

    void chooseGame();

    int[] returnValueUser(String choice);

    boolean verificationChoiceUser(String choice);

    void setOwner(int ligne, int colonne, String type);

    boolean verificationHavePlayer(Cell[][] board, int[] valueUser);

    Player getPlayerPlayNow();

    ArtificialPlayer getBotPlayNow();

    boolean checkWin();

    boolean checkHorizontal();

    boolean checkVertical();

    int checkCellFilled();

    boolean checkDiagonal();

    String getCurrentPlayerRepresentation();

    String separationBoardGame();

    void createPlayer(int[] value);
}
