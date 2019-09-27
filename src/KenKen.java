
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner; 

public class KenKen {
	
	static int size;
	public int counter1Basic, counter1Best, counterForLocalSolution, counterForLocalTotal;
	
	public static void main(String[] args) throws FileNotFoundException 
	{ 
	    // pass the path to the file as a parameter 
	    File file = new File("C:\\Users\\apoloes\\Desktop\\College\\4th year\\COMP 560\\KenKen560\\sampletext 7x7.txt"); 
	    Scanner sc = new Scanner(file); 
	  
	    //Size of the Matrix
	    size = sc.nextInt();

	    //Create an N by N matrix
	    Matrix m = new Matrix(size);
	    
	    //Populate Matrix with Points
	    for(int i=0; i<size; i++){
	    	String line = sc.next();
	    	for(int j=0; j<size; j++){
	    		Point p = new Point(i,j, line.charAt(j), size); //Local Search: new argument added, a Point's initial value
	    		m.addPoint(p);
	    	}
	    }
	    	    	    
	    
	    //Create Cages and populate Matrix with Cages
	    while (sc.hasNextLine()){
	    	String line = sc.next();

	    	int desired_value = 0;
	    	char cage_char = line.charAt(0);
	    	
	    	//Edge case where a Cage has no operation ('=')
	    	if (line.indexOf(":")+1 == line.length()-1) desired_value = Character.getNumericValue(line.charAt(2)); 
	    	else desired_value = Integer.parseInt(line.substring(line.indexOf(":")+1, line.length()-1));
	    	
	    	char cage_type = line.charAt(line.length()-1);

	    	Cage c = new Cage(cage_char, desired_value, cage_type, size);
	    	m.addCage(c);
	  	}
	    
	    ArrayList<Cage> cages = m.getCageList();
	    
	    Point[][] points = m.getPoints();
	    
	    //Assign Cages to Points and assign Points to a Cage
	    for(int i=0; i<size; i++){
	    	for(int j=0; j<size; j++){
	    		for(int k=0; k<cages.size(); k++){
		    		if (points[i][j].getCageName() == (cages.get(k).getName())){
		    			cages.get(k).setPoints(points[i][j]);
		    			points[i][j].setCage(cages.get(k));
		    		}
	    		}
	    	}
	    }
	    
	    for(Cage c : m.getCageList()){
	    	c.setListValues();
	    }
	    
	    for(int i=0; i<size; i++){
	    	for(int j=0; j<size; j++){
	    		points[i][j].setCagePossVals();	    		
	    	}
	    }
	    
	    
	    //KenKen object that runs the solvers
	    KenKen game = new KenKen();
	    
	    game.BestBacktrackingStarter(m);
	    System.out.println(m.toString());

	    //Clearing matrix Point values so the Best Backtracking can solve the original matrix
	    game.clearMatrix(m);

	    game.BasicBacktrackingStarter(m);

//	    //Don't need to clear the Matrix because LocalSearchStarter starts with a random Matrix
	    game.LocalSearchStarter(m);
	    
	    
	    System.out.println("Basic: " + game.counter1Basic);
	    System.out.println("Best: " + game.counter1Best);
	    System.out.println("Local total: " + game.counterForLocalTotal);
	    System.out.println("Local solution: " + game.counterForLocalSolution);

	   
	} 
	
	/*
	 * These methods just start their respective Backtracking solver
	 */
	public boolean BasicBacktrackingStarter(Matrix m){
		return solve(m);
	}
	
	public boolean BestBacktrackingStarter(Matrix m){
		return solveBestBack(m);
	}
	
