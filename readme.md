# README
This is a group project for solving Minimum Vertex Cover problem.
## Implementation
Four Algorithms are implemented

  * Construction Heuristics with Approximation guarantees
  * Exact algorithm using BnB
  * Local Search using Hill Climbing policy
  * Local Search using Simulated Annealing policy

## Structure
Solver contains the Main class and the util to readFile, SolutionRecorder is the singleton class handles output job. Vertex, Edge and Graph are classes and are populated during input reading precedure. BnBClone, Approx, LocalSearch implement different algorithms. 

## Run program
in this directory, open a shell and use the following command:

``
java -cp "./bin:./lib/*" -Xms500m -Xss8M Solver -inst ./Data/$graph -alg BnB -time 600 -seed 12
``

-alg and -inst are necessary, if -time or -seed is not provided, the cutoff time value will be default to be 600, seed will be default to be 1.

the order of the latter 4 parameters is not fixed. 

## dependency:
several extra library is used and included in /lib.  ``commons-cli-1.4`` for command parsing.
``GLPKSolverPack`` and ``SCPSolver`` for LP problem solving.
