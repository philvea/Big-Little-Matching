package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.util.*;
import org.junit.Test;
import matching.*;

public class Tests {

	private final String testPath = "src/tests/";
	private final String littleFileName = "/little_preferences.csv";
	private final String bigFileName = "/big_preferences.csv";
	
	@Test
	public void testGreedySimple2x2() {
		//test extremely simple case with 2 bigs, 2 littles, everyone gets their first choice
		MatchingDriver driver = new MatchingDriver(MatchingDriver.MATCHING_TYPE.GREEDY);
		String currPath = "sample-input-1";
		String littlePath = testPath + currPath + littleFileName;
		String bigPath = testPath + currPath + bigFileName;
		
		LinkedHashMap<String, Little> littles = new LinkedHashMap<>();
		LinkedHashMap<String, Big> bigs = new LinkedHashMap<>();
		
		Collection<Pairing> pairings;
		driver.getPreferences(new File(littlePath), new File(bigPath), littles, bigs);
		int numLittles = littles.size();
		pairings = driver.match(littles, bigs);
		
		assertEquals(numLittles, pairings.size());

		assertTrue(pairings.contains(new Pairing(new Little("A"), new Big("X"))));
		assertTrue(pairings.contains(new Pairing(new Little("B"), new Big("Y"))));
	}
	
	@Test
	public void testGreedySimple3x3() {
		//test simple case with 3 bigs, 3 littles, not everyone gets their first choice
		MatchingDriver driver = new MatchingDriver(MatchingDriver.MATCHING_TYPE.GREEDY);
		String currPath = "sample-input-2";
		String littlePath = testPath + currPath + littleFileName;
		String bigPath = testPath + currPath + bigFileName;
		
		LinkedHashMap<String, Little> littles = new LinkedHashMap<>();
		LinkedHashMap<String, Big> bigs = new LinkedHashMap<>();
		
		Collection<Pairing> pairings;
		driver.getPreferences(new File(littlePath), new File(bigPath), littles, bigs);
		int numLittles = littles.size();
		pairings = driver.match(littles, bigs);
		
		assertEquals(numLittles, pairings.size());
		assertTrue(pairings.contains(new Pairing(new Little("C"), new Big("X"))));
		assertTrue(pairings.contains(new Pairing(new Little("A"), new Big("Z"))));
		assertTrue(pairings.contains(new Pairing(new Little("B"), new Big("Y"))));
	}
	
	@Test
	public void testGreedyOneToOnePairings() {
		//test that the algorithm makes 1-1 pairings even when it increases the overall cost
		MatchingDriver driver = new MatchingDriver(MatchingDriver.MATCHING_TYPE.GREEDY);

		Little littleA = new Little("A");
		Little littleB = new Little("B");
		Big bigX = new Big("X");
		Big bigY = new Big("Y");
		
		littleA.rankings.put(bigX, Integer.valueOf(1));
		littleA.rankings.put(bigY, Integer.valueOf(2));
		
		littleB.rankings.put(bigX, Integer.valueOf(2));
		littleB.rankings.put(bigY, Integer.valueOf(5));
		
		bigX.rankings.put(littleA, Integer.valueOf(1));
		bigX.rankings.put(littleB, Integer.valueOf(2));
		
		bigY.rankings.put(littleA, Integer.valueOf(2));
		bigY.rankings.put(littleB, Integer.valueOf(5));
		
		
		LinkedHashMap<String, Little> littles = new LinkedHashMap<>();
		LinkedHashMap<String, Big> bigs = new LinkedHashMap<>();
		littles.put(littleA.getName(), littleA);
		littles.put(littleB.getName(), littleB);
		int numLittles = littles.size();
		bigs.put(bigX.getName(), bigX);
		bigs.put(bigY.getName(), bigY);
		
		Collection<Pairing> pairings;
		pairings = driver.match(littles, bigs);
		assertEquals(numLittles, pairings.size());
		assertTrue(pairings.contains(new Pairing(littleA, bigX)));
		assertTrue(pairings.contains(new Pairing(littleB, bigY)));
	}
	
