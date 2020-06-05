package matching;

public class Member {
	protected String name;
	
	public Member(String name) {
		this.name = name;
	}
	
	
	public String getName() {
		return name;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Member)) {
			return false;
		}
		return name.equals(((Member)o).name);
	}
}
