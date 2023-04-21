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
    public static void main( String[] args )
    {
        
    }

    private static void initDatabaseConnectionPool() {
        HikariConfig hikariConfig = new HikariConfig("/database.properties");
        dataSource = new HikariDataSource(hikariConfig);;
    }

    private static void closeDatabaseConnectionPool() {
        dataSource.close();
    }

    private static void createData(String nombre, int numero) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                        INSERT INTO tipos(nombre, numero)
                        VALUES (?, ?)
                    """)) {
                statement.setString(1, nombre);
                statement.setInt(2, numero);
                int rowsInserted = statement.executeUpdate();
                System.out.println("Rows inserted: " + rowsInserted);
            }
        }
    }
}
