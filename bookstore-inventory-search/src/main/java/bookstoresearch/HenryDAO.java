package bookstoresearch;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.Properties;
import java.util.Vector;

public class HenryDAO {
    private static final String CONFIG_FILE = "db.properties";
    private static Properties dbConfig;

    /**
     * Load the database settings from db.properties (only reads the file once)
     * @return Properties containing db.url, db.user and db.password
     */
    private static Properties getConfig()
    {
      if (dbConfig == null) {
          dbConfig = new Properties();
          try (InputStream in = new FileInputStream(CONFIG_FILE)) {
              dbConfig.load(in);
          } catch (IOException ex) {
              System.err.println(CONFIG_FILE + " not found. Copy db.properties.example to "
                      + CONFIG_FILE + " and fill in your database login (see README setup).");
              System.exit(1);
          }
      }
      return dbConfig;
    }

    /**
     * Get a connection to database
     * @return Connection object
     */
    public static Connection getConnection()
    {
      Properties config = getConfig();
      try {
          return DriverManager.getConnection(config.getProperty("db.url"),
                  config.getProperty("db.user"), config.getProperty("db.password"));
      } catch (SQLException ex) {
          throw new RuntimeException("Error connecting to the database", ex);
      }
    }
    
    public static Vector<Author> getAuthor() {
        Vector<Author> authors = new Vector<Author>();
        Author auth;
        String sql = "SELECT * FROM henry_author";
        
        try (Connection connection = HenryDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while(rs.next())
            {
                String ID = ( rs.getString("author_num"));
                String nameFirst = ( rs.getString("author_first") );
                String nameLast = ( rs.getString("author_last") );
                String fullName = nameFirst + " " + nameLast;
                auth = new Author(fullName, ID);
                authors.add(auth);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return authors;
    }
    
    public static Vector<Book> getBooksByAuthor(String author_num){
        Vector<Book> books = new Vector<Book>();
        Book book;
        String sql = "SELECT DISTINCT(title), henry_book.book_code, type, publisher_code, price, paperback FROM henry_book JOIN henry_inventory ON henry_book.book_code = henry_inventory.book_code JOIN henry_wrote ON henry_inventory.book_code = henry_wrote.book_code WHERE author_num = ?";
    	
        try (Connection connection = HenryDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, author_num);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while(rs.next())
                {
                    String ID = ( rs.getString("book_code"));
                    String title = ( rs.getString("title") );
                    String genre = ( rs.getString("type") );
                    String publisher = (rs.getString("publisher_code"));
                    String price = (rs.getString("price"));
                    String paperback = (rs.getString("paperback"));
                    
                    book = new Book(title, ID, genre, price, publisher, paperback);
                    books.add(book);
                }
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return books;
    }
    
    public static Vector<Branch> getInventory(String book_code){
        Vector<Branch> inventory = new Vector<Branch>();
        Branch branch;
        String sql = "SELECT branch_name, on_hand FROM henry_inventory JOIN henry_branch ON henry_inventory.branch_num = henry_branch.branch_num WHERE book_code = ?";
    	
        try (Connection connection = HenryDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, book_code);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while(rs.next())
                {
                    String name = ( rs.getString("branch_name") );
                    int onHand = ( rs.getInt("on_hand") );
                    
                    branch = new Branch(name, onHand);
                    inventory.add(branch);
                }
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return inventory;
    }
    
    public static Vector<Publisher> getPublisher(){
        Vector<Publisher> publishers = new Vector<Publisher>();
        Publisher publisher;
        String sql = "SELECT * FROM henry_publisher WHERE publisher_code IN (SELECT publisher_code FROM henry_book)";
    	
        try (Connection connection = HenryDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while(rs.next())
            {
            	String code = ( rs.getString("publisher_code"));
                String name = ( rs.getString("publisher_name") );
                String city = ( rs.getString("city") );
                
                publisher = new Publisher(code, name, city);
                publishers.add(publisher);
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return publishers;
    }
    
    public static Vector<Book> getBooksByPublisher(String publisher_code){
        Vector<Book> books = new Vector<Book>();
        Book book;
        String sql = "SELECT DISTINCT(title), henry_book.book_code, type, henry_book.publisher_code, price, paperback FROM henry_book JOIN henry_publisher ON henry_book.publisher_code = henry_publisher.publisher_code WHERE henry_book.book_code IN (SELECT book_code FROM henry_inventory) AND henry_book.publisher_code = ?";
    	
        try (Connection connection = HenryDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, publisher_code);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while(rs.next())
                {
                    String ID = ( rs.getString("book_code"));
                    String title = ( rs.getString("title") );
                    String genre = ( rs.getString("type") );
                    String publisher = (rs.getString("henry_book.publisher_code"));
                    String price = (rs.getString("price"));
                    String paperback = (rs.getString("paperback"));
                    
                    book = new Book(title, ID, genre, price, publisher, paperback);
                    books.add(book);
                }
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return books;
    }
	
    public static Vector<String> getBookGenres(){
        Vector<String> genres = new Vector<String>();
        String sql = "SELECT DISTINCT(type) FROM henry_book";
    	
        try (Connection connection = HenryDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while(rs.next())
            {
               String genre = ( rs.getString("type") );
                
               genres.add(genre);
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return genres;
    }
    
    public static Vector<Book> getBooksByCategory(String genre){
        Vector<Book> books = new Vector<Book>();
    	Book book;
        String sql = "SELECT DISTINCT(title), henry_book.book_code, type, publisher_code, price, paperback FROM henry_book JOIN henry_inventory ON henry_book.book_code = henry_inventory.book_code WHERE type = ?";
    	
        try (Connection connection = HenryDAO.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, genre);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while(rs.next())
                {
                    String ID = ( rs.getString("book_code"));
                    String title = ( rs.getString("title") );
                    String type = ( rs.getString("type") );
                    String publisher = (rs.getString("henry_book.publisher_code"));
                    String price = (rs.getString("price"));
                    String paperback = (rs.getString("paperback"));
                    
                    book = new Book(title, ID, type, price, publisher, paperback);
                    books.add(book);
                }
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return books;
    }
}
