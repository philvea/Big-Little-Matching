# Big-Little-Matching


## Problem

Big-little matching can be tedious as well as prone to bias and mistakes when done by hand. This project aims to make big-little matching fairer and easier by automating the process.

To make big-little pairings, each potential pairing is assigned a hypothetical 'cost', which represents how desirable the pairing is according to the preferences entered. A set of pairings is then calculated using either the greedy matching algorithm or the minimum cost algorithm.


## Greedy Algorithm

#### Rationale
The intuition for using a greedy algorithm is that a similar process is usually used when matching is done by hand. If results resembling those obtained by hand are desired, then this method should be used. Although this algorithm is fairly easy to do by hand, automating it saves considerable time and eliminates the possiblity of bias or mistakes.

#### Algorithm
The greedy algorithm is extremely simple. It finds the minimum cost pairing between all the bigs and littles that have not yet been paired, and adds that pairing to a list of pairings. The algorithm then continues the process on the remaining bigs and littles until all of the littles have been assigned a pairing. This is done by creating a Priority Queue of all possible pairings and continuously removing the top element (the pairing with minimum cost).

Despite its simplicity, the greedy algorithm works fairly well in most cases. One could argue that this is the fairest way to make pairings. If you believe that pairings with high compatibility should be made, and these pairings should not be sacrificed to benefit those without high compatibility pairings, then the greedy algorithm makes the most sense.


## Minimum Cost Matching

#### Rationale
Although the greedy algorithm works well in most cases, there are situations where an alternative approach could be more desirable. The following hypothetical cost matrix illustrates this. Each number in the matrix represents the hypothetical cost of that pairing, where low numbers represent good pairings and high numbers represent bad pairings.
|| Gail | Terri |
| --- | --- | --- |
| **Kailum** | 4 | 10 |
| **Neve** | 10 | 40 |

The greedy algorithm would produce the pairings (Gail, Kailum), (Terri, Neve). The sum of the costs of these pairings is 44. An alternate pairing would be (Gail, Neve), (Terri, Kailum), which would have a total cost of 20, which is a significantly lower cost.

Clearly, there are situations where making a greedy matching can increase the total cost of the matching. The idea of mimumum cost matching is to minimize the total cost of all the pairings, which may involve sacrifing a good pairing for a slightly worse pairing because it benefits the group as a whole.


#### Algorithm
This matching problem can be thought of as a graph problem. Say there are m vertices representing littles and n vertices representing potential bigs (can assume n >= m). There is also a set of weighted edges that connect the little vertices with big vertices. The goal is to select one edge from each little so that the total cost of the m edges selected is minimized and no vertex representing a big has more than one outgoing edge. Thus, finding a minimum cost big-little matching is equivalent to finding a minimum cost matching in a weighted bipartite graph, also known as the [unbalanced assignment problem](https://en.wikipedia.org/wiki/Assignment_problem#Unbalanced_assignment).

It isn't possible to brute force this, because there are way too many possible combinations to iterate over, even with an extremely small data set. For example, with 20 bigs and 10 littles, there are over 600 billion possible ways to pair the members. Fortunately, this  can be solved in polynomial time using the [Hungarian method](https://web.eecs.umich.edu/~pettie/matching/Kuhn-hungarian-assignment.pdf).


## Considerations/Future Work
- The cost of a pairing is currently calculated as a linear combination of the big preference and the little preference. Is there a better/fairer way to represent the cost? Should other factors be included?
- What should the cost of an unmatched little be?
- Add UI for inputting preference files and settings.
- Add more error handling. E.g. if a little lists a big multiple times in the preference file.
- Document process for running project on command line from windows/mac