	/*
	 * Basic Backtracking Solver
	 * It finds a Point with a value of 0 and tries to give it a value between 1 to N, if it fails then it tries the next number
	 */
	public void clearMatrix(Matrix m){
		for(int i=0; i<size; i++){
	    	for(int j=0; j<size; j++){
	    		m.getPoint(i, j).setValue(0);
	    	}
	    }
	}
	public boolean solve(Matrix m){
		counter1Basic++;
		Point blank = findBlank(m);
		if (blank != null){
			for(int i=1; i < size+1; i++){
				if (isValid(blank, m, i)){
					blank.setValue(i);
					
					if (solve(m)){
						return true;
					}
					
					blank.setValue(0);
				}
			}
			return false;
		}else {
			return true;
		}
	}
	
	//Checks if the value we are trying for a Point is valid
	public boolean isValid(Point p, Matrix m, int val){
		return checkRow(p, m, val)
		& checkCol(p, m, val)
		& checkCage(p.getCage(), m, val);
		
	}

	//Checks if the value passed is already in the row
	public boolean checkRow(Point p, Matrix m, int val){
		for(int i=0; i < size; i++){
			if (p.getY() == i) continue;
			if (m.getPoint(p.getX(), i).getValue() == val){
				return false;
			}
		}
		return true;
	}

	//Checks if the value passed is already in the column
	public boolean checkCol(Point p, Matrix m, int val){
		for(int i=0; i < size; i++){
			if (p.getX() == i) continue;
			if (m.getPoint(i, p.getY()).getValue() == val){
				return false;
			}
		}
		return true;
	}

	//Checks if the value passed satisfies the Cage conditions
	public boolean checkCage(Cage c, Matrix m, int val){
		if (c.getType() == '+'){ //ADD OPERATION
			if (c.getTotalVal() + val <= c.getDesValue()){
				return true;
			}
		} 
		else if (c.getType() == '-'){  //MINUS OPERATION
			int val1 = c.getPoints().get(0).getValue();
			int val2 = c.getPoints().get(1).getValue();
			
			if (val1 == 0){
				if (val2 == 0) return true;
				else if (val2 - val == c.getDesValue()) return true;
				else if (val - val2 == c.getDesValue()) return true;
			}
			else if (val1 - val == c.getDesValue()) return true;
			else if (val - val1 == c.getDesValue()) return true;
		}
		else if (c.getType() == '*'){ //TIMES OPERATION
			if (c.getTotalVal() * val <= c.getDesValue()){
				return true;
			}
		}
		else if (c.getType() == '/'){ //DIVISION OPERATION
			int val1 = c.getPoints().get(0).getValue();
			int val2 = c.getPoints().get(1).getValue();
			
			if (val1 == 0){
				if (val2 == 0) return true;
				else if (val2/val == c.getDesValue()) return true;
				else if (val/val2 == c.getDesValue()) return true;
			}
			else if (val1/val == c.getDesValue()) return true;
			else if (val/val1 == c.getDesValue()) return true;
		}
		//EQUAL OPERATION
		else if(c.getType() == '='){
			if(val == c.getDesValue()) return true;
		}
		return false;
	}

	

	//Finds a Point with a value of 0 (no value)
	public Point findBlank(Matrix m){
		for(int i=0; i<size; i++){
	    	for(int j=0; j<size; j++){
	    		if (m.getPoint(i, j).getValue() == 0){
	    			return m.getPoint(i, j);
	    		}
	    	}
	    }
		return null;
	}
	
	//Recursive function that gets a 
	//
	public boolean solveBestBack(Matrix m){
		counter1Best++;
		Point mrv = findMRV(m);  //Find Point with the lowest number of possible values
		
		if (mrv != null){
			ArrayList<Integer> cageValues = new ArrayList<Integer>();
			if (mrv.getCage().getPoints().size() <= 2) {
				cageValues = mrv.getCagePossVals();
			} else {
				cageValues = mrv.getPossVals();
			}
			
			ArrayList<Integer> ogCageValues = new ArrayList<Integer>(cageValues);
			
				for (int k : ogCageValues){
					if (isValid(mrv, m, k)){
						mrv.setValue(k);
						ArrayList<Integer> ogMRV = new ArrayList<Integer>(ogCageValues);
						mrv.clearMRV();
						
						updateMRV(mrv, m, -1, mrv.getValue());
						
						if(solveBestBack(m)){
							return true;
						}
						
						//RESET to original						
						if (mrv.getCage().getPoints().size() <= 2) mrv.setCageMRV(ogMRV);
						else mrv.setMRV(ogMRV);
						
						updateMRV(mrv, m, 1, mrv.getValue());
						mrv.setValue(0);
					}
				}
			return false;
		}else {
			return true;
		}
	}
	
