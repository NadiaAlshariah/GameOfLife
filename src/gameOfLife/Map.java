package gameOfLife;
import java.text.DecimalFormat;
import java.util.Scanner;

public class Map {
	DecimalFormat df = new DecimalFormat("00"); // so every number is made of 2 digits
	Step [][] map = new Step[7][10]; // the array of the map
	Step currentStep; // refer to the current step of the player
	int [] a_stopPoint = {1,5,24,55,65}; // array for the stop points numbers
	int [] a_payPoints = {4,6,10,50,15,25,35,45,13,47,48,52,57,70}; // array for the pay points that the player collects a salary after passing them
	
	// to be chosen and put in the player object
	public boolean collegeTrigger = false; 
	public boolean marriageTrigger = false;
	public boolean retiredTrigger = false;
	public boolean payTrigger = false;
	public boolean jobTrigger = false;
	
	public Map() {
		// temp values to create the array using loop
		int number = 0;  // initial value is 0 because it will increased by the beggining of the loop    
		String type;		
		String direction;
		
		// arrays for the cells classified accourding to its direction
		int [] a_right = {2,3,4,6,7,8,9,15,16,17,18,31,34,35,36,43,44,56,57,58,59,61,62,63,64};
		int [] a_left = {14,25,26,27,28,42,47,46,50,49,54,53,70,69,68,67,66};
		int [] a_top = {12,22,32,38,48,52};
		
		
		// arrays for the cells classified accourding to its type
		int [] a_education = {11,12,21,22,31,32};
		int [] a_work = {2,3,4};
		int [] a_marriage = {6,7,8,9,10,20,30,40,50};
		int [] a_single = {15,16,17,18,19,29,39};
		int [] a_children = {14,13,23,33,43};
		int [] a_invesment = {34,35,36,37,47};
		int [] a_risk = {54,53,52,42,41,51,61,62,63,64};
		int [] a_safe = {56,57,58,59,60,70,69,68,67,66};
	
		// loop to initiliaze the step objects of the map array
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 10; j++) {
				number ++; // to fill the temp variable with the current cell number
				
				// to fill the temp variable with the current cell direction
				if (arrayContains(a_stopPoint, number)) {
					direction = ""; // will be filled later because this is player choice
					type = "stop";
					}
				else if (arrayContains(a_right, number)) 	
					direction = "right";				// using user defined method to find if this cell number
				else if (arrayContains(a_left, number)) // exist in the pre-defined array for the diractions
					direction = "left";					// to determin its direction
				else if (arrayContains(a_top, number))
					direction = "top";
				else
					direction = "bottom";
					
				// to fill the temp variable with the current cell type
				if (arrayContains(a_work, number))
					type = "work";
				else if (arrayContains(a_education, number))
					type = "education";
				else if (arrayContains(a_marriage, number))
					type = "marriage";
				else if (arrayContains(a_single, number))
					type = "single";
				else if (arrayContains(a_children, number))
					type = "children";
				else if (arrayContains(a_invesment, number))
					type = "invesment";
				else if (arrayContains(a_risk, number))
					type = "risk";
				else if (arrayContains(a_safe, number))
					type = "safe";
				else
					type = "normal";
				
				map [i][j] = new Step(number, type, direction, i, j); // creating a new cell with the temp variables
			}
		}
		
		currentStep = map[0][0]; // initialize the current step as the start step = 1
	}
	
	public void print() {
		// print method to print the map
		System.out.println("---------------------------------------------------");
		for (int i = 0; i<7; i++)
		{
			for (int j = 0; j<10; j++)
				if (i == currentStep.row && j == currentStep.col) // to print the players location
					System.out.print("| "+ "㋡"+ " ");
				else if (i == 6 && j == 4)
					System.out.print("| "+ "██ "); // to print the ending point 
				else if ((i == 0 && j == 4)||(i == 0 && j == 1)||(i == 2 && j == 3)||(i == 5 && j == 4)) {
					System.out.print("| "+ "   ");
				}
				else
					System.out.print("| "+ df.format(map[i][j].number)+ " "); // to print every cell number
			System.out.println("|\n---------------------------------------------------");
		}
	}
	
	public void move(int steps) {

		for (int i = 0; i <steps; i++) {
			
			// first stop
			if (currentStep.row == 0 && currentStep.col == 0) 
				firstStop();
			
			// to move using direction string in step object
			if (currentStep.direction.equals("right")) // if dirction is right the column + 1
				currentStep = map[currentStep.row][currentStep.col + 1]; 
			
			else if (currentStep.direction.equals("left")) // if dirction is left the column - 1
				currentStep = map[currentStep.row][currentStep.col - 1];
			
			else if (currentStep.direction.equals("top"))  // if dirction is top the row - 1
				currentStep = map[currentStep.row - 1][currentStep.col];
			
			else 										   // if dirction is bottom the row + 1
				currentStep = map[currentStep.row + 1][currentStep.col];
			
			// to check the new stop point action
			if (arrayContains(a_payPoints, currentStep.number)) {
				payTrigger = true;
			}
			else if (currentStep.row == 0 && currentStep.col == 1) {
				jobTrigger = true;
				break;
			}
			else if (currentStep.row == 0 && currentStep.col == 4) {
				secondStop();
				break;
			}
			else if (currentStep.row == 2 && currentStep.col == 3) {
				thirdStop();
				break;
			}
			else if (currentStep.row == 5 && currentStep.col == 4) {
				fourthStop();
				break;
			}
			else if (currentStep.row == 6 && currentStep.col == 4) {
				retiredTrigger = true;
				break;
			}
		}
	}
	
	void firstStop(){
		String choice = Main.choiceChecker("College", "Going to College: (Pay 100K to level up your education"
				+ " You will get better jobs and 1 knowladge point)" , "Career", "Starting a Career: (get a job"
						+ " now and earn your Salary Sooner!)");
		
		if (choice.equals("a")){
			currentStep.direction = "bottom";
			collegeTrigger = true;
		}
		else if (choice.equals("b")){
			currentStep.direction = "right";
		}

	}
	
	void secondStop(){
		String choice = Main.choiceChecker("marriage", "Get Married: (Start your family"
				+ " to get happiness points!)\nHappiness + 1 point" , "staying single", "Stay single: (Stay"
						+ " on your path to get knowladge points!)");
		
		if (choice.equals("a")){
			currentStep.direction = "right";
			marriageTrigger = true;
		}
		else if (choice.equals("b"))
			currentStep.direction = "bottom";
	}
	
	void thirdStop(){
		
		String choice = Main.choiceChecker("Family", "Family: (grow your family"
				+ " to get happiness points!)" , "investment", "investment:"
						+ " (invest in your future to get wealth points!)");
		
		if (choice.equals("a")){
			currentStep.direction = "top";
		}
		else if (choice.equals("b"))
			currentStep.direction = "bottom";
	}
	
	void fourthStop(){
		
		String choice = Main.choiceChecker("safe", "Safe path: (Play safe and stick to your"
				+ " path)", "risk", "risky path: (win or lose BIG! let fate decide)");
		
		if (choice.equals("a"))
			currentStep.direction = "right";
		else if (choice.equals("b"))
			currentStep.direction = "left";
				
	}
	
	static boolean arrayContains(int [] array, int value) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == value)
				return true;
		}
		return false;
	}
	
}
