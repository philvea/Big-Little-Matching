package matching;

public class CostCalculator {

	private static int littlePrefMultiplier = 2;
	
	public static int calculateCost(Little little, Big big) {
		return (littlePrefMultiplier * little.rankings.get(big)) + big.rankings.get(little) + (big.getPrevLittles() == null ? 0 : big.getPrevLittles());
	}
	
	public static void setLittlePrefMultiplier(int multiplier) {
		littlePrefMultiplier = multiplier;
	}
	
}
