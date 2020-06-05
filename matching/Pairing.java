package matching;

public class Pairing implements Comparable<Pairing>{
	protected final Big big;
	protected final Little little;
	
	public Pairing(Little little, Big big) {
		this.little = little;
		this.big = big;
	}
	
	@Override
	public boolean equals(Object o) {
		Pairing p;
		if (!(o instanceof Pairing)) {
			throw new RuntimeException("Can't compare Pairing with a non-Pairing object");
		}
		p = (Pairing) o;
		return p.big.equals(big) && p.little.equals(little);
	}
	
	@Override
	public int hashCode() {
		return big.hashCode() + little.hashCode();
	}
	
	public Big getBig() {
		return big;
	}
	
	public Little getLittle() {
		return little;
	}

	@Override
	public int compareTo(Pairing p) {
		return CostCalculator.calculateCost(little, big) - CostCalculator.calculateCost(p.little, p.big);
	}

}
