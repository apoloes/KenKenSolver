# KenKenSolver

This is our KenKen Solver for UNC's CS 560 Artificial Intelligence
Our program implements three different solutions to a KenKen problem: the first implementation is a **basic backtracking solver**, the second implementation is an **improved backtracking solver**, the third implementation is a **local search solver**.

To run these implementations you will have to get all 4 classes (KenKen, Matrix, Cage, Point)

## To run all three implementations at the same time
- Specify the path file of the KenKen problem
- The program should be ready to run all three implementations without any changes

## To run the first implementation only (Basic Backtracker):
- Specify the path file of the KenKen problem
- Find the KenKen object called 'game'
  - KenKen game = new KenKen();
- If the game has been 'solved' previously (aka you ran BasicBacktrackingStarter or BestBacktrackingStarter) then you have to clear the matrix before attempting to solve again
  - game.clearMatrix(m)
- Run KenKen's BasicBacktrackingStarter method and pass the Matrix 'm' as an argument, then run the m.toString() method to print the solution
  - Matrix m = new Matrix(size);
  - game.BasicBacktrackingStarter(m);
  - System.out.println(m.toString());
- This should print out the KenKen solution and a counter of how many nodes (Points) were searched
  - System.out.println("Basic: " + game.counter1Basic);

## To run the second implementation only (Improved Backtracker):
- Specify the path file of the KenKen problem
- Find the KenKen object called 'game'
  - KenKen game = new KenKen();
- If the game has been 'solved' previously (aka you ran BasicBacktrackingStarter or BestBacktrackingStarter) then you have to clear the matrix before attempting to solve again
  - game.clearMatrix(m);
- Run KenKen's BestBacktrackingStarter method and pass the Matrix 'm' as an argument, then run the m.toString() method to print the solution
  - Matrix m = new Matrix(size);
  - game.BestBacktrackingStarter(m);
  - System.out.println(m.toString());
- To print the counter of how many nodes (Points) were searched then do the following line
  - System.out.println("Best: " + game.counter1Best);

## To run the third implementation only (Local Search):
- Specify the path file of the KenKen problem
- Find the KenKen object called 'game'
  - KenKen game = new KenKen();
- The Matrix doesn't have to be cleared before running the Local Search because the LocalSearchStarter fills the Matrix with random Points
- Run KenKen's LocalSearchStarter method and pass the Matrix 'm' as an argument
  - Matrix m = new Matrix(size);
  - game.LocalSearchStarter(m);
  - System.out.println(m.toString());
- To print the count of how many swaps were performed across all randomized Matrices (counterForLocalTotal) and the count of how many swaps were performed for the solution Matrix (counterForLocalSolution), run the following lines
  - System.out.println("Local total: " + game.counterForLocalTotal);
  - System.out.println("Local solution: " + game.counterForLocalSolution);
