import java.util.ArrayList;

public class Matrix {
	private int size;
	private Point[][] points = new Point[size][size];
	private ArrayList<Cage> cages = new ArrayList<Cage>();
	private int totalNumConstraints;
	
	//A matrix is given its size and also contains a 2D array of all Points and an ArrayList of all its cages
	public Matrix(int s){
		points = new Point[s][s];
		size = s;
	}
	
	public void addPoint(Point p){
		points[p.getX()][p.getY()] = p;
	}
	
	public void addCage(Cage c){
		cages.add(c);
	}
	
	//Prints the "solution" to the KenKen problem
	public String toString(){
		String return_text = "";
		for(int i=0; i<size; i++){
	    	for(int j=0; j<size; j++){
	    		return_text += points[i][j].getValue();
	    	}
	    	return_text += "\n";
	    }
		return return_text;
	}
	
	public Point getPoint(int x, int y){
		return points[x][y];
	}
	
	public Point[][] getPoints(){
		return points;
	}
	
	public ArrayList<Cage> getCageList(){
		return cages;
	}
	
	public int getSize() {
		return size;
	}
	
	// updates the total sum of each points constraint value across the entire board. returns the total sum
	public int updateTotalNumConstraints() {   
		totalNumConstraints = 0;
		
		for (int i=0; i<size; i++) {
			for (int j=0; j<size; j++) {
				totalNumConstraints += points[i][j].getNumConstraints();
			}
		}
		return totalNumConstraints;
	}
}
