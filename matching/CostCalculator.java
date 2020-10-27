package matching;

public class CostCalculator {

	/* 	multiplier to increase the priority of little's preferences. For
		example, a value of 2 means that little's preferences will be
		considered twice as important when calculating the potential cost
		of a pairing
	*/
	private static int littlePrefMultiplier = 2;

	/* 	calculate the potential cost of a pairing, which is a linear combination
		of the little's preference and the big's preference. The big's number
		of previous littles is also used in the calculation (if
		MatchingDriver.includePrevLittles is true)
	*/
	public static int calculateCost(Little little, Big big) {
		return (littlePrefMultiplier * little.rankings.get(big)) + big.rankings.get(little) + (big.getPrevLittles() == null ? 0 : big.getPrevLittles());
	}
	
	public static void setLittlePrefMultiplier(int multiplier) {
		littlePrefMultiplier = multiplier;
	}
	
}
