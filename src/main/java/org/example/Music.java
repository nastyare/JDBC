package org.example;
import java.sql.*;

public class Music {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "m2xK4tP2005";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {

            // список песенок
            String selectQuery1 = "SELECT * FROM public.music";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs1 = stmt.executeQuery(selectQuery1)) {
                System.out.println("Music list:");
                while (rs1.next()) {
                    String name = rs1.getString("name");
                    System.out.println(name);
                }
            }

            // м и т должны отсутствовать
            String selectQuery2 = "SELECT * FROM public.music WHERE name NOT ILIKE '%m%' AND name NOT ILIKE '%t%'";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs2 = stmt.executeQuery(selectQuery2)) {
                System.out.println("\nSongs without m and t");
                while (rs2.next()) {
                    String name = rs2.getString("name");
                    System.out.println(name);
                }
            }

            // 3. добавить песенку
            String insertQuery = "INSERT INTO public.music (id, name) VALUES (23, 'S-class')";
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(insertQuery);
                System.out.println("\nA new song has been successfully inserted.");
            }


            // 4. вывод нового списка
            try (Statement stmt = conn.createStatement();
                 ResultSet rs3 = stmt.executeQuery(selectQuery1)) {
                System.out.println("\nNew music list:");
                while (rs3.next()) {
                    String name = rs3.getString("name");
                    System.out.println(name);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
