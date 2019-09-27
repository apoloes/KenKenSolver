import java.util.ArrayList;

public class Cage {

	private int desired_value;
	private char type;
	private char name;
	private ArrayList<Point> my_points = new ArrayList<>();
	private ArrayList<Integer> vals_list = new ArrayList<>();
	private int m_size;
	
	//A Cage is given its name, its desired value, the type of operator it has, and the matrix size
	public Cage(char name, int d_value, char type, int matrix_size){
		this.name = name;
		this.desired_value = d_value;
		
		//Edge case where the Cage has no type (no operation) so we set the type to '=' for clarity/functionality
		if (d_value == Character.getNumericValue(type)) {
			this.type = '=';
		} else {
			this.type=type;
		}
		
		m_size = matrix_size;
	}

	public char getName(){
		return name;
	}
	public int getDesValue(){
		return desired_value;
	}
	public char getType(){
		return type;
	}
	
	//Adds a Point to the Cage's list of points
	public void setPoints(Point p){
		my_points.add(p);
	}
	
	public ArrayList<Point> getPoints(){
		return my_points;
	}
	
	//Returns the total value of the cage when called; used to check whether a value can go into that cage
	public int getTotalVal(){
		int total = 0;
		if (type == '+'){
			for (Point p : my_points){
				total += p.getValue();
			}
		}else if (type == '*'){
			for (Point p : my_points){
				if (total == 0){
					total = p.getValue();
				} else if (p.getValue() != 0) {
					total *= p.getValue();
				}
			}
		}
		return total;
	}
	
	/*
	 * Sets the possible values a Point in this Cage can have
	 * If the num of points in the Cage is > 2 then we have the list have every possible value 1 to N
	 */
	public void setListValues(){
		int num_points = my_points.size();
		
		if(num_points == 1){
			vals_list.add(desired_value);
		}
		
		if (num_points == 2){
			if (type=='+'){
				for(int i=1; i< m_size+1; i++){
					for(int j=1; j< m_size+1; j++){
						if (i+j == desired_value && i!=j){	//If i + j satisfy the constraint then add them to the list
							if (!vals_list.contains(i))	vals_list.add(i);
							if (!vals_list.contains(j)) vals_list.add(j);
						}
					}
				}
			} else if(type=='*'){
				for(int i=1; i< m_size+1; i++){
					for(int j=1; j< m_size+1; j++){
						if (i*j == desired_value && i!=j){	//If i * j satisfy the constraint then add them to the list
							if (!vals_list.contains(i))	vals_list.add(i);
							if (!vals_list.contains(j)) vals_list.add(j);
						}
					}
				}
			} else if(type=='-'){
				for(int i=1; i< m_size+1; i++){
					for(int j=1; j< m_size+1; j++){
						if (Math.abs(i-j) == desired_value && i!=j){	//If i + j satisfy the constraint then add them to the list
							if (!vals_list.contains(i))	vals_list.add(i);
							if (!vals_list.contains(j)) vals_list.add(j);
						}
					}
				}
			} else if(type=='/'){
				for(int i=1; i< m_size+1; i++){
					for(int j=1; j< m_size+1; j++){
						if (Math.max(i, j)/Math.min(i, j) == desired_value && i!=j && Math.max(i, j) % Math.min(i, j) == 0){	//If i + j satisfy the constraint then add them to the list
							if (!vals_list.contains(i))	vals_list.add(i);
							if (!vals_list.contains(j)) vals_list.add(j);
						}
					}
				}
			}
		} else if (num_points > 2){
			for(int i=1; i<m_size+1; i++){
				vals_list.add(i);
			}
		}

	}
	public ArrayList<Integer> getListValues(){
		return vals_list;
	}
}
