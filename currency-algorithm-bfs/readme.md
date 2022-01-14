Billys program design:


billy's program starts with retrieving the
lines from input file.

my data structes that i used are ArrayList
and linked lists.

the method of traversingthe linked list i
used was BFS

my program comes with 2 objects Node and
Edge

the edge would carry a weight and the endpoint of the edge

the node would carry n number of edges.

i am aware that it is directed so i had the edges 
built only one way
ie :

cnd ---9--> franc

this shows one edge with the weight of 9

each # of paths it would pass, it would collect the 
weight and multiply it with the next path ie:

cnd --2---> franc --4--> lira 
2 * 4 = 8
this result 8 is the currency change
from cnd to lira



on another case when there is an edge to each other
i have a method to calculate the lowest common multiple
ie:

cnd --2-->franc
cnd <-4-- franc

least common multiple function outputs this result:

cnd --1-->franc
cnd <-2-- franc


see pdf for more details
