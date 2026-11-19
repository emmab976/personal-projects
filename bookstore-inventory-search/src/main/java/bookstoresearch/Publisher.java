package bookstoresearch;

public class Publisher {
	private String code;
	private String name;
	private String city;
	
	public Publisher(String code, String name, String city) {
		this.code = code;
		this.name = name;
		this.city = city;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getName() {
		return name;
	}
	
	public String getCity() {
		return city;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
