package org.example;
import java.sql.*;

public class Main {
    public static void main(String[] args) {
        // URL, имя пользователя и пароль для подключения к базе данных
        String url = "jdbc:postgresql://localhost:5432/postgres"; // Замените на свой URL
        String user = "postgres"; // Замените на свой логин
        String password = "m2xK4tP2005"; // Замените на свой пароль

        // Подключение к базе данных и выполнение запросов
        try (Connection conn = DriverManager.getConnection(url, user, password)) {

            // 1. SELECT * FROM public.music
            String selectQuery1 = "SELECT * FROM public.music";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs1 = stmt.executeQuery(selectQuery1)) {
                System.out.println("Results of SELECT * FROM public.music:");
                while (rs1.next()) {
                    int id = rs1.getInt("id");
                    String name = rs1.getString("name");
                    System.out.println("ID: " + id + ", Name: " + name);
                }
            }

            // 2. SELECT * FROM public.music WHERE name NOT ILIKE '%m%' AND name NOT ILIKE '%t%'
            String selectQuery2 = "SELECT * FROM public.music WHERE name NOT ILIKE '%m%' AND name NOT ILIKE '%t%'";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs2 = stmt.executeQuery(selectQuery2)) {
                System.out.println("\nResults of SELECT * WHERE name NOT ILIKE '%m%' AND name NOT ILIKE '%t%':");
                while (rs2.next()) {
                    int id = rs2.getInt("id");
                    String name = rs2.getString("name");
                    System.out.println("ID: " + id + ", Name: " + name);
                }
            }

            // 3. INSERT INTO public.music (id, name) VALUES (21, 'Come Alive')
            String insertQuery = "INSERT INTO public.music (id, name) VALUES (21, 'Come Alive')";
            try (Statement stmt = conn.createStatement()) {
                int rowsAffected = stmt.executeUpdate(insertQuery);
                System.out.println("\nNumber of rows inserted: " + rowsAffected);
            }

            // 4. SELECT * FROM public.music (to check the insertion)
            try (Statement stmt = conn.createStatement();
                 ResultSet rs3 = stmt.executeQuery(selectQuery1)) {
                System.out.println("\nResults of SELECT * FROM public.music (after insertion):");
                while (rs3.next()) {
                    int id = rs3.getInt("id");
                    String name = rs3.getString("name");
                    System.out.println("ID: " + id + ", Name: " + name);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
