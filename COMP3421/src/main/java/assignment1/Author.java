package assignment1;

import java.util.Vector;

public class Author {
	private String name;
	private String id;
	
	public Author(String name, String id) {
		this.name = name;
		this.id = id;
	}
	
	public String getID() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
