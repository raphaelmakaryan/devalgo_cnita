package fr.raphaelmakaryan.bibliotheque.pertinent;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;

import java.time.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mongodb.ServerApi;
import com.mongodb.client.*;
import com.mongodb.ServerApiVersion;
import org.bson.types.ObjectId;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

import javax.swing.*;


public class GameSerialization implements Persistence {

    public MongoDatabase dbConnection() {
        ServerApi serverApi = ServerApi.builder().version(ServerApiVersion.V1).build();
        MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(new ConnectionString(connectionString)).serverApi(serverApi).build();
        MongoClient mongoClient = MongoClients.create(settings);
        return mongoClient.getDatabase("devalgo");
    }

    public MongoCollection<Document> dbReturnCollectionGames(MongoDatabase database) {
        return database.getCollection("games");
    }

    public MongoCollection<Document> dbReturnCollectionUsers(MongoDatabase database) {
        return database.getCollection("users");
    }

    public String dbCreateUser(MongoDatabase database, String type, String place, String symbole) {
        MongoCollection<Document> collection = database.getCollection("users");
        InsertOneResult result = collection.insertOne(new Document("dateCreation", LocalDateTime.now()).append("type", type).append("place", place).append("representation", symbole).append("score", 0));
        return result.getInsertedId().asObjectId().getValue().toString();
    }

    public String dbCreateGame(MongoDatabase database, String mode, String game, int taille, int jetonVictoire, String player1, String player2) {
        MongoCollection<Document> collection = dbReturnCollectionGames(database);
        Map<String, Map<String, String>> board = new HashMap<>();
        for (int i = 0; i < taille; i++) {
            Map<String, String> row = new HashMap<>();
            for (int j = 0; j < taille; j++) {
                row.put(String.valueOf(j), "");
            }
            board.put(String.valueOf(i), row);
        }
        InsertOneResult result = collection.insertOne(new Document("dateCreation", LocalDateTime.now()).append("gameChoose", game).append("mod", mode).append("size", taille).append("victoryValue", jetonVictoire).append("turn", "UNDEFINED").append("player1", new ObjectId(player1)).append("player2", new ObjectId(player2)).append("board", board));
        return result.getInsertedId().asObjectId().getValue().toString();
    }

    public void dbUpdateUser() {

    }

    public void dbUpdateGame(MongoDatabase database, String playerId, String gameId, String symbole, int colonne, int row) {
        MongoCollection<Document> collection = dbReturnCollectionGames(database);
        Document filter = new Document("_id", new ObjectId(gameId));
        Document game = collection.find(filter).first();
        if (game != null) {
            Document board = (Document) game.get("board");
            Document rowDoc = (Document) board.get(String.valueOf(row));
            if (rowDoc == null) {
                rowDoc = new Document();
                board.put(String.valueOf(row), rowDoc);
            }
            rowDoc.put(String.valueOf(colonne), symbole);
            collection.replaceOne(filter, game);
        } else {
            System.out.println("Aucun document trouvé avec l'ID " + gameId);
        }
    }

    public void dbUpdateGameTurnPlayer(MongoDatabase database, String gameId, String playerTurn) {
        MongoCollection<Document> collection = dbReturnCollectionGames(database);
        Document filter = new Document("_id", new ObjectId(gameId));
        Document game = collection.find(filter).first();
        if (game != null) {
            game.put("turn", playerTurn);
            collection.replaceOne(filter, game);
        } else {
            System.out.println("Aucun document trouvé avec l'ID " + gameId);
        }
    }

    public String dbGetGame(MongoDatabase database) {
        MongoCollection<Document> collection = dbReturnCollectionGames(database);
        MongoCursor<Document> cursor;
        LocalDateTime today = LocalDateTime.now().with(LocalTime.MIN);
        Document filter = new Document("dateCreation", new Document("$gte", today));
        cursor = collection.find(filter).iterator();
        ObjectId[] listAllIdGame = new ObjectId[0];
        List<String> gameNames = new ArrayList<>();
        int value = 1;

        while (cursor.hasNext()) {
            Document document = cursor.next();
            Instant dateCreation = document.getDate("dateCreation").toInstant();
            int hourBdd = dateCreation.atZone(ZoneId.systemDefault()).getHour() - 2;
            int minuteBdd = dateCreation.atZone(ZoneId.systemDefault()).getMinute();
            int hourNow = LocalDateTime.now().getHour();
            int minuteNow = LocalDateTime.now().getMinute();

            if (hourBdd == hourNow && (minuteBdd - minuteNow) < 2) {
                String gameName = value + ". " + document.getString("mod") + " - " + document.getString("gameChoose");
                listAllIdGame = new ObjectId[]{document.getObjectId("_id")};
                gameNames.add(gameName);
                value++;
            }
        }

        Object[] options = gameNames.toArray();
        String selectedGame = (String) JOptionPane.showInputDialog(
                null,
                "Choisissez un jeu à lancer:",
                "Sélection de jeu",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options);


        if (selectedGame != null) {
            String[] parts = selectedGame.split("\\.\\s+");
            if (listAllIdGame.length == parts.length) {
                return listAllIdGame[Integer.parseInt(parts[0])].toString();
            } else {
                return listAllIdGame[Integer.parseInt(parts[0]) - 1].toString();
            }
        } else {
            System.out.println("Vous avez décidé de fermer la page, fermeture du jeu.");
            System.exit(0);
        }
        return "";
    }

    public String[] dbGetGameId(MongoDatabase database, String gameId) {
        MongoCollection<Document> collection = dbReturnCollectionGames(database);
        Document filter = new Document("_id", new ObjectId(gameId));
        Document game = collection.find(filter).first();
        String gameChoose;
        String mode;
        String turn;
        String size;
        String victoryValue;
        if (game != null) {
            gameChoose = game.getString("gameChoose");
            mode = game.getString("mode");
            turn = game.getString("turn");
            size = String.valueOf(game.getInteger("size"));
            victoryValue = String.valueOf(game.getInteger("victoryValue"));
            return new String[]{gameId, gameChoose, mode, turn, size, victoryValue};
        } else {
            System.out.println("Aucun document trouvé avec l'ID " + gameId);
        }
        return new String[]{};
    }

    public String[] dbReturnUsersGameId(MongoDatabase database, ObjectId userId) {
        MongoCollection<Document> collection = dbReturnCollectionUsers(database);
        Document filter = new Document("_id", userId);
        Document user = collection.find(filter).first();
        String id;
        String type;
        String place;
        String representation;
        if (user != null) {
            id = String.valueOf(user.getObjectId("_id"));
            type = user.getString("type");
            place = user.getString("place");
            representation = user.getString("representation");
            return new String[]{id, type, place, representation};
        } else {
            return new String[]{};
        }
    }

    public String[][] dbGetUsersGameId(MongoDatabase database, String gameId) {
        MongoCollection<Document> collection = dbReturnCollectionGames(database);
        Document filter = new Document("_id", new ObjectId(gameId));
        Document game = collection.find(filter).first();
        if (game != null) {
            String[] player1Info = dbReturnUsersGameId(database, game.getObjectId("player1"));
            String[] player2Info = dbReturnUsersGameId(database, game.getObjectId("player2"));
            return new String[][]{player1Info, player2Info};
        } else {
            return new String[][]{null, null};
        }
    }

    static Logger root = (Logger) LoggerFactory
            .getLogger(Logger.ROOT_LOGGER_NAME);

    static {
        root.setLevel(Level.INFO);
    }
}
