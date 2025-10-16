package fr.raphaelmakaryan.bibliotheque.pertinent;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import fr.raphaelmakaryan.bibliotheque.modeles.Cell;
import fr.raphaelmakaryan.bibliotheque.modeles.GameModele;
import fr.raphaelmakaryan.bibliotheque.modeles.GameModeleInterface;
import org.bson.Document;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;

import java.time.*;
import java.util.*;

import com.mongodb.ServerApi;
import com.mongodb.client.*;
import com.mongodb.ServerApiVersion;
import org.bson.types.ObjectId;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

import javax.swing.*;


public class GameSerialization implements Persistence {

    /**
     * Retourne une connection à la base de donnée
     *
     * @return Connexion de la base de donnée
     */
    public MongoDatabase dbConnection() {
        ServerApi serverApi = ServerApi.builder().version(ServerApiVersion.V1).build();
        MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(new ConnectionString(connectionString)).serverApi(serverApi).build();
        MongoClient mongoClient = MongoClients.create(settings);
        return mongoClient.getDatabase("devalgo");
    }

    /**
     * Retourne les documents de la table Games
     *
     * @param database Connexion a la base de données
     * @return Retourne la table
     */
    public MongoCollection<Document> dbReturnCollectionGames(MongoDatabase database) {
        return database.getCollection("games");
    }

    /**
     * Retourne les documents de la table Users
     *
     * @param database Connexion a la base de données
     * @return Retourne la table
     */
    public MongoCollection<Document> dbReturnCollectionUsers(MongoDatabase database) {
        return database.getCollection("users");
    }

    /**
     * Crée un user dans la base de données
     *
     * @param database Connexion a la base de donnée
     * @param type     Type d'user
     * @param place    Place dans le jeu (J/B)
     * @param symbole  Symbole
     * @return Retourne son id de la base de donnée
     */
    public String dbCreateUser(MongoDatabase database, String type, String place, String symbole) {
        MongoCollection<Document> collection = database.getCollection("users");
        InsertOneResult result = collection.insertOne(new Document("dateCreation", LocalDateTime.now()).append("type", type).append("place", place).append("representation", symbole).append("score", 0));
        return Objects.requireNonNull(result.getInsertedId()).asObjectId().getValue().toString();
    }

