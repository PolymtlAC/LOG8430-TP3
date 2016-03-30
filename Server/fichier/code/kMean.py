
import collections
Antenna = collections.namedtuple('Antenna', ['x', 'y', 'r'])
Point = collections.namedtuple('Point', ['x', 'y'])

from random import randint # random int between two ints (included)
import math

# distance (squared) between a position and an antenna
def dist2(p, a) :
	dx = p[0] - a[0]
	dy = p[1] - a[1]
	return dx * dx + dy * dy

# returns the index of the nearest antenna from a position
def getNearest(pos, antennas) :
	return min(range(len(antennas)), # TODO : xrange ?
		key=lambda a : dist2(pos,antennas[a]))

# checking that all antennas are covered
def isCorrect(positions, antennas) :
    return  all([
        any([
                dist2(p,a) <= a[2] * a[2] for a in antennas
            ]) for p in positions
    ])


def cost(antennas, K, C) :
	return sum([ (K + C * a[2] * a[2]) for a in antennas ])

# returns an antenna covering all the positions
def baryCenter(posList) :

	# TODO : reduce instead
	xCenter = sum([p.x for p in posList]) / len(posList)
	yCenter = sum([p.y for p in posList]) / len(posList)
	xCenter = int(xCenter); yCenter = int(yCenter);
	rMax = math.sqrt(max([ dist2(p, Antenna(xCenter,yCenter,1)) for p in posList ]))
	rMax = max(1, int(math.ceil(rMax)));

	return Antenna(xCenter,yCenter,rMax);

# gets the circumcircle of 3 points
# (be careful when rounding to integers)
def circumCenter(p1,p2,p3) :

	ax = p1.x; ay = p1.y;
	bx = p2.x; by = p2.y;
	cx = p3.x; cy = p3.y;

	d = 2*(ax*(by-cy)+bx*(cy-ay)+cx*(ay-by));

	# is the triangle obtuse ?
	obtuse1 = (bx-ax)*(cx-ax) + (by-ay)*(cy-ay) < 0;
	obtuse2 = (ax-bx)*(cx-bx) + (ay-by)*(cy-by) < 0;
	obtuse3 = (bx-cx)*(ax-cx) + (by-cy)*(ay-cy) < 0;
	
	if d == 0 or obtuse1 or obtuse2 or obtuse3 : # all points are aligned

		# finding the 2 most far appart points
		d1 = dist2(p1,p2)
		d2 = dist2(p1,p3)
		d3 = dist2(p2,p3)
		if d1 <= d3 and d2 <= d3 :
			x = int((p2.x+p3.x)/2)
			y = int((p2.y+p3.y)/2)
			a = Point(x,y)
			r = int(math.ceil(math.sqrt(max([ dist2(p,a) for p in [p1, p2, p3]]))))
			return Antenna(x,y,r)

		elif d1 <= d2 and d3 <= d2 :
			x = int((p1.x+p3.x)/2)
			y = int((p1.y+p3.y)/2)
			a = Point(x,y)
			r = int(math.ceil(math.sqrt(max([ dist2(p,a) for p in [p1, p2, p3]]))))
			return Antenna(x,y,r)

		elif d2 <= d1 and d3 <= d1 :
			x = int((p2.x+p1.x)/2)
			y = int((p2.y+p1.y)/2)
			a = Point(x,y)
			r = int(math.ceil(math.sqrt(max([ dist2(p,a) for p in [p1, p2, p3]]))))
			return Antenna(x,y,r)

	x = ((ax*ax + ay*ay)*(by-cy)+(bx*bx+by*by)*(cy-ay) + (cx*cx+cy*cy)*(ay-by))/d;
	y = ((ax*ax + ay*ay)*(cx-bx)+(bx*bx+by*by)*(ax-cx) + (cx*cx+cy*cy)*(bx-ax))/d;
	x = int(x); y = int(y);
	a = Point(x,y);
	r = int(math.ceil(math.sqrt(max([ dist2(p,a) for p in [p1, p2, p3]]))))

	return Antenna(x,y,r);

# returns an antenna covering all the positions, using :
# https://en.wikipedia.org/wiki/Smallest-circle_problem
def covCenter(posList) :

	n = len(posList);

	if(n >= 3) :
		bestAntenna = None;
		bestR = float('inf')
		for i1 in range(0,n) :
			p1 = posList[i1];
			for i2 in range(i1+1,n) :
				p2 = posList[i2];
				for i3 in range(i2+1,n) :
					p3 = posList[i3];

					a = circumCenter(p1,p2,p3);
					if a.r < bestR and all([dist2(a,p) <= a.r*a.r for p in posList]) :

						bestR = a.r;
						bestAntenna = a;

		return bestAntenna;

	elif(len(posList) == 2) :
		p1 = posList[0];
		p2 = posList[1];
		x = int((p1.x+p2.x)/2)
		y = int((p1.y+p2.y)/2)
		a = Point(x,y)
		r = int(math.ceil(math.sqrt(max([ dist2(p,a) for p in [p1, p2]]))))
		return Antenna( x, y, r );

	elif(len(posList) == 1) :
		return Antenna( posList[0].x, posList[0].y, 1 )

# Based on the K-Mean algorithm (random start, and hill-climbing)
def search(positions, K, C) :

	# converting tuples to namedTuples
	positions = [ Point(p[0],p[1]) for p in positions ]

	# bounds of the positions
	minX = min([ p.x for p in positions ])
	maxX = max([ p.x for p in positions ])
	minY = min([ p.y for p in positions ])
	maxY = max([ p.y for p in positions ])

	# best solution found (will be updated)
	bestSolution = []
	bestCost = float('inf')

	for randomStart in range(10 * len(positions)) :

		nbAntenna = randint(1, len(positions)) # random number of antennas

		antennas = [ # initializing the antennas at random positions
			Antenna(randint(minX,maxX), randint(minY,maxY), 1)
			for i in range(nbAntenna)
		]
		
		# each antenna is a cluster
		# we move them until they are the centers
		# of their covered positions
		stabilized = False # clusters have stabilized
		while not stabilized :

			# getting the closest antenna (cluster) from each position
			clusters = [ getNearest(p,antennas) for p in positions ]

			# getting the list of positions from each antenna
			coveredPos = [ [] for _ in antennas ]
			for i in range(len(clusters)) :
				coveredPos[clusters[i]].append(i)

			# for each antenna, the new position is the center
			# of its covered antennas
			stabilized = True # no antenna has moved
			for i in range(nbAntenna) :
				posList = [ positions[p] for p in coveredPos[i] ];
				a = antennas[i]
				if len(posList) == 0 :
					antennas[i] = Antenna(a.x,a.y,-1) # unused antenna
				else :

					c = covCenter(posList);

					if c.x != a.x or c.y != a.y or c.r != a.r :
						stabilized = False
						antennas[i] = c

			# if final optimisation, trying to move antennas
			# around their positions (-1,+1) to reduce radiuses
			if stabilized :
				for i in range(nbAntenna) :
					posList = [ positions[p] for p in coveredPos[i] ];
					a = antennas[i]
					if len(posList) > 0 and a.r > 1 :
						for dx in range(-2,2+1) :
							for dy in range(-2,2+1) :
								a2 = Antenna(a.x+dx,a.y+dy,a.r-1);
								if isCorrect(posList,[a2]) :
									antennas[i] = a2


		# removing the unused antennas
		antennas = [a for a in antennas if a.r >= 0]
		c = cost(antennas,K,C)
		if c < bestCost :
			bestSolution = antennas
			bestCost = c

	return [(a.x,a.y,a.r) for a in bestSolution]