	//Find Point with fewest possible values (Minimum Remaining Values)
	public Point findMRV(Matrix m){
		int min = Integer.MAX_VALUE;
		Point p3 = null;
		for(Point[] p : m.getPoints()){
			for(Point p2 : p){
				
				int cageMRV = p2.getMRV();
				
				if (cageMRV == 0 && p2.getValue() != 0){ //otherwise use regular poss_val mrvs
					continue;
				}
				if (cageMRV == 0){
					cageMRV = m.getSize();
				}
				if (min > cageMRV){
					min = cageMRV;
					p3 = p2;
				}
			}
		}
		return p3;
	}
	//x is 1 or -1
	public void updateMRV(Point p, Matrix m, int x, int val) {
		//updates the mrv values for each of the points in the same row, column, and cage as the newly altered point
		
		//update Row MRVs
		for (int i=0;i<size;i++){
			if (p.getY() == i) continue;
			
			//If we are trying to add back to the List and the value is already in that column then we skip this Point.
			if (x==1 && !checkCol(m.getPoint(p.getX(), i), m, val)){
				continue;
			}
			m.getPoint(p.getX(), i).updateMRV(x, val);
			
		}
		
		//update Column MRVs
		for (int i=0;i<size;i++){
			if (p.getX() == i) continue;
			if (x==1 && !checkRow(m.getPoint(i, p.getY()), m, val)){
				continue;
			}
			m.getPoint(i,  p.getY()).updateMRV(x, val);
		}
	}

	//Checks if the val passed satisfies the cage conditions
	public boolean checkCageLocal(Cage c, Matrix m){ 
		if (c.getType() == '+'){ //ADD OPERATION
			if (c.getTotalVal()  == c.getDesValue()){  
				return true;
			}
		} 
		else if (c.getType() == '-'){  //MINUS OPERATION  
			
			int val1 = c.getPoints().get(0).getValue();
			int val2 = c.getPoints().get(1).getValue();
			
			if(Math.abs(val1 - val2) == c.getDesValue()) {
				return true;
			}
		}
		else if (c.getType() == '*'){ //TIMES OPERATION
			if (c.getTotalVal() == c.getDesValue()){  
				return true;
			}
		}
		else if (c.getType() == '/'){ //DIVISION OPERATION  
			int val1 = c.getPoints().get(0).getValue();
			int val2 = c.getPoints().get(1).getValue();
			
			if (Math.max(val1, val2)/Math.min(val1, val2) == c.getDesValue()) {
				return true;
			}

		}
		//EQUAL OPERATION
		else if(c.getType() == '='){
			if(c.getPoints().get(0).getValue() == c.getDesValue()) return true;
		}
		return false;
	}
	//responsible for running the search using up to N^5 random matrices passed through up to 4*N times
	public boolean LocalSearchStarter(Matrix m) {
		for (int numUniqueRandomMatrices = 0; numUniqueRandomMatrices < Math.pow(m.getSize(), 5); numUniqueRandomMatrices++) {
			m = populateMatrix(m);
			counterForLocalSolution=0;
			for (int numPasses = 0; numPasses < m.getSize()*4; numPasses++) { // numPasses represents the number of 'passes' through the currently selected matrix
				if (solveLocalSearch(m)) {
					return true;
				}
			}
		}
		System.out.println("A solution was not reached");
		return false;
	}
		