    /**
     * Crée une partie dans la base de données
     *
     * @param database      Connexion a la base de donnée
     * @param mode          Mode de jeu
     * @param game          Jeu choisi
     * @param taille        Taille
     * @param jetonVictoire Nombre de jetons pour la victoire
     * @param player1       Id du joueur 1
     * @param player2       Id du joueur 2
     * @return Retourne l'id de la partie
     */
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
        InsertOneResult result = collection.insertOne(new Document("dateCreation", LocalDateTime.now()).append("gameChoose", game).append("mod", mode).append("size", taille).append("victoryValue", jetonVictoire).append("turn", "UNDEFINED").append("state", "progress").append("player1", new ObjectId(player1)).append("player2", new ObjectId(player2)).append("board", board));
        return Objects.requireNonNull(result.getInsertedId()).asObjectId().getValue().toString();
    }

    /**
     * Met a jour le user car il a gagné
     *
     * @param database Connexion a la base de donnée
     * @param game     Récupere l'instance du jeu
     * @param mode     Mode du jeu
     */
    public void dbUpdateUser(MongoDatabase database, GameModele game, String mode) {
        if (!mode.equals("BvB")) {
            MongoCollection<Document> collection = dbReturnCollectionUsers(database);
            Document filter = new Document("_id", new ObjectId(game.getPlayerPlayNow().getIdDatabase()));
            Document user = collection.find(filter).first();
            if (user != null) {
                int score = user.getInteger("score") + 1;
                user.put("score", score);
                collection.replaceOne(filter, user);
            }
        }
    }

    /**
     * Met à jour la partie actuel pour le plateau
     *
     * @param database Connexion a la base de donnée
     * @param gameId   Id de la game de la base de donnée
     * @param symbole  Symbole du joueur qui a joué
     * @param colonne  Colonne où le joueur a joué
     * @param row      Ligne où le joueur a joué
     */
    public void dbUpdateGame(MongoDatabase database, String gameId, String symbole, int colonne, int row, String mode) {
        if (!mode.equals("BvB")) {
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
    }

    /**
     * Fonction qui met à jour l'état de la partie de jeu a la fin
     *
     * @param database Connexion a la base de donnée
     * @param gameId   Id de la partie
     */
    public void dbUpdateGameState(MongoDatabase database, String gameId, String mode) {
        if (!mode.equals("BvB")) {
            MongoCollection<Document> collection = dbReturnCollectionGames(database);
            Document filter = new Document("_id", new ObjectId(gameId));
            Document game = collection.find(filter).first();
            if (game != null) {
                game.put("state", "end");
                collection.replaceOne(filter, game);
            }
        }
    }

    /**
     * Met a jour le tour
     *
     * @param database   CConnexion a la base de donnée
     * @param gameId     Id de la partie
     * @param playerTurn Nouveau tour du joueur
     */
    public void dbUpdateGameTurnPlayer(MongoDatabase database, String gameId, String playerTurn, String mode) {
        if (!mode.equals("BvB")) {
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
    }

    /**
     * Affiche les parties dans la base de donnée pour le choix de charger une partie
     *
     * @param database Connexion de la base de donnée
     * @return Retourne l'id de la partie
     */
    public String dbGetGame(MongoDatabase database) {
        MongoCollection<Document> collection = dbReturnCollectionGames(database);
        MongoCursor<Document> cursor;
        LocalDateTime today = LocalDateTime.now().with(LocalTime.MIN);
        Document filter = new Document("dateCreation", new Document("$gte", today));
        cursor = collection.find(filter).iterator();
        ObjectId[] listAllIdGame = new ObjectId[Integer.parseInt(String.valueOf(collection.countDocuments()))];
        List<String> gameNames = new ArrayList<>();
        int value = 1;
        int valueOnList = 0;
        while (cursor.hasNext()) {
            Document document = cursor.next();
            Instant dateCreation = document.getDate("dateCreation").toInstant();
            String state = document.getString("state");
            int hourBdd = dateCreation.atZone(ZoneId.systemDefault()).getHour() - 2;
            int minuteBdd = dateCreation.atZone(ZoneId.systemDefault()).getMinute();
            int hourNow = LocalDateTime.now().getHour();
            int minuteNow = LocalDateTime.now().getMinute();

            if (hourBdd == hourNow && (minuteBdd - minuteNow) < 2 && state.equals("progress")) {
                String gameName = value + ". " + document.getString("mod") + " - " + document.getString("gameChoose");
                listAllIdGame[valueOnList] = document.getObjectId("_id");
                gameNames.add(gameName);
                value++;
                valueOnList++;
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
            if (listAllIdGame.length == 0) {
                return listAllIdGame[Integer.parseInt(parts[0])].toString();
            } else {
                return listAllIdGame[Integer.parseInt(parts[0]) - 1].toString();
            }
        } else {
            GameModeleInterface.interactionUtilisateur.inputMessage("Vous avez décidé de fermer la page, fermeture du jeu.");
            System.exit(0);
        }
        return "";
    }

    /**
     * Récupère les informations précises de la partie
     *
     * @param database Connexion de la base de données
     * @param gameId   Id de la partie
     * @return Retourne le tableau d'information
     */
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
            mode = game.getString("mod");
            turn = game.getString("turn");
            size = String.valueOf(game.getInteger("size"));
            victoryValue = String.valueOf(game.getInteger("victoryValue"));
            return new String[]{gameId, gameChoose, mode, turn, size, victoryValue};
        } else {
            System.out.println("Aucun document trouvé avec l'ID " + gameId);
        }
        return new String[]{};
    }

    /**
     * Récupère le tableau de la base de donnée pour le charger dans le jeu
     *
     * @param database CConnexion a la base de donnée
     * @param gameId   Id de la partie
     * @return Retourne le nouveau plateau
     */
    public Cell[][] dbGetBoardGameId(MongoDatabase database, String gameId) {
        MongoCollection<Document> collection = dbReturnCollectionGames(database);
        Document filter = new Document("_id", new ObjectId(gameId));
        Document game = collection.find(filter).first();
        int size = game.getInteger("size");
        int valueRow = 0;
        Cell[][] newBoard = new Cell[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                newBoard[i][j] = new Cell();
            }
        }
        Document boardData = (Document) game.get("board");
        while (valueRow != size) {
            for (int i = 0; i < size; i++) {
                Document row0 = (Document) boardData.get(String.valueOf(valueRow));
                String cellValue = row0.getString(String.valueOf(i));
                switch (cellValue) {
                    case " X ":
                        newBoard[valueRow][i].setRepresentation(" X ");
                        break;

                    case " O ":
                        newBoard[valueRow][i].setRepresentation(" O ");
                        break;
                }
            }
            valueRow++;
        }
        return newBoard;
    }

    /**
     * Retourne les informations des users selon les joueurs de la partie
     *
     * @param database Connexion a la base de donnée
     * @param userId   Id de l'user de la base de donnée
     * @return Retourne le tableau de ces informations
     */
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

    /**
     * Récupère via la base de donnée les deux joueurs pour récupérer leurs informations
     *
     * @param database Connexion a la base de donnée
     * @param gameId   Id de la partie
     * @return Retourne les infos des deux players
     */
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
