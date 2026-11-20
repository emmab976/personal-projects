package bookstoresearch;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Runs every "Check" query in sql/data-checks.sql against the test database.
 * Each check returns only rows that break a rule, so any row returned is a failure.
 */
class DataChecksTest {

    private static final Path CHECKS_FILE = Path.of("sql", "data-checks.sql");

    @BeforeAll
    static void loadSampleData() throws IOException, SQLException {
        TestDatabase.reset();
    }

    /**
     * Read data-checks.sql and pull out each "-- Check N: description" query.
     * "Info" queries are skipped, since they are expected to return rows.
     */
    static List<Arguments> checks() throws IOException {
        List<Arguments> checks = new ArrayList<>();
        String name = null;
        StringBuilder sql = new StringBuilder();

        for (String line : Files.readAllLines(CHECKS_FILE)) {
            if (line.startsWith("-- Check ")) {
                name = line.substring(3);
                sql.setLength(0);
            }
            else if (line.startsWith("-- Info ")) {
                name = null;
            }
            else if (name != null && !line.startsWith("--") && !line.isBlank()) {
                String trimmed = line.trim();
                if (trimmed.endsWith(";")) {
                    sql.append(trimmed, 0, trimmed.length() - 1);
                    checks.add(Arguments.of(Named.of(name, sql.toString())));
                    name = null;
                }
                else {
                    sql.append(line).append('\n');
                }
            }
        }
        return checks;
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("checks")
    void checkFindsNoProblemRows(String sql) throws SQLException {
        List<String> problemRows = new ArrayList<>();

        try (Connection connection = HenryDAO.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            int columns = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                List<String> values = new ArrayList<>();
                for (int i = 1; i <= columns; i++) {
                    values.add(rs.getString(i));
                }
                problemRows.add(String.join(", ", values));
            }
        }

        assertEquals(List.of(), problemRows, "Rows that break this rule");
    }

    @Test
    @DisplayName("All 11 checks in data-checks.sql are picked up")
    void everyCheckIsFound() throws IOException {
        assertEquals(11, checks().size());
    }
}