	@Test(expected = RuntimeException.class)
	public void testNotEnoughLittles() {
		//test when # littles is less than # bigs (error case)
		MatchingDriver driver = new MatchingDriver(MatchingDriver.MATCHING_TYPE.GREEDY);

		Little littleA = new Little("A");
		Little littleB = new Little("B");
		Big bigX = new Big("X");
		
		littleA.rankings.put(bigX, Integer.valueOf(1));
		littleB.rankings.put(bigX, Integer.valueOf(2));
		
		bigX.rankings.put(littleA, Integer.valueOf(1));
		bigX.rankings.put(littleB, Integer.valueOf(2));
		
		LinkedHashMap<String, Little> littles = new LinkedHashMap<>();
		LinkedHashMap<String, Big> bigs = new LinkedHashMap<>();
		littles.put(littleA.getName(), littleA);
		littles.put(littleB.getName(), littleB);
		bigs.put(bigX.getName(), bigX);
		driver.match(littles, bigs);
	}
	
	@Test(expected = RuntimeException.class)
	public void testDuplicateLittleEntries() {
		//test input with two entries from the same little (error case)
		MatchingDriver driver = new MatchingDriver(MatchingDriver.MATCHING_TYPE.GREEDY);

		String currPath = "sample-input-3";
		String littlePath = testPath + currPath + littleFileName;
		String bigPath = testPath + currPath + bigFileName;
		
		LinkedHashMap<String, Little> littles = new LinkedHashMap<>();
		LinkedHashMap<String, Big> bigs = new LinkedHashMap<>();
		
		driver.getPreferences(new File(littlePath), new File(bigPath), littles, bigs);
		driver.match(littles, bigs);
	}
	
	@Test(expected = RuntimeException.class)
	public void testDuplicateBigEntries() {
		//test input with two entries from the same big (error case)
		MatchingDriver driver = new MatchingDriver(MatchingDriver.MATCHING_TYPE.GREEDY);

		String currPath = "sample-input-4";
		String littlePath = testPath + currPath + littleFileName;
		String bigPath = testPath + currPath + bigFileName;
		LinkedHashMap<String, Little> littles = new LinkedHashMap<>();
		LinkedHashMap<String, Big> bigs = new LinkedHashMap<>();
		
		driver.getPreferences(new File(littlePath), new File(bigPath), littles, bigs);
		driver.match(littles, bigs);
	}
	
	@Test
	public void testMinCostSimple1() {
		//test extremely simple case with 2 bigs, 2 littles, everyone gets their first choice
		MatchingDriver driver = new MatchingDriver(MatchingDriver.MATCHING_TYPE.MIN_COST);
		String currPath = "sample-input-1";
		String littlePath = testPath + currPath + littleFileName;
		String bigPath = testPath + currPath + bigFileName;
		
		LinkedHashMap<String, Little> littles = new LinkedHashMap<>();
		LinkedHashMap<String, Big> bigs = new LinkedHashMap<>();
		
		Collection<Pairing> pairings;
		driver.getPreferences(new File(littlePath), new File(bigPath), littles, bigs);
		LinkedHashMap<String, Little> littlesCopy = new LinkedHashMap<>(littles);
		LinkedHashMap<String, Big> bigsCopy = new LinkedHashMap<>(bigs);
		int numLittles = littles.size();
		pairings = driver.match(littles, bigs);
		
		assertEquals(numLittles, pairings.size());
		assertTrue(pairings.contains(new Pairing(littlesCopy.get("A"), bigsCopy.get("X"))));
		assertTrue(pairings.contains(new Pairing(littlesCopy.get("B"), bigsCopy.get("Y"))));
	}
	
