package matching;
import java.util.*;

public class Big extends Member{

	public HashMap <Little, Integer> rankings = new HashMap<>();
	private Integer prevLittles;
	private static final Big UNMATCHED = new Big("No match");
	
	public Big(String name, Integer prevLittles) {
		super(name);
		this.prevLittles = prevLittles;
	}
	
	public Big(String name) {
		this(name, null);
	}
	
	public static Big unmatched() {
		return UNMATCHED;
	}
	
	public Integer getPrevLittles() {
		return prevLittles;
	}
	
	public void setPrevLittles(int littles) {
		this.prevLittles = littles;
	}

}
