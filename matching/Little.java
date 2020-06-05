package matching;

import java.util.*;

public class Little extends Member {

	public HashMap <Big, Integer> rankings = new HashMap<>();

	public Little(String name) {
		super(name);
	}
	
	@Override
	public String toString() {
		return name;
		
	}

}
