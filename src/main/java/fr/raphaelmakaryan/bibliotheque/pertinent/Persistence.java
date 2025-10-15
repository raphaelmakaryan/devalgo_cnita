package fr.raphaelmakaryan.bibliotheque.pertinent;

import com.mongodb.client.MongoDatabase;
import fr.raphaelmakaryan.bibliotheque.modeles.GameModele;
import io.github.cdimascio.dotenv.Dotenv;

public interface Persistence {
    Dotenv dotenv = Dotenv.configure().directory("src/main/java/fr/raphaelmakaryan/bibliotheque")
            .filename(".env")
            .load();
    String connectionString = dotenv.get("DATABASE_URL");

    MongoDatabase dbConnection();

    String dbCreateUser(MongoDatabase database, String type, String place, String symbole);

    String dbCreateGame(MongoDatabase database, String mode, String game, int taille, int jetonVictoire, String player1, String player2);

    void dbUpdateUser(MongoDatabase database, GameModele game, String mode);

    void dbUpdateGame(MongoDatabase database, String gameId, String symbole, int colonne, int row, String mode);

    void dbUpdateGameState(MongoDatabase database, String gameId, String mode);

    void dbUpdateGameTurnPlayer(MongoDatabase database, String gameId, String playerId, String mode);

    String dbGetGame(MongoDatabase database);

    String[] dbGetGameId(MongoDatabase database, String gameId);
}
