package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Models.*;
import org.example.Services.DBServices;

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



    public static void main(String[] args) throws IOException, SQLException {
        ObjectMapper mapper = new ObjectMapper();
        byte[] jsonData = Files.readAllBytes(Paths.get("players.json"));

        List<Player> players = mapper.readValue(jsonData, new TypeReference<List<Player>>(){});

        DBServices services = new DBServices(conn, URL, USERNAME, PASSWORD);


        System.out.println(players.size());
        //services.toDataBase(players);
        List<Player> players1 = services.fromDataBase();
        System.out.println(players1.size());

    }
}
