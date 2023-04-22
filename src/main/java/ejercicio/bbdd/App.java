package ejercicio.bbdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Hello world!
 *
 */
public class App 
{

    private static HikariDataSource dataSource;
    public static void main( String[] args ) throws SQLException
    {

        try{

            initDatabaseConnectionPool();
            createData("Java", 2);
            createData("Python", 4);
            readData();
            updateData("Java", 9);
            readData();
            deleteData("Python");
            readData();

        } finally {

            closeDatabaseConnectionPool(); 

        }
        
    }

    private static void initDatabaseConnectionPool() {
        dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mariadb://localhost:3306/jdbc_demo");
        dataSource.setUsername("root");
        dataSource.setPassword("20Fran04.");
    }

    private static void closeDatabaseConnectionPool() {
        dataSource.close();
    }

    private static void createData(String nombre, int numero) throws SQLException {
        System.out.println("Creating Data...");
        int rowsInserted;
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                        INSERT INTO tipos(nombre, numero)
                        VALUES (?, ?)
                    """)) {
                statement.setString(1, nombre);
                statement.setInt(2, numero);
                rowsInserted = statement.executeUpdate();
            }

        }
        System.out.println("Rows inserted: " + rowsInserted);
    }

    private static void readData() throws SQLException {
        System.out.println("Reading data...");
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                        SELECT nombre, numero
                        FROM tipos
                        ORDER BY numero DESC
                    """)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    boolean empty = true;
                    while (resultSet.next()) {
                        empty = false;
                        String nombre = resultSet.getString("nombre");
                        int numero = resultSet.getInt("numero");
                        System.out.println("\t> " + nombre + ": " + numero);
                    }
                    if (empty) {
                        System.out.println("\t (no data)");
                    }
                }
            }
        }
    }
    

    private static void updateData(String nombre, int numero) throws SQLException {
        System.out.print("Updating data...");
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                        UPDATE tipos
                        SET numero = ?
                        WHERE nombre = ?
                    """)) {
                statement.setInt(1, numero);
                statement.setString(2, nombre);
                int rowsUpdated = statement.executeUpdate();
                System.out.println("\nRows updated: " + rowsUpdated);
            }
        }
    }

    private static void deleteData(String nombre) throws SQLException {
        System.out.print("Deleting data...");
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                        DELETE FROM tipos
                        WHERE nombre LIKE ?
                    """)) {
                statement.setString(1, nombre);
                int rowsDeleted = statement.executeUpdate();
                System.out.println("Rows deleted: " + rowsDeleted);
            }
        }
    }
}
