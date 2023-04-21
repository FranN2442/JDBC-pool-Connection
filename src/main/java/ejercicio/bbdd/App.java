package ejercicio.bbdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
            createData("fran", 33);

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
}
