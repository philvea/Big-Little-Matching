package matching;

import java.util.*;
/*
	Matcher class that uses a greedy algorithm to decide pairings. A priority queue
	of all potential big-little pairings is made, and the minimum cost pairing
	is removed from the priority queue. If the pairing represents a valid pairing
	(neither the big or the little has already been paired), then the pairing is
	added to the list of pairings, otherwise it is discarded. The algorithm does
	this continuously until all littles have been paired.
*/
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

		// now, use the PQ to greedily create a set of pairings
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
		
		// For all unmatched littles, create a pairing that indicates they are unmatched
		for (Little little : littles.values()) {
			if (!pairedLittles.contains(little)) {
				pairings.add(new Pairing(little, Big.unmatched()));
			}
		}
		
		return pairings;
	}
}