	public Matrix populateMatrix(Matrix m) {
		ArrayList<Integer> basicArrayList = new ArrayList<Integer>();
		
		for(int i = 0; i < m.getSize(); i++) { //Populates an array list with n values from 1 to n
			basicArrayList.add(i+1);
		}	
			
		for(int i=0; i < m.getSize(); i++){ //create the matrix with rows containing one of each value from 1 to n in a randomized order and randomized columns
			Collections.shuffle(basicArrayList); //randomize basicArray
			for(int j=0; j < m.getSize(); j++){
				m.getPoint(i, j).setValue(basicArrayList.get(j));
			}
		}
			
		for (int i=0; i<m.getSize(); i++) { // initialize constraints on the matrix
			for (int j=0; j<m.getSize(); j++) {
				updateConstraints(m.getPoint(i, j),m);
			}
		}
		return m;
	}
		
	// returns true if the puzzle is solved, false otherwise.  All swapping is done within rows only
	public boolean solveLocalSearch(Matrix m) {
		for (int q=0; q<m.getSize(); q++) { // q: rows
			for(int i=0; i < m.getSize(); i++) { // i: index of left point to swap
				for(int j=i+1; j < m.getSize(); j++) { // j: index of right point to swap
					trySwap(m.getPoint(q, i), m.getPoint(q, j), m); // if swapping the values are successfully swapped due to minimizing the constraints
					if (m.updateTotalNumConstraints() == 0) {
						System.out.println("The Local Search returned a solved KenKen!");
						return true;						
					}
				}
			}
		}
		return false;
	}
		
	public boolean trySwap(Point p1, Point p2, Matrix m) {  // attempts the swap and returns whether or not the two numbers were swapped. 
		int oldTotalConstraints = p1.getNumConstraints() + p2.getNumConstraints();
		
		// actually swap the values
		int tempValue = p1.getValue();
		p1.setValue(p2.getValue());
		p2.setValue(tempValue);
			
		updateConstraints(p1, m);
		updateConstraints(p2, m);
			
		int newTotalConstraints = p1.getNumConstraints() + p2.getNumConstraints();
			
		if (newTotalConstraints <= oldTotalConstraints) {  // only finalizes the swap if there is a net decrease in the number of total constraints b/w the two points, or 30 percent of the time when the number of total constraints stays the same.
			counterForLocalTotal++;
			counterForLocalSolution++;
			for (int i = 0; i < m.getSize(); i++) {
				//update all of the numConstraint values from p1 and p2's columns.  This updates the numConstraints for p1 and p2 again at a marginal cost.  maybe implement if logic here if time permits
				updateConstraints(m.getPoint(i, p1.getY()),m);
				updateConstraints(m.getPoint(i, p2.getY()),m);
			}
			// update numConstraint values for both cages containing p1 and p2.  again likely some overlap here from previous column updateConstraint but doesnt affect value, just not particularly efficient
			for (int i=0; i < p1.getCage().getPoints().size(); i++) {
				updateConstraints(p1.getCage().getPoints().get(i), m);
			}
			for (int i=0; i < p2.getCage().getPoints().size(); i++) {
				updateConstraints(p2.getCage().getPoints().get(i), m);
			}
			return true;
		} else {  // if swapping cages doesnt reduce the number of constraints, reset values and numConstraints to original amounts
			p2.setValue(p1.getValue());
			p1.setValue(tempValue);
			updateConstraints(p1, m);
			updateConstraints(p2, m);
			return false;
		}
	}		
	
	public void updateConstraints(Point p1, Matrix m) { // pass in a point and the matrix of points, obtain new value of num constraint for a given point
		int numPointConstraints = 0;
		if (!checkCol(p1,m,p1.getValue())){
			numPointConstraints++;
		} 
		
		if (!checkCageLocal(p1.getCage(), m)){
			numPointConstraints++;
		}
		
		p1.setNumConstraints(numPointConstraints);
	}
}