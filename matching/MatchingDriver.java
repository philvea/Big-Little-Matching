package matching;

import java.io.*;
import java.util.*;

public class MatchingDriver {

	public enum MATCHING_TYPE {GREEDY, MIN_COST};
	private final int COL_SPACING = 4;
	private MATCHING_TYPE matchingType;
	private boolean includePrevLittles;		//if true, uses the number of previous littles a big has had in edge cost calculation
	private int columnWidth = 12;
    private static final String csvDelimiter = ",";
   
    
    public MatchingDriver(MATCHING_TYPE matchingType, boolean includePrevLittles) {
    	this.matchingType = matchingType;
    	this.includePrevLittles = includePrevLittles;
    }
    
    
    public MatchingDriver(MATCHING_TYPE matchingType) {
    	this(matchingType, false);
    }
    
    
	public static void main(String[] args) {
		MATCHING_TYPE type;
		boolean includeLittles;
		Scanner scanner = new Scanner(System.in);
        LinkedHashMap <String, Big> bigs = new LinkedHashMap<>();
        LinkedHashMap <String, Little> littles = new LinkedHashMap<>();
        
        System.out.println("Enter the type of matching (Greedy or Minimum Cost):");
        String line = scanner.nextLine().toLowerCase();
        while (!line.equals("greedy") && !line.equals("minimum cost")) {
        	System.out.println("Please enter a valid matching type (Greedy or Minimum Cost):");
        	line = scanner.nextLine();
        }
        if (line.equals("greedy")) {
        	type = MATCHING_TYPE.GREEDY;
        } else {
        	type = MATCHING_TYPE.MIN_COST;
        }
        
        System.out.println("Include previous littles in cost calculation? (Yes or No):");
        line = scanner.nextLine().toLowerCase();
        while (!line.equals("yes") && !line.equals("no")) {
        	System.out.println("Please enter a valid response (Yes or No):");
        	line = scanner.nextLine();
        }
        if (line.equals("yes")) {
        	includeLittles = true;
        } else {
        	includeLittles = false;
        }
		MatchingDriver driver = new MatchingDriver(type, includeLittles);

        
        System.out.println("Enter the name of the little preferences file (including path):");
        File littleFile = new File(scanner.nextLine());
        while (!littleFile.exists()) {
        	System.out.println("Couldn't find file. Current search path: " + new File(".").getAbsolutePath());
    		System.out.println("Please try again:");
            littleFile = new File(scanner.nextLine());
        }
        
        System.out.println("Enter the name of the big preferences file (including path):");
        File bigFile = new File(scanner.nextLine());
        while (!bigFile.exists()) {
        	System.out.println("Couldn't find file. Current search path: " + new File(".").getAbsolutePath());
    		System.out.println("Please try again:");
            bigFile = new File(scanner.nextLine());
        } 
        scanner.close();
        driver.getPreferences(littleFile, bigFile, littles, bigs); 
		driver.match(littles, bigs);
	}
	
	
	public void getPreferences(File littleFile, File bigFile, 
			HashMap <String, Little> littles, HashMap <String, Big> bigs) {
        BufferedReader br = null;
        
        try {
            String line = "";
            br = new BufferedReader(new FileReader(littleFile));
            br.readLine();	//ignore header
            while ((line = br.readLine()) != null && line.length() > 0) {

                String[] tokens = line.toUpperCase().split(csvDelimiter);
                columnWidth = Math.max(columnWidth, COL_SPACING + tokens[0].length());
                Little currLittle = littles.get(tokens[0]);
                if (currLittle == null) {
                	//need to add the little to the hashmap 
                	littles.put(tokens[0], new Little(tokens[0]));
                	currLittle = littles.get(tokens[0]);
                } else {
                	//should never have two entries from the same little
                	throw new RuntimeException("Input contains two entries from the same little");
                }

                for (int i=1; i<tokens.length; i++) {
                	Big currBig = bigs.get(tokens[i]);
                	if (currBig == null) {
                		//need to add the big to the hashmap
                		bigs.put(tokens[i], new Big(tokens[i]));
                		currBig = bigs.get(tokens[i]);

                	}
                	currLittle.rankings.put(currBig,  i);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        try {
            String line = "";
            br = new BufferedReader(new FileReader(bigFile));
            br.readLine();	//ignore header
            while ((line = br.readLine()) != null && line.length() > 0) {

                String[] tokens = line.toUpperCase().split(csvDelimiter);
                columnWidth = Math.max(columnWidth, COL_SPACING + tokens[0].length());
                Big currBig = bigs.get(tokens[0]);

                if (currBig == null) {
                	//need to add the big to the hashmap
            		bigs.put(tokens[0], new Big(tokens[0]));
                	currBig = bigs.get(tokens[0]);
                }
                if (includePrevLittles) {
            		try {
            			currBig.setPrevLittles(Integer.valueOf(tokens[1]));
            		} catch (NumberFormatException e) {
            			throw new RuntimeException("First column in big preferences file must contain the number of previous littles");
            		}
                }
            	if (currBig.rankings.size() != 0) {
            		throw new RuntimeException("Input contains two entries from the same big");
            	}

                for (int i= includePrevLittles ? 2 : 1; i<tokens.length; i++) {
                	Little currLittle = littles.get(tokens[i]);
                	if (currLittle == null) {
                		//a big should never list a little that did not have an entry
                		throw new RuntimeException("Encountered unexpected little on big list");

                	}
                	currBig.rankings.put(currLittle, i);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
	}

	
	public Collection<Pairing> match(LinkedHashMap <String, Little> littles, LinkedHashMap <String, Big> bigs) {
        
        if (littles.size() > bigs.size()) {
        	throw new RuntimeException("The number of bigs must be greater than or equal to the number of littles");
        }
        
        LinkedList<Pairing> pairings = new LinkedList<>();
        Matcher matcher = null;
        
    	//before running minimum cost matching, remove 1-1 pairings
    	for (Little little : littles.values()) {
    		for (Big big : bigs.values()) {
    			Integer littlePref = little.rankings.get(big);
    			Integer bigPref = big.rankings.get(little);
    			if (littlePref != null && littlePref.equals(1) && bigPref != null && bigPref.equals(1)) {
    				pairings.add(new Pairing(little, big));
    			}
    		}
    	}
    	
    	//remove all bigs and littles that already have pairings (can't do this during iteration because 
    	//LinkedHashMap iterator is fail-fast)
    	for (Pairing p : pairings) {
    		littles.remove(p.getLittle().name);
    		for (Little little : littles.values()) {
    			little.rankings.remove(p.getBig());
    		}
    	}
        
        
        if (matchingType == MATCHING_TYPE.GREEDY) {
        	matcher = new GreedyMatcher(littles);
        } else if (matchingType == MATCHING_TYPE.MIN_COST) {
        	
        	if (littles.size() > 0) {
	        	String[] littleOrdering = littles.keySet().stream().toArray(String[]::new);
	        	String[] bigOrdering = bigs.keySet().stream().toArray(String[]::new);
	        	double [][] costMatrix = new double [bigs.size()][bigs.size()];
	        	
	        	//create cost matrix for Hungarian method
	        	for (int i=0; i<littleOrdering.length; i++) {
	        		for (int j=0; j<bigOrdering.length; j++) {
        				Little little = littles.get(littleOrdering[i]);
        				Big big = bigs.get(bigOrdering[j]);
        				
        				if (big.rankings.containsKey(little) && little.rankings.containsKey(big)) {
        					costMatrix[i][j] = CostCalculator.calculateCost(little, big);
        				} else {
        					//TODO: change this to a value that better represents the cost of an unpaired matching, in order to
        					//prevent desperate sacrifices in the matching
        					costMatrix[i][j] = Double.MAX_VALUE;	//flag value to show one or both members in the pair did not list each other
        				}
	        		}
	        	}

	        	matcher = new MinimumCostMatcher(costMatrix, littles, bigs);
        	}
        } else {
        	throw new RuntimeException("Matching type not supported");
        }
        
        if (littles.size() > 0) {
        	pairings.addAll(matcher.makePairings());
        }
        Collections.shuffle(pairings);
        printPairings(pairings);
        return pairings;
	}
	

	private void printPairings(Collection<Pairing> pairings) {
		int tableWidth = 2 * columnWidth + 1;
		String rowSeparator = "";
		for (int i=0; i<tableWidth; i++) {
			rowSeparator += "-";
		}
		final String fmtStr = "%-" + columnWidth + "s%-" + columnWidth + "s%-1s\n";
		System.out.println(rowSeparator);
		System.out.format(fmtStr, "| Little", "| Big", "|");
		System.out.println(rowSeparator);

		for (Pairing p : pairings) {
			System.out.format(fmtStr, "| " + p.getLittle().getName(), "| " + p.getBig().getName(), "|");
		}
		System.out.println(rowSeparator);
	}
	
}
