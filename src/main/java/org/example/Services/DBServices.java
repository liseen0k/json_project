package org.example.Services;

import org.example.Models.Currencies;
import org.example.Models.Items;
import org.example.Models.Player;
import org.example.Models.Progresses;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBServices {

    Connection conn;
    String URL;
    String USERNAME;
    String PASSWORD;

    public DBServices(Connection conn, String URL, String USERNAME, String PASSWORD) {
        this.conn = conn;
        this.URL = URL;
        this.USERNAME = USERNAME;
        this.PASSWORD = PASSWORD;
    }

    public void insert(Statement statement, String tableName, Player player) throws SQLException {
            statement.executeUpdate(String.format("INSERT INTO public." + "\"" + tableName + "\"" + "\n" +
                    " VALUES (%d, '%s');", player.getPlayerId(), player.getNickname()));
    }

    public void insert(Statement statement, String tableName, Progresses progresses) throws SQLException {
        statement.executeUpdate(String.format("INSERT INTO public." + "\"" + tableName + "\"" + "\n" +
                " VALUES (%d, %d, %d, %d, %d);", progresses.getId(), progresses.getPlayerId(), progresses.getResourceId(), progresses.getScore(), progresses.getMaxScore()));
    }

    public void insert(Statement statement, String tableName, Currencies currencies) throws SQLException {
        statement.executeUpdate(String.format("INSERT INTO public." + "\"" + tableName + "\"" + "\n" +
                " VALUES (%d, %d, %d, '%s', %d);", currencies.getId(), currencies.getPlayerId(), currencies.getResourceId(), currencies.getName(), currencies.getCount()));
    }

    public void insert(Statement statement, String tableName, Items items) throws SQLException {
        statement.executeUpdate(String.format("INSERT INTO public." + "\"" + tableName + "\"" + "\n" +
                " VALUES (%d, %d, %d, %d, %d);", items.getId(), items.getPlayerId(), items.getResourceId(), items.getCount(), items.getLevel()));
    }

    public void toDataBase(List<Player> playerList) throws SQLException {
        try {
            System.out.println("Устанавливается соединение с базой данных...");
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Соединение установлено");
            Statement statement = conn.createStatement();
            System.out.println("Идет загрузка данных в базу...");
            for (Player pl : playerList) {
                insert(statement, "Player", pl);

                for (Progresses prog : pl.getProgresses()) {
                    insert(statement, "Progresses", prog);
                }
                for (Currencies cur : pl.getCurrencies()) {
                    insert(statement, "Currencies", cur);
                }
                for (Items item : pl.getItems()) {
                    insert(statement, "Items", item);
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

    public List<Player> fromDataBase() throws SQLException {
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
}
