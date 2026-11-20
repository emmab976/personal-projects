package bookstoresearch;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Shared setup for the integration tests: rebuilds the test database from
 * sql/schema.sql and sql/seed.sql so every run starts from the same known data.
 */
final class TestDatabase {

    private TestDatabase() {
    }

    static void reset() throws IOException, SQLException {
        String configFile = System.getProperty("db.config", "db.properties");
        Path config = Path.of(configFile);
        assertTrue(Files.exists(config), configFile + " not found. Copy test-db.properties.example to "
                + "test-db.properties and fill in your test database login (see README).");

        // Safety check: reset() drops and recreates every table, so never let it touch a real database
        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(config)) {
            props.load(in);
        }
        String url = props.getProperty("db.url", "");
        assertTrue(url.contains("bookstore_test"),
                "Tests must run against the bookstore_test database, but " + configFile + " points at " + url);

        String schema = Files.readString(Path.of("sql", "schema.sql"));
        String seed = Files.readString(Path.of("sql", "seed.sql"));

        try (Connection connection = HenryDAO.getConnection();
             Statement stmt = connection.createStatement()) {
            runScript(stmt, schema);
            runScript(stmt, seed);
        }
    }

    /**
     * Run a whole .sql file in one call (needs allowMultiQueries=true in the test URL)
     * and read through every result so an error in any statement is reported.
     */
    private static void runScript(Statement stmt, String sql) throws SQLException {
        boolean isResultSet = stmt.execute(sql);
        while (isResultSet || stmt.getUpdateCount() != -1) {
            isResultSet = stmt.getMoreResults();
        }
    }
}
