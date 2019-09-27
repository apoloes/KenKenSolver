import java.util.ArrayList;

public class Point {
	private int x;
	private int y;
	private char cage_name;
	private int value;
	private Cage cage;
	private int m_size;
	private ArrayList<Integer> poss_vals = new ArrayList<Integer>();
	private ArrayList<Integer> cage_poss_vals = new ArrayList<Integer>();
	private int num_constraints;
	
	//A point is given an x and y coordinate, the cage it is in, and the size of the matrix
	public Point(int x_coord, int y_coord, char cage_n, int matrix_size){
		x = x_coord;
		y = y_coord;
		cage_name = cage_n;
		
		m_size = matrix_size;
		
		//Populate the list of possible values with values 1 to size of matrix+1
		for(int i=1; i<m_size+1; i++){
			poss_vals.add(i);
		}
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public Cage getCage(){
		return cage;
	}
	
	public char getCageName(){
		return cage_name;
	}
	
	public int getValue(){
		return value;
	}
	
	public void setValue(int v){
		value = v;
		
	}
	public void setCage(Cage c){
		cage = c;
	}
	
	//This sets the Point's possible values by getting them from the Cage
	public void setCagePossVals(){
		ArrayList<Integer> cage_poss_vals_copy = new ArrayList<Integer>(cage.getListValues());
		cage_poss_vals = cage_poss_vals_copy;
	}
	public void setMRV(ArrayList<Integer> v){
		poss_vals = v;
	}
	public void setCageMRV(ArrayList<Integer> v){
		cage_poss_vals = v;
	}
	public ArrayList<Integer> getPossVals(){
		return poss_vals;
	}
	
	public ArrayList<Integer> getCagePossVals(){
		return cage_poss_vals;
	}
	
	//Adds or removes values from the ArrayList of possible values (cage_poss_vals or poss_vals depending on Cage size)
	public void updateMRV(int i, int val){
		
		//If the Cage has 2 or fewer points then this runs
		if (cage.getPoints().size() <= 2){
			//If the size of the ArrayList is 0 and you're trying to add a value to it and the Point value is 0, then return
			if (cage_poss_vals.size() == 0 && i == 1 && value != 0){
				return;
			} else if (cage_poss_vals.size() < 0 || cage_poss_vals.size() > m_size){
				return;
			} else{
				if (i==-1){ // remove value from Point's cage_poss_vals
					if (cage_poss_vals.contains(val)){
						cage_poss_vals.remove((Object) val);
					}
				}else{ // add value to a Point's cage_poss_vals
					if (!cage_poss_vals.contains(val)){
						cage_poss_vals.add(val);
					}
				}
			}
		} else{
			//This is an edge case where the poss_vals are empty
			//and we are trying to add something to it and the Point's value is not 0, so it shouldnt add something
			if (poss_vals.size() == 0 && i == 1 && value != 0){
				return;
			}else if (poss_vals.size() < 0 || poss_vals.size() > m_size){
				return;
			} else{
				if (i==-1){ //remove value from Point's poss_vals
					if(poss_vals.contains(val)){
						poss_vals.remove((Object) val);
					}
				}else{ //Check if the value is there, if it isnt then add the value to poss_vals
					if(!poss_vals.contains(val)){
						poss_vals.add(val);
					}
				}
			}
		}
	
	}
	
	/*
	 * Returns the corresponding ArrayList based on Cage size
	 */
	public int getMRV(){
		if(cage.getPoints().size() <= 2){
			return cage_poss_vals.size();
		}else{
			return poss_vals.size();
		}
	}
	
	/*
	 * Clears the corresponding ArrayList based on Cage size
	 */
	public void clearMRV(){
		if(cage.getPoints().size() <= 2){
			cage_poss_vals.clear();
		}else{
			poss_vals.clear();
		}
	}
	
	//Returns the number of constraints violated when the Point has its current value
	//Only used for Local Search
	public int getNumConstraints() {
		return num_constraints;
	}
	public void setNumConstraints(int i) {
		num_constraints = i;
	}
}