	@Test
	public void testMinCostSimple2() {
		/*
		 * test that the algorithm correctly minimizes cost even when it requires sacrificing ideal pairs\
		 * 	  Matrix:
		 *      X   Y
		 *  A   4   10
		 *  B   10  20
		 *  
		 *  should sacrifice the A-X pairing minimize overall cost
		 */
		MatchingDriver driver = new MatchingDriver(MatchingDriver.MATCHING_TYPE.MIN_COST);

		Little littleA = new Little("A");
		Little littleB = new Little("B");
		Big bigX = new Big("X");
		Big bigY = new Big("Y");
		
		littleA.rankings.put(bigX, Integer.valueOf(2));
		littleA.rankings.put(bigY, Integer.valueOf(5));
		
		littleB.rankings.put(bigX, Integer.valueOf(5));
		littleB.rankings.put(bigY, Integer.valueOf(10));
		
		bigX.rankings.put(littleA, Integer.valueOf(2));
		bigX.rankings.put(littleB, Integer.valueOf(5));
		
		bigY.rankings.put(littleA, Integer.valueOf(5));
		bigY.rankings.put(littleB, Integer.valueOf(10));
		
		
		LinkedHashMap<String, Little> littles = new LinkedHashMap<>();
		LinkedHashMap<String, Big> bigs = new LinkedHashMap<>();
		littles.put(littleA.getName(), littleA);
		littles.put(littleB.getName(), littleB);
		bigs.put(bigX.getName(), bigX);
		bigs.put(bigY.getName(), bigY);
		
		Collection<Pairing> pairings;
		pairings = driver.match(littles, bigs);
		assertEquals(littles.size(), pairings.size());
		assertTrue(pairings.contains(new Pairing(littleA, bigY)));
		assertTrue(pairings.contains(new Pairing(littleB, bigX)));
	}
	
	@Test
	public void testMinCostOneToOnePairing() {
		/*
		 * test that the algorithm correctly matches 1:1 pairings even when it increases total cost
		 * 	  Matrix:
		 *      X   Y
		 *  A   2   10
		 *  B   10  20
		 *  
		 */
		MatchingDriver driver = new MatchingDriver(MatchingDriver.MATCHING_TYPE.MIN_COST);

		Little littleA = new Little("A");
		Little littleB = new Little("B");
		Big bigX = new Big("X");
		Big bigY = new Big("Y");
		
		littleA.rankings.put(bigX, Integer.valueOf(1));
		littleA.rankings.put(bigY, Integer.valueOf(5));
		
		littleB.rankings.put(bigX, Integer.valueOf(5));
		littleB.rankings.put(bigY, Integer.valueOf(10));
		
		bigX.rankings.put(littleA, Integer.valueOf(1));
		bigX.rankings.put(littleB, Integer.valueOf(5));
		
		bigY.rankings.put(littleA, Integer.valueOf(5));
		bigY.rankings.put(littleB, Integer.valueOf(10));
		
		
		LinkedHashMap<String, Little> littles = new LinkedHashMap<>();
		LinkedHashMap<String, Big> bigs = new LinkedHashMap<>();
		
		littles.put(littleA.getName(), littleA);
		littles.put(littleB.getName(), littleB);
		bigs.put(bigX.getName(), bigX);
		bigs.put(bigY.getName(), bigY);
		int numLittles = littles.size();
		
		Collection<Pairing> pairings;
		pairings = driver.match(littles, bigs);
		assertEquals(numLittles, pairings.size());
		assertTrue(pairings.contains(new Pairing(littleA, bigX)));
		assertTrue(pairings.contains(new Pairing(littleB, bigY)));
	}
	
