import collections
Antenna = collections.namedtuple('Antenna', ['x', 'y', 'r'])
Point = collections.namedtuple('Point', ['x', 'y'])

import math

# distance (squared) between two positions
def dist2(p1, p2) :
	dx = p1.x - p2.x
	dy = p1.y - p2.y
	return dx * dx + dy * dy

# checking that all antennas are covered
def isCorrect(positions, antennas) :
    return  all([
        any([
                dist2(p,a) <= a.r * a.r for a in antennas
            ]) for p in positions
    ])

def cost(antennas, K, C) :
	return sum([ (K + C * a.r * a.r) for a in antennas ])

# A* state-space search
# for some uncovered positions,
# we recursively search for the best antennas
def searchAStar(positions, K, C) :

	if len(positions) == 0 : # if no positions are left
		return ( 0, [] ) # no antenna, and no cost needed
	if len(positions) == 1 :
		p = positions[0];
		return ( K + C, [Antenna(p.x,p.y,1)])

	# bounds of the positions
	minX = min([ p.x for p in positions ])
	maxX = max([ p.x for p in positions ])
	minY = min([ p.y for p in positions ])
	maxY = max([ p.y for p in positions ])

	solutions = {};

	# for every antenna position
	for x in range(minX,maxX+1) :
		for y in range(minY,maxY+1) :

			antennaPos = Point(x,y)

			# we search the min/max radiuses
			distances2 = [ dist2( p, antennaPos ) for p in positions ];
			maxR = int(math.ceil(math.sqrt(max(distances2))))
			minR = max( 1, int(math.ceil(math.sqrt(min(distances2)))) )

			# we try all radiuses
			for r in range( minR, maxR + 1 ) :

				# positions covered by the antenna
				covPos = tuple([
					positions[i] for i in range(len(positions))
					if distances2[i] <= r*r
				]);

				# if no positions are covered, ignore the antenna
				if len(covPos) == 0 : continue

				# compare the solution to other sets found
				if not covPos in solutions or solutions[covPos].r > r :
					solutions[covPos] = Antenna(x,y,r)

	# sorting all solutions
	solutions = [ (covPos,solutions[covPos]) for covPos in solutions ];
	solutions.sort(key = lambda s : 
		K + C*s[1].r*s[1].r # antenna cost
		+ K * (len(positions) - len(s[0])) # heuristic (K * nb of pos left)
	)

	bestCost = float('inf')
	bestSolution = [];

	for s in solutions :
		a = s[1]
		uncovPos = [ # positions uncovered by the antenna
			p for p in positions
			if dist2(p,a) > a.r*a.r
		]
		result = searchAStar(uncovPos,K,C)
		c = result[0] + K + C*a.r*a.r;
		if c < bestCost :
			bestCost = c
			bestSolution = result[1] + [a]

	return (bestCost, bestSolution);

def search(positions, K, C) :

	# converting tuples to namedTuples
	positions = [ Point(p[0],p[1]) for p in positions ]

	solution = searchAStar( positions, K, C )[1]

	#print("is correct : " + str(isCorrect(positions, solution)))
	#print("cost is " + str(cost(solution,K,C)));

	return [(a.x,a.y,a.r) for a in solution];