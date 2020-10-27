package matching;
import java.util.*;

public class Big extends Member{

	public HashMap <Little, Integer> rankings = new HashMap<>();
	private Integer prevLittles;

	// singleton Big that will be paired with all unmatched littles
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
		/* 	if MatchingDriver.includePrevLittles is false, this will return null
			if MatchingDriver.includePrevLittles is true, this will return the
			number of previous littles the big has had (to be used in cost
			calculation)
		*/
		return prevLittles;
	}
	
	public void setPrevLittles(int littles) {
		this.prevLittles = littles;
	}

}