	@Test
	public void testMinCostExpansion() {
		/*
		 * test that the algorithm works when # littles is not equal to # bigs,
		 *  i.e. a matrix expansion is required and extraneous results must be filtered out
		 * 	  Matrix:
		 *      X   Y   Z
		 *  A   4   10  10
		 *  B   10  20  4
		 *  
		 */
		MatchingDriver driver = new MatchingDriver(MatchingDriver.MATCHING_TYPE.MIN_COST);

		Little littleA = new Little("A");
		Little littleB = new Little("B");
		Big bigX = new Big("X");
		Big bigY = new Big("Y");
		Big bigZ = new Big("Z");
		
		littleA.rankings.put(bigX, Integer.valueOf(2));
		littleA.rankings.put(bigY, Integer.valueOf(5));
		littleA.rankings.put(bigZ, Integer.valueOf(5));
		
		littleB.rankings.put(bigX, Integer.valueOf(5));
		littleB.rankings.put(bigY, Integer.valueOf(10));
		littleB.rankings.put(bigZ, Integer.valueOf(2));
		
		bigX.rankings.put(littleA, Integer.valueOf(2));
		bigX.rankings.put(littleB, Integer.valueOf(5));
		
		bigY.rankings.put(littleA, Integer.valueOf(5));
		bigY.rankings.put(littleB, Integer.valueOf(10));
		
		bigZ.rankings.put(littleA, Integer.valueOf(5));
		bigZ.rankings.put(littleB, Integer.valueOf(2));
		
		LinkedHashMap<String, Little> littles = new LinkedHashMap<>();
		LinkedHashMap<String, Big> bigs = new LinkedHashMap<>();
		littles.put(littleA.getName(), littleA);
		littles.put(littleB.getName(), littleB);
		bigs.put(bigX.getName(), bigX);
		bigs.put(bigY.getName(), bigY);
		bigs.put(bigZ.getName(), bigZ);
		
		Collection<Pairing> pairings;
		pairings = driver.match(littles, bigs);

		assertEquals(littles.size(), pairings.size());
		assertTrue(pairings.contains(new Pairing(littleA, bigX)));
		assertTrue(pairings.contains(new Pairing(littleB, bigZ)));
	}
	
	@Test
	public void testMinCostUnmatchedLittle1() {
		/*
		 * test algorithm behavior when a little has no possible bigs
		 * 	  Matrix:
		 *      X   Y
		 *  A   2   3
		 *  B
		 *  
		 *  should sacrifice the A-X pairing minimize overall cost
		 */
		MatchingDriver driver = new MatchingDriver(MatchingDriver.MATCHING_TYPE.MIN_COST);

		Little littleA = new Little("A");
		Little littleB = new Little("B");
		Big bigX = new Big("X");
		Big bigY = new Big("Y");
		
		littleA.rankings.put(bigX, Integer.valueOf(1));
		littleA.rankings.put(bigY, Integer.valueOf(2));
		
		littleB.rankings.put(bigX, Integer.valueOf(2));
		littleB.rankings.put(bigY, Integer.valueOf(1));
		
		bigX.rankings.put(littleA, Integer.valueOf(1));
		
		bigY.rankings.put(littleA, Integer.valueOf(2));
		
		
		LinkedHashMap<String, Little> littles = new LinkedHashMap<>();
		LinkedHashMap<String, Big> bigs = new LinkedHashMap<>();
		littles.put(littleA.getName(), littleA);
		littles.put(littleB.getName(), littleB);
		int numLittles = littles.size();
		bigs.put(bigX.getName(), bigX);
		bigs.put(bigY.getName(), bigY);
		
		Collection<Pairing> pairings;
		pairings = driver.match(littles, bigs);
		assertEquals(numLittles, pairings.size());
		assertTrue(pairings.contains(new Pairing(littleA, bigX)));
		assertTrue(pairings.contains(new Pairing(littleB, Big.unmatched())));
	}
	
	@Test
	public void testMinCostUnmatchedLittle2() {
		/*
		 * test algorithm behavior when a little has no possible bigs 
		 * 	  Matrix:
		 *      X   Y
		 *  A   3   4
		 *  B
		 *  
		 *  should sacrifice the A-X pairing minimize overall cost
		 */
		MatchingDriver driver = new MatchingDriver(MatchingDriver.MATCHING_TYPE.MIN_COST);

		Little littleA = new Little("A");
		Little littleB = new Little("B");
		Big bigX = new Big("X");
		Big bigY = new Big("Y");
		
		littleA.rankings.put(bigX, Integer.valueOf(2));
		littleA.rankings.put(bigY, Integer.valueOf(3));
		
		littleB.rankings.put(bigX, Integer.valueOf(2));
		littleB.rankings.put(bigY, Integer.valueOf(1));
		
		bigX.rankings.put(littleA, Integer.valueOf(2));
		
		bigY.rankings.put(littleA, Integer.valueOf(2));
		
		
		LinkedHashMap<String, Little> littles = new LinkedHashMap<>();
		LinkedHashMap<String, Big> bigs = new LinkedHashMap<>();
		littles.put(littleA.getName(), littleA);
		littles.put(littleB.getName(), littleB);
		bigs.put(bigX.getName(), bigX);
		bigs.put(bigY.getName(), bigY);
		
		Collection<Pairing> pairings;
		pairings = driver.match(littles, bigs);
		assertEquals(littles.size(), pairings.size());
		assertTrue(pairings.contains(new Pairing(littleA, bigX)));
		assertTrue(pairings.contains(new Pairing(littleB, Big.unmatched())));
	}
	
