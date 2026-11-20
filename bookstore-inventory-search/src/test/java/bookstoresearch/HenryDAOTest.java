package bookstoresearch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Integration tests for HenryDAO, run against the bookstore_test database
 * loaded with the sample data in sql/seed.sql.
 */
class HenryDAOTest {

    @BeforeAll
    static void loadSampleData() throws IOException, SQLException {
        TestDatabase.reset();
    }

    private static List<String> titles(Vector<Book> books) {
        return books.stream().map(Book::getTitle).sorted().toList();
    }

    // ------------------------------------------------------------
    // Author search
    // ------------------------------------------------------------

    @Test
    @DisplayName("Author list includes every author")
    void getAuthorReturnsEveryAuthor() {
        Vector<Author> authors = HenryDAO.getAuthor();

        assertEquals(25, authors.size());
        assertTrue(authors.stream().anyMatch(a -> a.getName().equals("Toni Morrison")));
    }

    @Test
    @DisplayName("Author search returns all of that author's books in stock")
    void booksByAuthorReturnsTheirBooks() {
        // Author 1 is Toni Morrison
        assertEquals(List.of("Beloved", "Jazz", "Song of Solomon"), titles(HenryDAO.getBooksByAuthor("1")));
    }

    @Test
    @DisplayName("Author with no books returns an empty list (the app shows \"No books found\")")
    void booksByAuthorWithNoBooksIsEmpty() {
        // Author 21 is Riva Castelman, who has no books in the sample data
        assertTrue(HenryDAO.getBooksByAuthor("21").isEmpty());
    }

    // ------------------------------------------------------------
    // Publisher search
    // ------------------------------------------------------------

    @Test
    @DisplayName("Publisher list leaves out publishers with no books")
    void getPublisherListsOnlyPublishersWithBooks() {
        Vector<Publisher> publishers = HenryDAO.getPublisher();

        assertEquals(21, publishers.size());
        assertFalse(publishers.stream().anyMatch(p -> p.getName().equals("Arkham House")));
    }

    @Test
    @DisplayName("Publisher search finds Berkley Publishing's one book")
    void booksByPublisherFindsBerkleyBook() {
        assertEquals(List.of("Group: Six People in Search of a Life"), titles(HenryDAO.getBooksByPublisher("BP")));
    }

    // ------------------------------------------------------------
    // Category search
    // ------------------------------------------------------------

    @Test
    @DisplayName("Category list includes every book type")
    void getBookGenresReturnsEveryType() {
        Vector<String> genres = HenryDAO.getBookGenres();

        assertEquals(12, genres.size());
        assertTrue(genres.contains("HOR"));
    }

    @Test
    @DisplayName("Category search returns every book of that type")
    void booksByCategoryReturnsMatchingBooks() {
        assertEquals(List.of("Black House", "Dreamcatcher: A Novel", "Magic Terror"),
                titles(HenryDAO.getBooksByCategory("HOR")));
    }

    // ------------------------------------------------------------
    // Inventory and book details
    // ------------------------------------------------------------

    @Test
    @DisplayName("Book codes with letters (138X) load as text (regression for the getInt bug)")
    void bookCodesWithLettersLoadAsText() {
        Book beloved = HenryDAO.getBooksByAuthor("1").stream()
                .filter(b -> b.getTitle().equals("Beloved"))
                .findFirst()
                .orElseThrow();

        assertEquals("138X", beloved.getBookNum());
        assertEquals("12.95", beloved.getPrice());

        Vector<Branch> inventory = HenryDAO.getInventory("138X");
        assertEquals(1, inventory.size());
        assertEquals("Henry On The Hill", inventory.get(0).getName());
        assertEquals(3, inventory.get(0).getQuantity());
    }

    @Test
    @DisplayName("Inventory lists every branch that stocks a book")
    void inventoryListsEveryBranch() {
        // The Grapes of Wrath is stocked at all four branches
        Vector<Branch> inventory = HenryDAO.getInventory("9701");

        assertEquals(4, inventory.size());
        assertEquals(8, inventory.stream().mapToInt(Branch::getQuantity).sum());
    }

    // ------------------------------------------------------------
    // SQL injection (regression for the PreparedStatement change)
    // ------------------------------------------------------------

    @Test
    @DisplayName("Publisher search treats an injection attempt as plain text")
    void publisherSearchIsNotInjectable() {
        // With the old string-built query this returned every book in stock
        assertTrue(HenryDAO.getBooksByPublisher("X' OR '1'='1").isEmpty());
    }

    @Test
    @DisplayName("Category search treats an injection attempt as plain text")
    void categorySearchIsNotInjectable() {
        assertTrue(HenryDAO.getBooksByCategory("FIC' OR '1'='1").isEmpty());
    }
}
