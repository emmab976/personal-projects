package assignment1;

public class Book {
	private String title;
	private String bookNum;
	private String genre;
	private String price;
	private String paperback;
	private String publisher;
	
	public Book(String title, String bookNum, String genre, String price, String publisher, String paperback) {
		this.title = title;
		this.bookNum = bookNum;
		this.genre = genre;
		this.price = price;
		this.paperback = paperback;
	}
	
	
	public String getTitle() {
		return title;
	}
	
	public String getBookNum() {
		return bookNum;
	}
	
	public String getGenre() {
		return genre;
	}
	
	public String getPrice() {
		return price;
	}
	
	@Override
	public String toString() {
		return title;
	}
	
	
	
}