	@Test
	public void testGreedyUnmatchedLittle1() {
		/*
		 * test algorithm behavior when a little has no possible bigs
		 * 	  Matrix:
		 *      X   Y
		 *  A   2   3
		 *  B
		 *  
		 *  should sacrifice the A-X pairing minimize overall cost
		 */
		MatchingDriver driver = new MatchingDriver(MatchingDriver.MATCHING_TYPE.GREEDY);

		Little littleA = new Little("A");
		Little littleB = new Little("B");
		Big bigX = new Big("X");
		Big bigY = new Big("Y");
		
		littleA.rankings.put(bigX, Integer.valueOf(1));
		littleA.rankings.put(bigY, Integer.valueOf(2));
		
		littleB.rankings.put(bigX, Integer.valueOf(2));
		littleB.rankings.put(bigY, Integer.valueOf(1));
		
		bigX.rankings.put(littleA, Integer.valueOf(1));
		
		bigY.rankings.put(littleA, Integer.valueOf(2));
		
		
		LinkedHashMap<String, Little> littles = new LinkedHashMap<>();
		LinkedHashMap<String, Big> bigs = new LinkedHashMap<>();
		littles.put(littleA.getName(), littleA);
		littles.put(littleB.getName(), littleB);
		int numLittles = littles.size();
		bigs.put(bigX.getName(), bigX);
		bigs.put(bigY.getName(), bigY);
		
		Collection<Pairing> pairings;
		pairings = driver.match(littles, bigs);
		assertEquals(numLittles, pairings.size());
		assertTrue(pairings.contains(new Pairing(littleA, bigX)));
		assertTrue(pairings.contains(new Pairing(littleB, Big.unmatched())));
	}
	
	@Test
	public void testGreedyUnmatchedLittle2() {
		/*
		 * test algorithm behavior when a little has no possible bigs 
		 * 	  Matrix:
		 *      X   Y
		 *  A   3   4
		 *  B
		 *  
		 *  should sacrifice the A-X pairing minimize overall cost
		 */
		MatchingDriver driver = new MatchingDriver(MatchingDriver.MATCHING_TYPE.GREEDY);

		Little littleA = new Little("A");
		Little littleB = new Little("B");
		Big bigX = new Big("X");
		Big bigY = new Big("Y");
		
		littleA.rankings.put(bigX, Integer.valueOf(2));
		littleA.rankings.put(bigY, Integer.valueOf(3));
		
		littleB.rankings.put(bigX, Integer.valueOf(2));
		littleB.rankings.put(bigY, Integer.valueOf(1));
		
		bigX.rankings.put(littleA, Integer.valueOf(2));
		
		bigY.rankings.put(littleA, Integer.valueOf(2));
		
		
		LinkedHashMap<String, Little> littles = new LinkedHashMap<>();
		LinkedHashMap<String, Big> bigs = new LinkedHashMap<>();
		littles.put(littleA.getName(), littleA);
		littles.put(littleB.getName(), littleB);
		bigs.put(bigX.getName(), bigX);
		bigs.put(bigY.getName(), bigY);
		
		Collection<Pairing> pairings;
		pairings = driver.match(littles, bigs);
		assertEquals(littles.size(), pairings.size());
		assertTrue(pairings.contains(new Pairing(littleA, bigX)));
		assertTrue(pairings.contains(new Pairing(littleB, Big.unmatched())));
	}

}
