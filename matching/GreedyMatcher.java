package matching;

import java.util.*;

public class GreedyMatcher implements Matcher {

	private HashMap <String, Little> littles;

	public GreedyMatcher(HashMap <String, Little> littles) {
		this.littles = littles;
	}

	public Collection<Pairing> makePairings() {
		//create a PQ of all of the possible pairings
		PriorityQueue <Pairing> pq = new PriorityQueue<>();
		
		for (Little little : littles.values()) {
			for (Big big : little.rankings.keySet()) {
				if (big.rankings.keySet().contains(little)) {
					pq.add(new Pairing(little, big));
				}
			}
		}

		HashSet<Little> pairedLittles = new HashSet<>();
		HashSet<Big> pairedBigs = new HashSet<>();
		HashSet<Pairing> pairings = new HashSet<>();
		
		while (pairedLittles.size() < littles.size() && pq.size() > 0) {
			Pairing minPairing = pq.poll();
			if (!pairedLittles.contains(minPairing.getLittle()) && !pairedBigs.contains(minPairing.getBig())) {
				pairedLittles.add(minPairing.getLittle());
				pairedBigs.add(minPairing.getBig());
				pairings.add(minPairing);
			}
		}
		
		for (Little little : littles.values()) {
			if (!pairedLittles.contains(little)) {
				pairings.add(new Pairing(little, Big.unmatched()));
			}
		}
		
		return pairings;
	}
}
