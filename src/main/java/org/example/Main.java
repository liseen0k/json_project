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
                    statement.executeUpdate(String.format("INSERT INTO public.\"Player\"" +
                            " VALUES (%d, '%s');", pl.getPlayerId(), pl.getNickname()));

                    for (Progresses prog : pl.getProgresses()) {
                        statement.executeUpdate(String.format("INSERT INTO public.\"Progresses\"" +
                                " VALUES (%d, %d, %d, %d, %d);", prog.getId(), prog.getPlayerId(), prog.getResourceId(), prog.getScore(), prog.getMaxScore()));
                    }
                    for (Currencies cur : pl.getCurrencies()) {
                        statement.executeUpdate(String.format("INSERT INTO public.\"Currencies\"" +
                                " VALUES (%d, %d, %d, '%s', %d);", cur.getId(), cur.getPlayerId(), cur.getResourceId(), cur.getName(), cur.getCount()));
                    }
                    for (Items item : pl.getItems()) {
                        statement.executeUpdate(String.format("INSERT INTO public.\"Items\"" +
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
            Statement statement = conn.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY,
                    ResultSet.HOLD_CURSORS_OVER_COMMIT
            );
            System.out.println("Идет выгрузка данных из базы...");
            ResultSet resultSet = statement.executeQuery("SELECT * FROM public.\"Player\"");

            while(resultSet.next()) {
                Player player = new Player();
                int playerId = resultSet.getInt("playerId");
                player.setPlayerId(playerId);
                player.setNickname(resultSet.getString("nickname"));

                Statement st2 = conn.createStatement(
                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY
                );
                ResultSet progressResult = st2.executeQuery(
                        String.format("SELECT * FROM public.\"Progresses\" WHERE \"Progresses\".\"playerId\"=%d", playerId));
                while (progressResult.next()) {
                    player.addProgress(new Progresses(progressResult.getInt("id"),
                            progressResult.getInt("playerId"),
                            progressResult.getInt("resourceId"),
                            progressResult.getInt("score"),
                            progressResult.getInt("maxScore")));
                }

                Statement st3 = conn.createStatement(
                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY
                );
                ResultSet currencyResult = st3.executeQuery(
                        String.format("SELECT * FROM public.\"Currencies\" WHERE \"Currencies\".\"playerId\"=%d", playerId));
                while (currencyResult.next()) {
                    player.addCurrency(new Currencies(currencyResult.getInt("id"),
                            currencyResult.getInt("playerId"),
                            currencyResult.getInt("resourceId"),
                            currencyResult.getString("name"),
                            currencyResult.getInt("count")));
                }

                Statement st4 = conn.createStatement(
                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY
                );
                ResultSet itemResult = st4.executeQuery(
                        String.format("SELECT * FROM public.\"Items\" WHERE \"Items\".\"playerId\"=%d", playerId));
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
