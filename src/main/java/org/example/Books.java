package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Books {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "m2xK4tP2005";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // сортировка по годам
            String selectBooksOrderedQuery = "SELECT * FROM public.books ORDER BY publishingyear ASC";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(selectBooksOrderedQuery)) {
                System.out.println("Books ordered by year:");
                while (resultSet.next()) {
                    System.out.println(resultSet.getString("name") +
                            ", " + resultSet.getInt("publishingyear"));
                }
            }

            // выпущено раньше 2000
            String selectBooksBefore2000Query = "SELECT * FROM public.books WHERE publishingyear < 2000";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(selectBooksBefore2000Query)) {
                System.out.println("\nBooks published before 2000:");
                while (resultSet.next()) {
                    System.out.println(resultSet.getString("name") +
                            ", " + resultSet.getInt("publishingyear"));
                }
            }

            // внести меня
            String insertVisitorQuery = "INSERT INTO public.visitors (id, name, surname, phone, subscribed) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertVisitorQuery)) {
                preparedStatement.setInt(1, 16);
                preparedStatement.setString(2, "Anastasia");
                preparedStatement.setString(3, "Reksius");
                preparedStatement.setString(4, "555-228-3218");
                preparedStatement.setBoolean(5, false);
                preparedStatement.executeUpdate();
                System.out.println("\nVisitor added.");
            }

            // внести любимую книжку
            String insertBookQuery = "INSERT INTO public.books (id, name, author, publishingYear, isbn, publisher) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertBookQuery)) {
                preparedStatement.setInt(1, 30);
                preparedStatement.setString(2, "The Maze Runner");
                preparedStatement.setString(3, "James Dashner");
                preparedStatement.setInt(4, 2009);
                preparedStatement.setString(5, "0816529418");
                preparedStatement.setString(6, "Delacorte Press");
                preparedStatement.executeUpdate();
                System.out.println("Book added.");
            }

            // связать меня со список любимых книжек
            String insertFavoritesQuery = "INSERT INTO public.favorites (visitor_id, book_id) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertFavoritesQuery)) {
                preparedStatement.setInt(1, 16);
                preparedStatement.setInt(2, 30);
                preparedStatement.executeUpdate();

                preparedStatement.setInt(2, 17);
                preparedStatement.executeUpdate();

                System.out.println("Favorites added.");
            }

            // вывести новый список посетителей
            String selectAllVisitorsQuery = "SELECT * FROM public.visitors";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(selectAllVisitorsQuery)) {
                System.out.println("\nAll Visitors:");
                while (resultSet.next()) {
                    System.out.println(resultSet.getString("name") +
                            ", " + resultSet.getString("surname") +
                            ", " + resultSet.getBoolean("subscribed"));
                }
            }

            // вывести меня с моим списком книжек
            String joinQuery = """
                    SELECT v.id AS visitor_id, v.name AS visitor_name, v.surname AS visitor_surname, 
                           v.phone AS visitor_phone, v.subscribed AS visitor_subscribed,
                           b.id AS book_id, b.name AS book_name, b.author AS book_author, 
                           b.publishingYear AS book_publishing_year, b.isbn AS book_isbn, b.publisher AS book_publisher
                    FROM public.favorites f
                    JOIN public.visitors v ON f.visitor_id = v.id
                    JOIN public.books b ON f.book_id = b.id
                    WHERE f.visitor_id = ?
                    """;
            try (PreparedStatement preparedStatement = connection.prepareStatement(joinQuery)) {
                preparedStatement.setInt(1, 16);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    System.out.println("\nNastya's Favorites:");
                    while (resultSet.next()) {
                        System.out.println(resultSet.getString("book_name") +
                                ", " + resultSet.getString("book_author"));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
