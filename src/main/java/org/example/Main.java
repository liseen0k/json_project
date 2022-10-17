package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Models.Currencies;
import org.example.Models.Items;
import org.example.Models.Player;
import org.example.Models.Progresses;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static Connection conn = null;
    private static final String URL = "jdbc:postgresql://localhost:5432/gameStat";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "liseen0k";

    public static void toDataBase(List<Player> playerList) throws SQLException {
        try {
            System.out.println("Устанавливается соединение с базой данных...");
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Соединение установлено");
            Statement statement = conn.createStatement();
            System.out.println("Идет загрузка данных в базу...");
                for (Player pl : playerList) {
                    statement.executeUpdate(String.format("INSERT INTO Player (id, nickname)" +
                            " VALUES (%d, '%s');", pl.getPlayerId(), pl.getNickname()));

                    for (Progresses prog : pl.getProgresses()) {
                        statement.executeUpdate(String.format("INSERT INTO Progresses (id, playerId, resourceId, score, maxScore)" +
                                " VALUES (%d, %d, %d, %d, %d);", prog.getId(), prog.getPlayerId(), prog.getResourceId(), prog.getScore(), prog.getMaxScore()));
                    }
                    for (Currencies cur : pl.getCurrencies()) {
                        statement.executeUpdate(String.format("INSERT INTO Currencies (id, playerId, resourceId, name, count)" +
                                " VALUES (%d, %d, %d, '%s', %d);", cur.getId(), cur.getPlayerId(), cur.getResourceId(), cur.getName(), cur.getCount()));
                    }
                    for (Items item : pl.getItems()) {
                        statement.executeUpdate(String.format("INSERT INTO Items (id, playerId, resourceId, count, level)" +
                                " VALUES (%d, %d, %d, %d, %d);", item.getId(), item.getPlayerId(), item.getResourceId(), item.getCount(), item.getLevel()));
                    }
                }
            System.out.println("Данные загружены");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        finally {
            System.out.println("Разрыв соединения с базой...");
            conn.close();
            System.out.println("Соединение разорвано");
        }
    }

    public static List<Player> fromDataBase() throws SQLException {
        List<Player> playerList = new ArrayList<>();
        try {
            System.out.println("Устанавливается соединение с базой данных...");
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Соединение установлено");
            Statement statement = conn.createStatement();
            System.out.println("Идет выгрузка данных из базы...");
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Player");

            while(resultSet.next()) {
                Player player = new Player();
                int playerId = resultSet.getInt("id");
                player.setPlayerId(playerId);
                System.out.println("pl" + playerId);
                player.setNickname(resultSet.getString("nickname"));

                ResultSet progressResult = statement.executeQuery(
                        String.format("SELECT * FROM Progresses WHERE playerId=%d", playerId));
                while (progressResult.next()) {
                    player.addProgress(new Progresses(progressResult.getInt("id"),
                            progressResult.getInt("playerId"),
                            progressResult.getInt("resourceId"),
                            progressResult.getInt("score"),
                            progressResult.getInt("maxScore")));
                }

                ResultSet currencyResult = statement.executeQuery(
                        String.format("SELECT * FROM Currencies WHERE playerId=%d", playerId));
                while (currencyResult.next()) {
                    player.addCurrency(new Currencies(currencyResult.getInt("id"),
                            currencyResult.getInt("playerId"),
                            currencyResult.getInt("resourceId"),
                            currencyResult.getString("name"),
                            currencyResult.getInt("count")));
                }

                ResultSet itemResult = statement.executeQuery(
                        String.format("SELECT * FROM Items WHERE playerId=%d", playerId));
                while (itemResult.next()) {
                    player.addItem(new Items(itemResult.getInt("id"),
                            itemResult.getInt("playerId"),
                            itemResult.getInt("resourceId"),
                            itemResult.getInt("count"),
                            itemResult.getInt("level")));
                }

                playerList.add(player);
            }
            System.out.println("Выгрузка данных из базы завершена");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        finally {
            System.out.println("Разрыв соединения с базой...");
            conn.close();
            System.out.println("Соединение разорвано");
        }
        return playerList;
    }


    public static void main(String[] args) throws IOException, SQLException {
        ObjectMapper mapper = new ObjectMapper();
        byte[] jsonData = Files.readAllBytes(Paths.get("players.json"));

        List<Player> players = mapper.readValue(jsonData, new TypeReference<List<Player>>(){});


        System.out.println(players.size());
        toDataBase(players);
        List<Player> players1 = fromDataBase();
        System.out.println(players1.size());

    }
}
