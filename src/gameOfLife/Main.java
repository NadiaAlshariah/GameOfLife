package gameOfLife;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) {		
		Scanner read = new Scanner(System.in);
		int playersNum = 0; //initial value to be changed
		
		// do-while loop to ask the user for players count, the while part repeats
		// the question in case the user inputs invalid number which is >4 or <2
		do {
			System.out.println("Note: The players number should be between 2-4");
			System.out.print("Enter the number of Player: ");
			playersNum = read.nextInt();
		}
		while ((playersNum > 4) || (playersNum < 2));
		
		// i made the game play in methods based on the players count
		// to reduce the amount of coding lines because there will be redundancy 
		
		if (playersNum == 4)
			gamePlay4();  // the number next to the method name refers to num of players
		else if (playersNum == 3)
			gamePlay3();
		else              // in this case the players num surely == 2
			gamePlay2();
	
	}
	
	public static String instuctions = "Welcome to The Game of Life!"+
	  "\nRoll the dice to begin your journey, Choose college or work, then navigate various events"
	+ "\nlike marriage and investments. Earn the highest score by accumulating Money,"
	+ "\nattributes like (wealth, happiness, and knowledge), as well as managing loans and children."
	+ "\nPlan wisely to win!"
	+ "\nMade by: Nadia Alshariah as CS310 project-1";
	
	public static String Rules = "- the game has stop points that will change the path that the player follows \n"
			+ "- the stop points represented as empty steps\n"
			+ "- the first stop point is the starting point where the player has to choose a college or work\n"
			+ "- the other points can be either payday, family, house, tax, spin the wheel or reguler action cards.\n"
			+ "- when a player passes a pay day he/she gain the salary whether the player stopped at it or not\n"
			+ "- a player needs to stop at the other-than-payday points to activate them including taxes points\n"
			+ "- spin the wheel point allow the player to get a card randomly\r\n"
			+ "- the player can own multipal houses but he/she has to sell them at the end of the game or before that\n"
			+ "- if you sell your houses before retirement you can get up to 3 prices, after retirement you get 1 only.\n"
			+ "- in the end the score is calculeted as following:\r\n"
			+ "1 - every 1K of money = 1 point\r\n"
			+ "2 - every attribute point = 20 points\r\n"
			+ "3 - the player with the crown (most of each attribute) gets extra 50 points\n"
			+ "4- every child = 50 points \n"
			+ "5- every loan - 100 points"
			+ "\n\n- the player with the highest score WINS!";
	
	
	static void gamePlay2() { // this is the method that manages the 2 player game
		
		// the start of the name assigning to players process
		Scanner nameReader = new Scanner(System.in);
		String name; // temporary variable to save the name of the player
		
		// assigning the first player name
		System.out.print("Player 1, Enter your Name: ");
		name = nameReader.nextLine();
		Player player1 = new Player(name);
		
		// assigning the second player name
		System.out.print("Player 2, Enter your Name: ");
		name = nameReader.nextLine();
		Player player2 = new Player(name);
		
		// the end of the name assigning to players process
		
		// prints the instructions, which is the 1st requirement of the project
		System.out.println("\n" + instuctions + "\n");
		wait1s(); // its a user defined function to wait for 1 second
		
		// using user defined method for choices that returns a string that is either a or b
		// choiceChecker ("shortend choice A", "choice A", "shortened Choice B", "Choice B");
		String c = choiceChecker("Show rules", "show the rules of the game", "Skip rules", "Skip the rules (if you already know them)");
		if (c.equals("a")) {
			System.out.println(Rules);
		}
		wait3s();
		enterPressed("\nTo start the game");
		
		
		int diceRoll; // a temp variable to save the diceRoll value
		
		// this loop is the main game play, as long as there's an active player who
		// has not finish the game yet, the game will keep going
		while (player1.retired == false || player2.retired == false) {		
				
			System.out.println("it's "+ player1.name + "'s Turn!"); // the beginning of p1 turn
			wait1s();		 // its a user defined function to wait for 1 second	
			
			// prints the stats of the player
			System.out.println(player1);
			wait1s();
			
			if (player1.retired == false){ // if player is not retired
				System.out.println("your current postion: ");
				player1.playerMap.print(); // prints the map of the player
				System.out.println();
				
				enterPressed("To roll the dice"); // self defined func. to check if the player pressed enter
				
				// messege printed to inform the player
				System.out.print("Dice rolling ");  
				for (int t = 0; t < 3; t ++) {
					wait1s();
					System.out.print(".");
				}	
				System.out.println();
				
				// rolling the dice using a static func. that returns an int
				diceRoll = rollDice();
				System.out.println("\nYou got "+diceRoll+"!\n");
				wait1s();
				
				player1.playerMap.move(diceRoll); // moving the player with the dice amount
				player1.currentPostion = player1.playerMap.currentStep.number; // saving the player Postion
				
				// ACTIONS
				
				// college action
				if (player1.playerMap.collegeTrigger) {  
					collegeAction(player1);
					player1.playerMap.collegeTrigger = false;
				}
				
				// job selection action
				if (player1.playerMap.jobTrigger) {
					if (player1.college) {
						System.out.println("Congrats on your graduation!!!");
						player1.knowAdd(1);
					}
					System.out.println("\nTime to choose your Career!");
					jobSelection(player1);
					
					player1.playerMap.jobTrigger = false;
				}
				
				// marriage action
				if(player1.playerMap.marriageTrigger) { 
					marriageAction(player1, player2);
					player1.playerMap.marriageTrigger = false;
				}
				
				// pay day action: when the players pass a pay step they get
				// their salary wheather they stopped on it or not		
				if (player1.playerMap.payTrigger) {
					payAction(player1);
					player1.playerMap.payTrigger = false;
				}
				
				// to activate the action of the current step:
				action(player1, player1.currentPostion); // regular actions
				
				// if player reached retirement:
				if(player1.playerMap.retiredTrigger) {
					reachRetirement(player1);
				}
			}
			
			else if (player1.retired) {
				System.out.println("\n((You get to spin the retirement wheel till the end of game))\n");
				retiredWheelSpin(player1);
			}
			
			maxAttribute2(player1, player2);
			
			// print the player stats at the end of the turn
			System.out.println(player1);
			wait3s(); 
			
			// -----------------------------------------------------------------
			System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||\n");
//				
			System.out.println("it's "+ player2.name + "'s Turn!"); // the beginning of p2 turn
			wait1s();		 // its a user defined function to wait for 1 second	
			
			// prints the stats of the player
			System.out.println(player2);
			wait1s();
			
			if (player2.retired == false){ // if player is not retired
				System.out.println("your current postion: ");
				player2.playerMap.print(); // prints the map of the player
				System.out.println();
				
				enterPressed("To roll the dice"); // self defined func. to check if the player pressed enter
				
				// messege printed to inform the player
				System.out.print("Dice rolling ");  
				for (int t = 0; t < 3; t ++) {
					wait1s();
					System.out.print(".");
				}	
				System.out.println();
				
				// rolling the dice using a static func. that returns an int
				diceRoll = rollDice();
				System.out.println("\nYou got "+diceRoll+"!\n");
				wait1s();
				
				player2.playerMap.move(diceRoll); // moving the player with the dice amount
				player2.currentPostion = player2.playerMap.currentStep.number; // saving the player Postion
				
				// ACTIONS
				
				// college action
				if (player2.playerMap.collegeTrigger) {  
					collegeAction(player2);
					player2.playerMap.collegeTrigger = false;
				}
				
				// job selection action
				if (player2.playerMap.jobTrigger) {
					if (player2.college) {
						System.out.println("Congrats on your graduation!!!");
						player2.knowAdd(1);
					}
					System.out.println("\nTime to choose your Career!");
					jobSelection(player2);
					
					player2.playerMap.jobTrigger = false;
				}
				
				// marriage action
				if(player2.playerMap.marriageTrigger) { 
					marriageAction(player2, player1);
					player2.playerMap.marriageTrigger = false;
				}
				
				// pay day action: when the players pass a pay step they get
				// their salary wheather they stopped on it or not		
				if (player2.playerMap.payTrigger) {
					payAction(player2);
					player2.playerMap.payTrigger = false;
				}
				
				// to activate the action of the current step:
				action(player2, player2.currentPostion); // regular actions
				
				// if player reached retirement:
				if(player2.playerMap.retiredTrigger) {
					reachRetirement(player2);
				}
			}
			
			else if (player2.retired) {
				System.out.println("\n((You get to spin the retirement wheel till the end of game))\n");
				retiredWheelSpin(player2);
			}
			
			maxAttribute2(player1, player2);
			
			// print the player stats at the end of the turn
			System.out.println(player2);
			wait3s();
			
			System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||\n");
		}
	endGame2(player1, player2);
	}
	
	static void gamePlay3() { // this is the method that manages the 2 player game
		
		// the start of the name assigning to players process
		Scanner nameReader = new Scanner(System.in);
		String name; // temporary variable to save the name of the player
		
		// assigning the first player name
		System.out.print("Player 1, Enter your Name: ");
		name = nameReader.nextLine();
		Player player1 = new Player(name);
		
		// assigning the second player name
		System.out.print("Player 2, Enter your Name: ");
		name = nameReader.nextLine();
		Player player2 = new Player(name);
		
		// assigning the thirdd player name
		System.out.print("Player 3, Enter your Name: ");
		name = nameReader.nextLine();
		Player player3 = new Player(name);
		
		
		// the end of the name assigning to players process
		
		// prints the instructions, which is the 1st requirement of the project
		System.out.println("\n" + instuctions + "\n");
		wait1s(); // its a user defined function to wait for 1 second
		
		// using user defined method for choices that returns a string that is either a or b
		// choiceChecker ("shortend choice A", "choice A", "shortened Choice B", "Choice B");
		String c = choiceChecker("Show rules", "show the rules of the game", "Skip rules", "Skip the rules (if you already know them)");
		if (c.equals("a")) {
			System.out.println(Rules);
		}
		wait3s();
		enterPressed("\nTo start the game");
		
		
		int diceRoll; // a temp variable to save the diceRoll value
		
		// this loop is the main game play, as long as there's an active player who
		// has not finish the game yet, the game will keep going
		while (player1.retired == false || player2.retired == false || player3.retired == false) {		
				
			System.out.println("it's "+ player1.name + "'s Turn!"); // the beginning of p1 turn
			wait1s();		 // its a user defined function to wait for 1 second	
			
			// prints the stats of the player
			System.out.println(player1);
			wait1s();
			
			if (player1.retired == false){ // if player is not retired
				System.out.println("your current postion: ");
				player1.playerMap.print(); // prints the map of the player
				System.out.println();
				
				enterPressed("To roll the dice"); // self defined func. to check if the player pressed enter
				
				// messege printed to inform the player
				System.out.print("Dice rolling ");  
				for (int t = 0; t < 3; t ++) {
					wait1s();
					System.out.print(".");
				}	
				System.out.println();
				
				// rolling the dice using a static func. that returns an int
				diceRoll = rollDice();
				System.out.println("\nYou got "+diceRoll+"!\n");
				wait1s();
				
				player1.playerMap.move(diceRoll); // moving the player with the dice amount
				player1.currentPostion = player1.playerMap.currentStep.number; // saving the player Postion
				
				// ACTIONS
				
				// college action
				if (player1.playerMap.collegeTrigger) {  
					collegeAction(player1);
					player1.playerMap.collegeTrigger = false;
				}
				
				// job selection action
				if (player1.playerMap.jobTrigger) {
					if (player1.college) {
						System.out.println("Congrats on your graduation!!!");
						player1.knowAdd(1);
					}
					System.out.println("\nTime to choose your Career!");
					jobSelection(player1);
					
					player1.playerMap.jobTrigger = false;
				}
				
				// marriage action
				if(player1.playerMap.marriageTrigger) { 
					marrigeAction(player1, player2, player3);
					player1.playerMap.marriageTrigger = false;
				}
				
				// pay day action: when the players pass a pay step they get
				// their salary wheather they stopped on it or not		
				if (player1.playerMap.payTrigger) {
					payAction(player1);
					player1.playerMap.payTrigger = false;
				}
				
				// to activate the action of the current step:
				action(player1, player1.currentPostion); // regular actions
				
				// if player reached retirement:
				if(player1.playerMap.retiredTrigger) {
					reachRetirement(player1);
				}
			}
			
			else if (player1.retired) {
				System.out.println("\n((You get to spin the retirement wheel till the end of game))\n");
				retiredWheelSpin(player1);
			}
			
			maxAttribute3(player1, player2, player3); // to check if the player has a max attribute 
			
			// print the player stats at the end of the turn
			System.out.println(player1);
			wait3s(); 
			
			// -----------------------------------------------------------------
			System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||\n");
//				
			System.out.println("it's "+ player2.name + "'s Turn!"); // the beginning of p2 turn
			wait1s();		 // its a user defined function to wait for 1 second
			
			System.out.println(player2);
			wait1s();
			
			if (player2.retired == false){ // if player is not retired
				System.out.println("your current postion: ");
				player2.playerMap.print(); // prints the map of the player
				System.out.println();
				
				enterPressed("To roll the dice"); // self defined func. to check if the player pressed enter
				
				// messege printed to inform the player
				System.out.print("Dice rolling ");  
				for (int t = 0; t < 3; t ++) {
					wait1s();
					System.out.print(".");
				}	
				System.out.println();
				
				// rolling the dice using a static func. that returns an int
				diceRoll = rollDice();
				System.out.println("\nYou got "+diceRoll+"!\n");
				wait1s();
				
				player2.playerMap.move(diceRoll); // moving the player with the dice amount
				player2.currentPostion = player2.playerMap.currentStep.number; // saving the player Postion
				
				// ACTIONS
				
				// college action
				if (player2.playerMap.collegeTrigger) {  
					collegeAction(player2);
					player2.playerMap.collegeTrigger = false;
				}
				
				// job selection action
				if (player2.playerMap.jobTrigger) {
					if (player2.college) {
						System.out.println("Congrats on your graduation!!!");
						player2.knowAdd(1);
					}
					System.out.println("\nTime to choose your Career!");
					jobSelection(player2);
					
					player2.playerMap.jobTrigger = false;
				}
				
				// marriage action
				if(player2.playerMap.marriageTrigger) { 
					marrigeAction(player2, player1, player3);
					player2.playerMap.marriageTrigger = false;
				}
				
				// pay day action: when the players pass a pay step they get
				// their salary wheather they stopped on it or not		
				if (player2.playerMap.payTrigger) {
					payAction(player2);
					player2.playerMap.payTrigger = false;
				}
				
				// to activate the action of the current step:
				action(player2, player2.currentPostion); // regular actions
				
				// if player reached retirement:
				if(player2.playerMap.retiredTrigger) {
					reachRetirement(player2);
				}
			}
			
			else if (player2.retired) {
				System.out.println("\n((You get to spin the retirement wheel till the end of game))\n");
				retiredWheelSpin(player2);
			}
			
			maxAttribute3(player1, player2, player3); // to check if the player has a max attribute 
			
			// print the player stats at the end of the turn
			System.out.println(player2);
			wait3s();
			
			// --------------p3----------------------------------
			System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||\n");		
			System.out.println("it's "+ player3.name + "'s Turn!"); // the beginning of p3 turn
			wait1s();		 // its a user defined function to wait for 1 second	
			
			System.out.println(player3);
			wait1s();
			
			if (player3.retired == false){ // if player is not retired
				System.out.println("your current postion: ");
				player3.playerMap.print(); // prints the map of the player
				System.out.println();
				
				enterPressed("To roll the dice"); // self defined func. to check if the player pressed enter
				
				// messege printed to inform the player
				System.out.print("Dice rolling ");  
				for (int t = 0; t < 3; t ++) {
					wait1s();
					System.out.print(".");
				}	
				System.out.println();
				
				// rolling the dice using a static func. that returns an int
				diceRoll = rollDice();
				System.out.println("\nYou got "+diceRoll+"!\n");
				wait1s();
				
				player3.playerMap.move(diceRoll); // moving the player with the dice amount
				player3.currentPostion = player3.playerMap.currentStep.number; // saving the player Postion
				
				// ACTIONS
				
				// college action
				if (player3.playerMap.collegeTrigger) {  
					collegeAction(player3);
					player3.playerMap.collegeTrigger = false;
				}
				
				// job selection action
				if (player3.playerMap.jobTrigger) {
					if (player3.college) {
						System.out.println("Congrats on your graduation!!!");
						player3.knowAdd(1);
					}
					System.out.println("\nTime to choose your Career!");
					jobSelection(player3);
					
					player3.playerMap.jobTrigger = false;
				}
				
				// marriage action
				if(player3.playerMap.marriageTrigger) { 
					marrigeAction(player3, player2, player1);
					player3.playerMap.marriageTrigger = false;
				}
				
				// pay day action: when the players pass a pay step they get
				// their salary wheather they stopped on it or not		
				if (player3.playerMap.payTrigger) {
					payAction(player3);
					player3.playerMap.payTrigger = false;
				}
				
				// to activate the action of the current step:
				action(player3, player3.currentPostion); // regular actions
				
				// if player reached retirement:
				if(player3.playerMap.retiredTrigger) {
					reachRetirement(player3);
				}
			}
			
			else if (player3.retired) {
				System.out.println("\n((You get to spin the retirement wheel till the end of game))\n");
				retiredWheelSpin(player3);
			}
			
			maxAttribute3(player1, player2, player3);
			
			// print the player stats at the end of the turn
			System.out.println(player3);
			wait3s();
			
			System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||\n");
		}
	endGame3(player1, player2, player3);	
	}
	static void gamePlay4() { // this is the method that manages the 2 player game
		
		// the start of the name assigning to players process
		Scanner nameReader = new Scanner(System.in);
		String name; // temporary variable to save the name of the player
		
		// assigning the first player name
		System.out.print("Player 1, Enter your Name: ");
		name = nameReader.nextLine();
		Player player1 = new Player(name);
		
		// assigning the second player name
		System.out.print("Player 2, Enter your Name: ");
		name = nameReader.nextLine();
		Player player2 = new Player(name);
		
		// assigning the third player name
		System.out.print("Player 3, Enter your Name: ");
		name = nameReader.nextLine();
		Player player3 = new Player(name);
		
		// assigning the fourth player name
		System.out.print("Player 4, Enter your Name: ");
		name = nameReader.nextLine();
		Player player4 = new Player(name);
		
		// the end of the name assigning to players process
		
		// prints the instructions, which is the 1st requirement of the project
		System.out.println("\n" + instuctions + "\n");
		wait1s(); // its a user defined function to wait for 1 second
		
		// using user defined method for choices that returns a string that is either a or b
		// choiceChecker ("shortend choice A", "choice A", "shortened Choice B", "Choice B");
		String c = choiceChecker("Show rules", "show the rules of the game", "Skip rules", "Skip the rules (if you already know them)");
		if (c.equals("a")) {
			System.out.println(Rules);
		}
		wait3s();
		enterPressed("\nTo start the game");
		
		
		int diceRoll; // a temp variable to save the diceRoll value
		
		// this loop is the main game play, as long as there's an active player who
		// has not finish the game yet, the game will keep going
		while (player1.retired == false || player2.retired == false || player3.retired == false || player4.retired == false) {		
				
			System.out.println("it's "+ player1.name + "'s Turn!"); // the beginning of p1 turn
			wait1s();		 // its a user defined function to wait for 1 second	
			
			System.out.println(player1);
			wait1s();
			
			if (player1.retired == false){ // if player is not retired
				System.out.println("your current postion: ");
				player1.playerMap.print(); // prints the map of the player
				System.out.println();
				
				enterPressed("To roll the dice"); // self defined func. to check if the player pressed enter
				
				// messege printed to inform the player
				System.out.print("Dice rolling ");  
				for (int t = 0; t < 3; t ++) {
					wait1s();
					System.out.print(".");
				}	
				System.out.println();
				
				// rolling the dice using a static func. that returns an int
				diceRoll = rollDice();
				System.out.println("\nYou got "+diceRoll+"!\n");
				wait1s();
				
				player1.playerMap.move(diceRoll); // moving the player with the dice amount
				player1.currentPostion = player1.playerMap.currentStep.number; // saving the player Postion
				
				// ACTIONS
				
				// college action
				if (player1.playerMap.collegeTrigger) {  
					collegeAction(player1);
					player1.playerMap.collegeTrigger = false;
				}
				
				// job selection action
				if (player1.playerMap.jobTrigger) {
					if (player1.college) {
						System.out.println("Congrats on your graduation!!!");
						player1.knowAdd(1);
					}
					System.out.println("\nTime to choose your Career!");
					jobSelection(player1);
					
					player1.playerMap.jobTrigger = false;
				}
				
				// marriage action
				if(player1.playerMap.marriageTrigger) { 
					marrigeAction(player1, player2, player3, player4);
					player1.playerMap.marriageTrigger = false;
				}
				
				// pay day action: when the players pass a pay step they get
				// their salary wheather they stopped on it or not		
				if (player1.playerMap.payTrigger) {
					payAction(player1);
					player1.playerMap.payTrigger = false;
				}
				
				// to activate the action of the current step:
				action(player1, player1.currentPostion); // regular actions
				
				// if player reached retirement:
				if(player1.playerMap.retiredTrigger) {
					reachRetirement(player1);
				}
			}
			
			else if (player1.retired) {
				System.out.println("\n((You get to spin the retirement wheel till the end of game))\n");
				retiredWheelSpin(player1);
			}
			
			maxAttribute4(player1, player2, player3, player4);
			
			// print the player stats at the end of the turn
			System.out.println(player1);
			wait3s(); 
			
			// -----------------------------------------------------------------
			System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||\n");
				
			System.out.println("it's "+ player2.name + "'s Turn!"); // the beginning of p2 turn
			wait1s();		 // its a user defined function to wait for 1 second	
			
			System.out.println(player2);
			wait1s();
			
			if (player2.retired == false){ // if player is not retired
				System.out.println("your current postion: ");
				player2.playerMap.print(); // prints the map of the player
				System.out.println();
				
				enterPressed("To roll the dice"); // self defined func. to check if the player pressed enter
				
				// messege printed to inform the player
				System.out.print("Dice rolling ");  
				for (int t = 0; t < 3; t ++) {
					wait1s();
					System.out.print(".");
				}	
				System.out.println();
				
				// rolling the dice using a static func. that returns an int
				diceRoll = rollDice();
				System.out.println("\nYou got "+diceRoll+"!\n");
				wait1s();
				
				player2.playerMap.move(diceRoll); // moving the player with the dice amount
				player2.currentPostion = player2.playerMap.currentStep.number; // saving the player Postion
				
				// ACTIONS
				
				// college action
				if (player2.playerMap.collegeTrigger) {  
					collegeAction(player2);
					player2.playerMap.collegeTrigger = false;
				}
				
				// job selection action
				if (player2.playerMap.jobTrigger) {
					if (player2.college) {
						System.out.println("Congrats on your graduation!!!");
						player2.knowAdd(1);
					}
					System.out.println("\nTime to choose your Career!");
					jobSelection(player2);
					
					player2.playerMap.jobTrigger = false;
				}
				
				// marriage action
				if(player2.playerMap.marriageTrigger) { 
					marrigeAction(player2, player1, player3, player4);
					player2.playerMap.marriageTrigger = false;
				}
				
				// pay day action: when the players pass a pay step they get
				// their salary wheather they stopped on it or not		
				if (player2.playerMap.payTrigger) {
					payAction(player2);
					player2.playerMap.payTrigger = false;
				}
				
				// to activate the action of the current step:
				action(player2, player2.currentPostion); // regular actions
								
				// if player reached retirement:
				if(player2.playerMap.retiredTrigger) {
					reachRetirement(player2);
				}
			}
			
			else if (player2.retired) {
				System.out.println("\n((You get to spin the retirement wheel till the end of game))\n");
				retiredWheelSpin(player2);
			}
			
			maxAttribute4(player1, player2, player3, player4);
			
			// print the player stats at the end of the turn
			System.out.println(player2);
			wait3s();
			
			// --------------p3----------------------------------
			System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||\n");		
			System.out.println("it's "+ player3.name + "'s Turn!"); // the beginning of p3 turn
			wait1s();		 // its a user defined function to wait for 1 second	
			
			System.out.println(player3);
			wait1s();
			
			if (player3.retired == false){ // if player is not retired
				System.out.println("your current postion: ");
				player3.playerMap.print(); // prints the map of the player
				System.out.println();
				
				enterPressed("To roll the dice"); // self defined func. to check if the player pressed enter
				
				// messege printed to inform the player
				System.out.print("Dice rolling ");  
				for (int t = 0; t < 3; t ++) {
					wait1s();
					System.out.print(".");
				}	
				System.out.println();
				
				// rolling the dice using a static func. that returns an int
				diceRoll = rollDice();
				System.out.println("\nYou got "+diceRoll+"!\n");
				wait1s();
				
				player3.playerMap.move(diceRoll); // moving the player with the dice amount
				player3.currentPostion = player3.playerMap.currentStep.number; // saving the player Postion
				
				// ACTIONS
				
				// college action
				if (player3.playerMap.collegeTrigger) {  
					collegeAction(player3);
					player3.playerMap.collegeTrigger = false;
				}
				
				// job selection action
				if (player3.playerMap.jobTrigger) {
					if (player3.college) {
						System.out.println("Congrats on your graduation!!!");
						player3.knowAdd(1);
					}
					System.out.println("\nTime to choose your Career!");
					jobSelection(player3);
					
					player3.playerMap.jobTrigger = false;
				}
				
				// marriage action
				if(player3.playerMap.marriageTrigger) { 
					marrigeAction(player3, player2, player1, player4);
					player3.playerMap.marriageTrigger = false;
				}
				
				// pay day action: when the players pass a pay step they get
				// their salary wheather they stopped on it or not		
				if (player3.playerMap.payTrigger) {
					payAction(player3);
					player3.playerMap.payTrigger = false;
				}
				
				// to activate the action of the current step:
				action(player3, player3.currentPostion); // regular actions
				
				// if player reached retirement:
				if(player3.playerMap.retiredTrigger) {
					reachRetirement(player3);
				}
			}
			
			else if (player3.retired) {
				System.out.println("\n((You get to spin the retirement wheel till the end of game))\n");
				retiredWheelSpin(player3);
			}
			
			maxAttribute4(player1, player2, player3, player4);
			
			// print the player stats at the end of the turn
			System.out.println(player3);
			wait3s();
			
			// p4 ------------------------------
			System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||\n");		
			System.out.println("it's "+ player4.name + "'s Turn!"); // the beginning of p4 turn
			wait1s();		 // its a user defined function to wait for 1 second	
			
			System.out.println(player4);
			wait1s();
			
			if (player4.retired == false){ // if player is not retired
				System.out.println("your current postion: ");
				player4.playerMap.print(); // prints the map of the player
				System.out.println();
				
				enterPressed("To roll the dice"); // self defined func. to check if the player pressed enter
				
				// messege printed to inform the player
				System.out.print("Dice rolling ");  
				for (int t = 0; t < 3; t ++) {
					wait1s();
					System.out.print(".");
				}	
				System.out.println();
				
				// rolling the dice using a static func. that returns an int
				diceRoll = rollDice();
				System.out.println("\nYou got "+diceRoll+"!\n");
				wait1s();
				
				player4.playerMap.move(diceRoll); // moving the player with the dice amount
				player4.currentPostion = player4.playerMap.currentStep.number; // saving the player Postion
				
				// ACTIONS
				
				// college action
				if (player4.playerMap.collegeTrigger) {  
					collegeAction(player4);
					player4.playerMap.collegeTrigger = false;
				}
				
				// job selection action
				if (player4.playerMap.jobTrigger) {
					if (player4.college) {
						System.out.println("Congrats on your graduation!!!");
						player4.knowAdd(1);
					}
					System.out.println("\nTime to choose your Career!");
					jobSelection(player4);
					
					player4.playerMap.jobTrigger = false;
				}
				
				// marriage action
				if(player4.playerMap.marriageTrigger) { 
					marrigeAction(player4, player3, player2, player1);
					player4.playerMap.marriageTrigger = false;
				}
				
				// pay day action: when the players pass a pay step they get
				// their salary wheather they stopped on it or not		
				if (player4.playerMap.payTrigger) {
					payAction(player4);
					player4.playerMap.payTrigger = false;
				}
				
				// to activate the action of the current step:
				action(player4, player4.currentPostion); // regular actions
				
				// if player reached retirement:
				if(player4.playerMap.retiredTrigger) {
					reachRetirement(player4);
				}
			}
			
			else if (player4.retired) {
				System.out.println("\n((You get to spin the retirement wheel till the end of game))\n");
				retiredWheelSpin(player4);
			}
			
			maxAttribute4(player1, player2, player3, player4);
			
			// print the player stats at the end of the turn
			System.out.println(player4);
			wait3s();
			
			System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||\n");
		}
	endGame4(player1, player2, player3, player4);	
	}
	
	static void wait3s()  {
		try {
			Thread.sleep(3000);
			}
		catch (InterruptedException e){
			e.printStackTrace();
			}
	}
	
	static void wait1s() {
		try {
			Thread.sleep(1000);
			}
		catch (InterruptedException e){
			e.printStackTrace();
			}
	}
	
	public static int rollDice() {
		double x = Math.random()*4;
		x = Math.ceil(x);
		return (int) x;
	}
	
	public static void wheelSpin(Player player) {
		System.out.println("You got a wheel Spin!!");
		wait1s();
		System.out.println("The wheel is a random action, its one of the following:");
		wait1s();
		System.out.println("- 100K bonus\n- Taxes\n- Family\n- House\n- Job\n- Another Spin\n");
		enterPressed("To spin the wheel");
		
		// random value
		double x = Math.random()*6;
		x = (Math.ceil(x));	
		
		switch((int)x){
		case 1:
			System.out.println("You just received a 100K bonus from work!!!");
			player.moneyAdd(100);
			break;
		case 2:
			taxAction(player);
			break;
		case 3:
			System.out.println("\nYou got a family action!");
			familyAction(player);
			break;
		case 4:// houseAction();
			System.out.println("\nYou got a house action!");
			houseAction(player);
			break;
		case 5: // jobAction();
			System.out.println("\nYou got a job action!");
			jobAction(player);
			break;
		case 6: 
			wheelSpin(player);
			break;
		}
			
	}
	
	public static void retiredWheelSpin(Player player) {
		System.out.println("You got a wheel Spin!!");
		wait1s();
		System.out.println("The wheel is a random action, its one of the following:");
		wait1s();
		System.out.println("- Nothing :(\n- win 10K$ \n- win 100K$\n- 1 Wealth Point\n- 1 HappinessPoint\n- 1 KnowledgePoint\n- Another Spin\n");
		enterPressed("To spin the wheel");
		
		// random value
		double x = Math.random()*10;
		x = (Math.ceil(x));	
		
		if (x <= 4) {
			System.out.println("You got nothing :(");
		}
		else {
			switch((int)x){
			case 5:
				System.out.println("You just won a 100K !!!");
				player.moneyAdd(100);
				break;
			case 6:
				System.out.println("You just won a 10K !!!");
				player.moneyAdd(10);
				break;
			case 7:
				System.out.println("its extra Knowledge point");
				player.knowAdd(1);
				break;
			case 8:// houseAction();
				System.out.println("its extra Wealh point");
				player.wealthAdd(1);
				break;
			case 9: // jobAction();
				System.out.println("its extra Happiness point");
				player.happyAdd(1);
				break;
			case 10: 
				System.out.println("\nYou got another spin!");
				retiredWheelSpin(player);
				break;
			}
		}	
	}
	
	static void enterPressed(String text) {
	    Scanner enter = new Scanner(System.in);
	    System.out.print(text + " press Enter ...\n"); // text attribute is the objective from enter pressing
	    enter.nextLine(); // Wait for user to press Enter
	}
	
	public static Player MaxKnowPlayer;
	public static Player MaxHappyPlayer;
	public static Player MaxWealthPlayer;
	
	static void maxAttribute2(Player player1 , Player player2) {
		// compare knowledge points
		if (player1.knowledge > player2.knowledge) {
			MaxKnowPlayer = player1;
			player1.crownKnow = "♕";
			player2.crownKnow = "";
		}
		else if ((player2.knowledge > player1.knowledge)){
			MaxKnowPlayer = player2;
			player2.crownKnow = "♕";
			player1.crownKnow = "";
		}
		
		// compare happiness points
		if (player1.happiness > player2.happiness) {
			MaxHappyPlayer = player1;
			player1.crownHappy = "♕";
			player2.crownHappy = "";
		}
		else if((player2.happiness > player1.happiness)){
			MaxHappyPlayer = player2;
			player2.crownHappy = "♕";
			player1.crownHappy = "";
		}
		
		// compare wealth points 
		if (player1.wealth > player2.wealth) {
			MaxWealthPlayer = player1;
			player1.crownWealth = "♕";
			player2.crownWealth = "";
		}
		else if (player2.wealth > player1.wealth){
			MaxWealthPlayer = player2;
			player2.crownWealth = "♕";
			player1.crownWealth = "";
		}
	}
	
	static void maxAttribute3(Player player1 , Player player2, Player player3) {
		// compare knowledge points
		if (player1.knowledge > player2.knowledge && player1.knowledge > player3.knowledge) {
			MaxKnowPlayer = player1;
			player1.crownKnow = "♕";
			player2.crownKnow = "";
			player3.crownKnow = "";
		}
		else if (player2.knowledge > player1.knowledge && player2.knowledge > player3.knowledge) {
			MaxKnowPlayer = player2;
			player2.crownKnow = "♕";
			player1.crownKnow = "";
			player3.crownKnow = "";
		}
		else if (player3.knowledge > player1.knowledge && player3.knowledge > player2.knowledge) {
			MaxKnowPlayer = player3;
			player3.crownKnow = "♕";
			player1.crownKnow = "";
			player2.crownKnow = "";
		}
		
		// compare happiness points
		if (player1.happiness > player2.happiness && player1.happiness > player3.happiness) {
			MaxHappyPlayer = player1;
			player1.crownHappy = "♕";
			player2.crownHappy = "";
			player3.crownHappy = "";
		}
		else if ((player2.happiness > player1.happiness && player2.happiness > player3.happiness)) {
			MaxHappyPlayer = player2;
			player2.crownHappy = "♕";
			player1.crownHappy = "";
			player3.crownHappy = "";
		}
		else if (player3.happiness > player1.happiness && player3.happiness > player2.happiness) {
			MaxHappyPlayer = player3;
			player3.crownHappy = "♕";
			player1.crownHappy = "";
			player2.crownHappy = "";
		}
		
		// compare wealth points 
		if (player1.wealth > player2.wealth && player1.wealth > player3.wealth) {
			MaxWealthPlayer = player1;
			player1.crownWealth = "♕";
			player2.crownWealth = "";
			player3.crownWealth = "";
		}
		else if ((player2.wealth > player1.wealth && player2.wealth > player3.wealth)) {
			MaxWealthPlayer = player2;
			player2.crownWealth = "♕";
			player1.crownWealth = "";
			player3.crownWealth = "";
		}
		else if (player3.wealth > player1.wealth && player3.wealth > player2.wealth) {
			MaxWealthPlayer = player3;
			player3.crownWealth = "♕";
			player1.crownWealth = "";
			player2.crownWealth = "";
		}
	}
	
	static void maxAttribute4(Player player1 , Player player2, Player player3, Player player4) {
		// compare knowledge points
		if (player1.knowledge > player2.knowledge && player1.knowledge > player3.knowledge && player1.knowledge > player4.knowledge) {
			MaxKnowPlayer = player1;
			player1.crownKnow = "♕";
			player2.crownKnow = "";
			player3.crownKnow = "";
			player4.crownKnow = "";
		}
		else if (player2.knowledge > player1.knowledge && player2.knowledge > player3.knowledge && player2.knowledge > player4.knowledge) {
			MaxKnowPlayer = player2;
			player2.crownKnow = "♕";
			player1.crownKnow = "";
			player3.crownKnow = "";
			player4.crownKnow = "";
		}
		else if (player3.knowledge > player1.knowledge && player3.knowledge > player2.knowledge && player3.knowledge > player4.knowledge) {
			MaxKnowPlayer = player3;
			player3.crownKnow = "♕";
			player1.crownKnow = "";
			player2.crownKnow = "";
			player4.crownKnow = "";
		}
		
		else if (player4.knowledge > player1.knowledge && player4.knowledge > player2.knowledge && player4.knowledge > player3.knowledge) {
			MaxKnowPlayer = player4;
			player4.crownKnow = "♕";
			player1.crownKnow = "";
			player2.crownKnow = "";
			player3.crownKnow = "";
		}
		
		// compare happiness points
		if (player1.happiness > player2.happiness && player1.happiness > player3.happiness && player1.happiness > player4.happiness) {
			MaxHappyPlayer = player1;
			player1.crownHappy = "♕";
			player2.crownHappy = "";
			player3.crownHappy = "";
			player4.crownHappy = "";
		}
		else if ((player2.happiness > player1.happiness && player2.happiness > player3.happiness && player2.happiness > player4.happiness)) {
			MaxHappyPlayer = player2;
			player2.crownHappy = "♕";
			player1.crownHappy = "";
			player3.crownHappy = "";
			player4.crownHappy = "";
		}
		else if (player3.happiness > player1.happiness && player3.happiness > player2.happiness && player3.happiness > player4.happiness) {
			MaxHappyPlayer = player3;
			player3.crownHappy = "♕";
			player1.crownHappy = "";
			player2.crownHappy = "";
			player4.crownHappy = "";
		}
		else if (player4.happiness > player1.happiness && player4.happiness > player2.happiness && player4.happiness > player3.happiness) {
			MaxHappyPlayer = player4;
			player4.crownHappy = "♕";
			player1.crownHappy = "";
			player2.crownHappy = "";
			player3.crownHappy = "";
		}
		
		// compare wealth points 
		if (player1.wealth > player2.wealth && player1.wealth > player3.wealth && player1.wealth > player4.wealth) {
			MaxWealthPlayer = player1;
			player1.crownWealth = "♕";
			player2.crownWealth = "";
			player3.crownWealth = "";
			player4.crownWealth = "";
		}
		else if ((player2.wealth > player1.wealth && player2.wealth > player3.wealth && player2.wealth > player4.wealth)) {
			MaxWealthPlayer = player2;
			player2.crownWealth = "♕";
			player1.crownWealth = "";
			player3.crownWealth = "";
			player4.crownWealth = "";
		}
		else if (player3.wealth > player1.wealth && player3.wealth > player2.wealth && player3.wealth > player4.wealth) {
			MaxWealthPlayer = player3;
			player3.crownWealth = "♕";
			player1.crownWealth = "";
			player2.crownWealth = "";
			player4.crownWealth = "";
		}
		else if (player4.wealth > player1.wealth && player4.wealth > player2.wealth && player4.wealth > player3.wealth) {
			MaxWealthPlayer = player4;
			player4.crownWealth = "♕";
			player1.crownWealth = "";
			player2.crownWealth = "";
			player3.crownWealth = "";
		}
	}
	
	static void marrigeAction(Player mar_player) { // this is for second chance and thats why it doesnt 
		mar_player.married = true;				   // take another players
		System.out.println("\nCongrats on your Marriage!!!");
		mar_player.happyAdd(1);
		wait1s();
	}

	
	static void marrigeAction(Player mar_player, Player player2, Player player3, Player player4 ) {
		mar_player.married = true;
		mar_player.happyAdd(1);
		
		System.out.println("\nCongrats!!!! Here is your wedding gift from Everybody!!");
		wait1s();
		player2.moneySub(20);
		player3.moneySub(20);
		player4.moneySub(20);
		mar_player.moneyAdd(60);

	}
	// marriage action for 4 players
	static void marrigeAction(Player mar_player, Player player2, Player player3) {
		mar_player.married = true;
		mar_player.happyAdd(1);
		
		System.out.println("\nCongrats!!!! Here is your wedding gift from Everybody!!");
		wait1s();
		player2.moneySub(20);
		player3.moneySub(20);
		mar_player.moneyAdd(40);
	}
	
	// marriage action for 3 players
	static void marriageAction(Player mar_player, Player player2) {
		mar_player.married = true;
		mar_player.happyAdd(1);
		wait1s();
		
		System.out.println("\nCongrats!!! Here is your wedding gift from "+ player2.name + "!!\n");
		wait1s();
		player2.moneySub(20);
		mar_player.moneyAdd(20);

	}
	
	// marriage action for 2 players
	static void collegeAction(Player player) {
		player.college = true;
		
		wait1s();
		System.out.println("You just got into College!! good luck.\n");
		wait1s();
		player.moneySub(100);
	}
	
	static void jobSelection(Player player) {
		Job [] jobsArray = new Job[7];
		if (player.college) {
			jobsArray[0] = new Job("Brain Surgeon", 100, 80);
			jobsArray[1] = new Job("Petroleum Engineer", 120, 105);
			jobsArray[2] = new Job("Professor", 115, 100);
			jobsArray[3] = new Job("Pilot", 95, 50);
			jobsArray[4] = new Job("Robots Engineer", 110, 85);
			jobsArray[5] = new Job("Video Games Developer", 114, 90);
			jobsArray[6] = new Job("Special Agent", 125, 110);

		}
		else {
			jobsArray[0] = new Job("Delivery driver", 60, 30);
			jobsArray[1] = new Job("Security guard", 50, 25);
			jobsArray[2] = new Job("Artist", 45, 10);
			jobsArray[3] = new Job("Photographer", 70, 55);
			jobsArray[4] = new Job("Athlete", 75, 60);
			jobsArray[5] = new Job("Video Blogger", 55, 30);
			jobsArray[6] = new Job("Chef", 65, 40);
		}
		
		// if the player has a job do not suggest it again
		int random1 = (int)Math.round(Math.random()*6);
		while (jobsArray[random1] == player.job)
			random1 = (int)Math.round(Math.random()*6);
		
		int random2 = random1;
		
		while (random2 == random1 || jobsArray[random2] == player.job) {
			random2 = (int)Math.round(Math.random()*6);
		}
		
		System.out.println("You got 2 job offeres");
		String choice = choiceChecker(jobsArray[random1].jobTitle, jobsArray[random1].toString(), jobsArray[random2].jobTitle, jobsArray[random2].toString());
		
		if (choice.equals("a")){
			player.setJob(jobsArray[random1]);
		}
		else if (choice.equals("b")){
			player.setJob(jobsArray[random2]);
		}
	}
	
	public static String choiceChecker(String choiceA_short, String choiceA, String choiceB_short, String choiceB) {
		Scanner read = new Scanner(System.in);
		
		// prints the choices of the player
		System.out.println("\nChoose between:\n");
		Main.wait1s(); // used a static method from the main class to wait a sec
		System.out.println("A) "+ choiceA );
		System.out.println("B) "+ choiceB+"\n");
		Main.wait1s();
		System.out.println("Type A for "+ choiceA_short);
		System.out.println("Type B for "+ choiceB_short + "\n");
		System.out.print("Your Choice: ");
		
		// reads the answer
		String choice = read.next().toLowerCase();
		
		// check if its correct or ask the user to correct it
		while ((!choice.equals("a") && (!choice.equals("b") ))) { // to check if the input is correct and ask the player to re enter if wrong
			System.out.println("\nEnter a valid choice, either A or B");
			System.out.print("your choice: ");
			choice = read.next().toLowerCase();
		}
		
		// prints the choice of the player
		if (choice.equals("a"))
			System.out.println("You have chosen "+ choiceA_short +"\n");
		else
			System.out.println("You have chosen "+ choiceB_short + "\n");
		
		return choice;
	}
	
	static void payAction(Player player) {
		System.out.println("\nSalary has been deposited to your account!!");
		wait1s();
		player.moneyAdd(player.salary);
	}
	
	static void taxAction(Player player) {
		System.out.println("TAXES!! you have to pay " + player.job.tax +"K :(");
		wait1s();
		player.moneySub(player.job.tax);
	}
	
	static void houseAction(Player player) {
		// all the houses in the game
		House [] house_list = new House [10];
		house_list [0] = new House ("Small Apartment", 220, 400);
		house_list [1] = new House ("Large Apartment", 250, 500);
		house_list [2] = new House ("Townhouse", 280, 600);
		house_list [3] = new House ("Small House", 310, 610);
		house_list [4] = new House ("Studio Apartment", 360, 700);
		house_list [5] = new House ("Suburban House", 400, 800);
		house_list [6] = new House ("Duplex", 450, 850);
		house_list [7] = new House ("Luxury Condo", 500, 875);
		house_list [8] = new House ("Mansion", 630, 950);
		house_list [9] = new House ("an Island", 710, 1000);
		
		int i, random1, random2;
		
		// if the player owns a house:
		if (Player.house_counter(player.housesList) > 0) {
			System.out.println("This is a house Action Card .. ");
			System.out.println("Your money = " + player.cash + "K$");
			String c = choiceChecker("buy a house", "buy a new house", "sell a house", "sell an owned house");
			if (c.equals("b")) { // if the player chose to sell
				if(Player.house_counter(player.housesList)>1) { // if the player have more than 1 house
					Scanner read = new Scanner(System.in);		// then gotta choose
					player.housePrint();
					System.out.print("\nEnter the number besides the house that you want to sell: ");
					int houseNum = read.nextInt();
					
					while(houseNum > Player.house_counter(player.housesList) || houseNum < 1) { // to check that the number is valid
						System.out.print("\nEnter a valid number: ");
						houseNum = read.nextInt();
					}
					player.sellHouse(houseNum);
					return;
				}
				else {					// in case the player has only 1 house
					player.sellHouse(1);
					return;
				}
			}
		}
		// if the player doesnt own a house so go to buying immeditly
		// or if he own a house but chose to buy more
		System.out.println("You are buying a house ");
		System.out.println("Your money = " + player.cash + "K$");
		if (player.cash < 200) { // if the player is too poor we suggest the lowest prices
			 random1 = 0;
			 random2 = 1;
		}
		else {		// to provide the player of something buyable
			for (i = 9; i >= 0; i --)
				if (player.cash + 50 >= house_list[i].buyingPrice) 
					break;
			
			random1 = (int)Math.round(Math.random()*i); 
			random2 = random1;							// to make sure we dont suggeset
														// the same house
			while (random2 == random1) {	 			
				random2 = (int)Math.round(Math.random()*i);
			}
		}
		// choice of the 2 houses
		String choice = choiceChecker(house_list[random1].houseName,house_list[random1].tostring(),house_list[random2].houseName,house_list[random2].tostring());
		if (choice.equals("a")) 
			player.buyHouse(house_list[random1]);
		else 
			player.buyHouse(house_list[random2]);
	}
	
	static void jobAction(Player player) {
		int randomAmount ;
		
		if (player.college) 
			randomAmount = (int)Math.ceil(Math.random()*3)*10; // the raise is random (10k,20k,30k)
		else 
			randomAmount = (int)Math.ceil(Math.random()*3)*4; // the raise is random (4k,8k,12k)
		
		System.out.println("\njob Action ... You will get a knowldge point!\n");
		
		String choice = choiceChecker("Raise", "you can get a raise!!\nRaise Bonus = 70K\nNew Salary"
				+ " = "+ (player.salary + randomAmount)+ "K$", "Another job", "find another job"
						+ " (Try your luck, you might get a better job)" );
		if (choice.equals("a")) { // in case the player chose a raise
			player.salary += randomAmount;
			System.out.println("\nCongrats on Your Raise!!\nHere is your Raise Bonus");
			player.moneyAdd(70);
			player.knowAdd(1);
			System.out.println("Your new Salary = " + player.salary + "K$");
		}
		else { // in case the player wants a new job
			jobSelection(player);
			player.knowAdd(1);
		}
		
	}	
	
	static void familyAction(Player player) {
		int random =(int)Math.ceil(Math.random()*4);
		
		if (player.married == false) {
			
			String Choice = choiceChecker("Get married", "Get married (this is your second chance to gain"
					+ "\nHappiness Points but you dont get a wedding gift)", "Continue life", "Continue life");
			if (Choice.equals("a")){
				marrigeAction(player);
			}
		}
		else {
			switch(random) {
			case 1:
				System.out.println("Congrats on the arrival of your new baby girl!!");
				player.happyAdd(1);
				player.children += 1;
				break;
			case 2:
				System.out.println("Congrats on the arrival of your new baby boy!!");
				player.happyAdd(1);
				player.children += 1;
				break;
			case 3:
				System.out.println("YOU JUST GOT TWINS, 2 pretty little girls!!");
				player.happyAdd(2);
				player.children += 2;
				break;
			case 4:
				System.out.println("YOU JUST GOT TWINS, 2 charming little boys!!");
				player.happyAdd(2);
				player.children += 2;
				break;
			}
		}
	}	
	
	
	static int [] regular = {3,7,11,12,18,21,22,31,32,26,27,38,39,49,56,58,59,68,66}; // to check if the number is regular action before entering the switch
	static int [] tax = {9,16,28,36,41,53,62}; // to check f the number is tax action before entering the switch
	static int [] house = {8,30,34,37,46,63,67,69}; // to check if the number is house action before entering the switch
	static int [] family = {14,23,43,44,40}; // to check if the number is family action before entering the switch
	static int [] wheel = {20,33,54,64,42,51,61}; // to check if the number is wheel spinning action before entering the switch
	static int [] job = {60, 17 ,19 ,29}; // to check if the number is job action before entering the switch
	
	static void action(Player player, int step) {
		
		String choice; // temp
		
		// 1- regular action steps
		// {3,7,11,12,18,21,22,31,32,26,27,38,39,49,56,58,59,68,66};
		if (Map.arrayContains(regular,step)) {
			switch (step) {
			case 3: // work path
				choice = choiceChecker("Cruise","Go on a Cruise\nMoney - 100K\nHappiness + 2 points"
						+ "\n","Overtime","Work Overtime\nMoney + 20K");
				if (choice.equals("a")){
					player.moneySub(100);
					player.happyAdd(2);
				}
				else {
					player.moneyAdd(20);
				}
				break;
			case 7: // marraige path
				choice = choiceChecker("Buy a gift","buy a gifts for your family\nMoney - 25K\nHappiness + 1 points"
						+ "\n","go on a trip", "go on a trip with your family\nMoney - 50K\nHappiness + 2 points");
				if (choice.equals("a")){
					player.moneySub(25);
					player.happyAdd(1);
				}
				else {
					player.moneySub(50);
					player.happyAdd(2);
				}
				break;
			case 11: // college path
				choice = choiceChecker("Painting Lessons","Get painting lessons\nMoney - 100K\nKnowledge"
						+ " + 2 points\n","Game Stream","Watch Game Live Stream\nKnowledge + 1 point");
				if (choice.equals("a")){
					player.moneySub(100);
					player.knowAdd(2);
				}
				else {
					player.knowAdd(1);
				}
				break;
			case 12: // college path
				choice = choiceChecker("Painting Lessons","Get painting lessons\nMoney - 100K\nKnowledge"
						+ " + 2 points\n","Camp","Camp under the stars\nHappiness + 1 point");
				if (choice.equals("a")){
					player.moneySub(100);
					player.knowAdd(2);
				}
				else {
					player.happyAdd(1);
				}
				break;
			case 18: // single education-career path
				choice = choiceChecker("Learn video editing","Learn video editing\nKnowlege + 1 point"
						+ "\n", "Vacation","Go on a vacation\nMoney - 100K\nHappiness + 2 points");
				if (choice.equals("a")){
					player.knowAdd(1);
				}
				else {
					player.moneySub(100);
					player.happyAdd(2);
				}
				break;
			case 21: // college path
				System.out.println("You accidentally broke one of the dorm lamps!\n"
						+ "you have to pay 20K dorm damage fee :(\n");
				player.moneySub(20);
				break;
				
			case 22: // college path
				choice = choiceChecker("Volunteer","Volunteering Opportunity with college\nHappiness + 1 point"
						+ "\n","Learn cooking","take cooking lessons online\nKnowlege + 1 point");
				if (choice.equals("a")){
					player.happyAdd(1);
				}
				else {
					player.knowAdd(1);
				}
				break;
			case 26: // normal step
				choice = choiceChecker("Buy the TV screen","Buy a new huge TV screen\nMoney - 50K\nWealth + 1 point"
						+ "\n","Order food","Order from your favourite resturant\nMoney - 50K\nHappiness + 1 point");
				if (choice.equals("a")){
					player.moneySub(50);
					player.wealthAdd(1);
				}
				else {
					player.moneySub(50);
					player.happyAdd(1);
				}
				break;
				
			case 27:  // normal step
				choice = choiceChecker("New Car","Buy a New Car\nMoney - 150K \nWealth + 2 points"
						+ "\n","Charity","Donate to Charity\nMoney - 150K\nHappiness + 2 point");
				if (choice.equals("a")){
					player.moneySub(150);
					player.wealthAdd(2);
				}
				else {
					player.moneySub(150);
					player.happyAdd(2);
				}
				break;
			case 31: // college path
				System.out.println("Congratulations!! You won the best research prize!");
				player.moneyAdd(20);
				break;
			case 32: // college path
				choice = choiceChecker("Part time job","get a part time job\nMoney + 20K"
						+ "\n","College project","Work on extra project for college\nKnowledge + 1 point");
				if (choice.equals("a")){
					player.moneyAdd(20);
				}
				else {
					player.knowAdd(1);
				}
				break;
			case 38: // normal step
				System.out.println("Congrats on winning a prize at your work!!!\nHere is your award:");
				player.moneyAdd(50);
				break;
			case 39: //single (knowlegde or career)
				choice = choiceChecker("Piece of Art","Buy a Piece of Art\nMoney - 75K\nWealth + 1 point"
						+ "\n","Professional Event","Attend Professional Event\nMoney - 75K\nKnowledge + 1 point");
				if (choice.equals("a")){
					player.moneySub(75);
					player.wealthAdd(1);
				}
				else {
					player.moneySub(75);
					player.knowAdd(1);
				}
				break;
			case 49: //normal step
				choice = choiceChecker("Spin the wheel","Spin the wheel","Vacation","Go on"
						+ " a vacation\nMoney - 100K\nHappiness + 2 points");
				if (choice.equals("a")){
					wheelSpin(player);
				}
				else {
					player.moneySub(100);
					player.happyAdd(2);
				}
				break;
			case 56: // safe path
				choice = choiceChecker("house decoration","change your house decoration\nMoney - 100K \nWealth + 2 points"
						+ "\n","Charity","Donate to Charity\nMoney - 150K\nHappiness + 2 point");
				if (choice.equals("a")){
					player.moneySub(150);
					player.wealthAdd(2);
				}
				else {
					player.moneySub(150);
					player.happyAdd(2);
				}
				break;
			case 58: // safe path
				System.out.println("You recieved a gift from your co-worker!");
				player.moneyAdd(45);
				break;
			case 59: // safe path
				System.out.println("Your car broke down and needs repairs :(");
				player.moneySub(80);
				break;
				
			case 68: // safe path
				choice = choiceChecker("Road Trip","Take a Road Trip\nMoney - 100K\nHappiness + 2 points"
						+ "\n","Go on a cruise","Go on a cruise\nMoney - 150K\nHappiness + 3 points");
				if (choice.equals("a")){
					player.moneySub(100);
					player.happyAdd(2);
				}
				else {
					player.moneySub(150);
					player.happyAdd(3);
				}
				break;
			case 66: // safe path
				if (player.loans > 0) {
					choice = choiceChecker("Pay","Pay your loans", "Don't pay", "Don't pay"
							+ " and Lose Score points at the end");
					if (choice.equals("a")){
						int amount = player.loans * 60;
						player.moneySub(amount);
						player.loans=0;
					}				
				}
				else {
					choice = choiceChecker("Spin the wheel","Spin the wheel","Vacation","Go on"
							+ " a vacation\nMoney - 100K\n Happiness + 2 points");
					if (choice.equals("a")){
						wheelSpin(player);
					}
					else {
						player.moneySub(100);
						player.happyAdd(2);
					}
				}
				break;
			}	
		}
		
		
		// 2 - tax actions
		// {9,16,28,36,41,53,62}
		
		else if(Map.arrayContains(tax, step)) {
			switch(step) {
			case 9:
				taxAction(player);
				break;
			case 16:
				taxAction(player);
				break;
			case 28:
				taxAction(player);
				break;
			case 36:
				taxAction(player);
				break;
			case 41:
				taxAction(player);
				break;
			case 53:
				taxAction(player);
				break;
			case 62:
				taxAction(player);
				break;
			}
		}		
		// house actions
		//{8,30,34,37,46,63,67,69};
		else if(Map.arrayContains(house, step)) {
			switch (step) {
			case 8:
				houseAction(player);
				break;
			case 30:
				houseAction(player);
				break;
			case 34:
				houseAction(player);
				break;
			case 37:
				houseAction(player);
				break;
			case 46:
				houseAction(player);
				break;
			case 63:
				houseAction(player);
				break;
			case 67:
				houseAction(player);
				break;
			case 69:
				houseAction(player);
				break;
			}
		}
		
		
		// family Actions
		// {14,23,43,44,40,60};
		else if(Map.arrayContains(family, step)) {
			switch(step) {
			case 40:
				familyAction(player);
				break;
			case 14:
				familyAction(player);
				break;
			case 23:
				familyAction(player);
				break;
			case 43:
				familyAction(player);
				break;
			case 44:
				familyAction(player);
				break;
			}
		}
		
		
		// spin the wheel
		// {20,33,54,64,42,51,61}
		else if(Map.arrayContains(wheel, step)) {
			switch(step) {
			case 20:
				wheelSpin(player);
				break;
			case 33:
				wheelSpin(player);
				break;
			case 54:
				wheelSpin(player);
				break;
			case 64:
				wheelSpin(player);
				break;
			case 42:
				wheelSpin(player);
				break;
			case 51:
				wheelSpin(player);
				break;
			case 61:
				wheelSpin(player);
				break;
			}
		}
		
		
		//job Action
		//{60, 17 ,19 ,29};
		else if(Map.arrayContains(job, step)) {
			switch(step) {
			case 60:
				jobAction(player);
				break;
			case 17:
				jobAction(player);
				break;
			case 19:
				jobAction(player);
				break;
			case 29:
				jobAction(player);
				break;
			}
		}
	}
	
	public static int counter = 0;
	public static int retirmentMoney() {
		counter++;
		if (counter == 1)
			return 250;
		else if (counter == 2)
			return 200;
		else if (counter == 3)
			return 150;
		else
			return 100;
	}
	
	public static void reachRetirement(Player player) {
		System.out.println(player.name +" just reached retirement!! here is retirement bonus.");
		player.moneyAdd(retirmentMoney());
		player.retired = true;
		player.playerMap.retiredTrigger = false;
		if (Player.house_counter(player.housesList) != 0) {
			
			// to delete all of the houses
			System.out.println("\nYou have to sell all of Your houses\n");
			int maxCount = Player.house_counter(player.housesList);
			
			for (int counter = 1; counter <= maxCount; counter++) {
				if (Player.house_counter(player.housesList) > 0) {
					player.sellHouse(1);
				} 
				else {
					break;
				}
			}
		}
		
	}
	
	public static void endGame2(Player player1, Player player2) {
		System.out.println("===============\nEnd of the game\n===============");
		wait1s();
		
		int p1Score = 0;
		System.out.println("The score of "+player1.name+":");
		wait1s();
		
		// every 1K = 1 point
		System.out.println("Money points = "+player1.cash);
		p1Score += player1.cash; 
		wait1s();
		
		// every attribute point = 20 point
		System.out.println("Attributes points = "+ (int)((player1.happiness + player1.knowledge + player1.wealth)*20));
		p1Score +=(int)((player1.happiness + player1.knowledge + player1.wealth)*20);
		wait1s();
		
		// every child = 50 poits
		System.out.println("Children points = " + (int)(player1.children * 50));
		p1Score +=(int)(player1.children * 50);
		wait1s();
		
		// if player has maximum num of knowledge points
		if (MaxKnowPlayer == player1){
			System.out.println("You got the highest number of knowledge pts = 50 points");
			p1Score +=50;
			wait1s();
		}
		
		// if player has maximum num of happiness points
		if (MaxHappyPlayer == player1){
			System.out.println("You got the highest number of Happiness pts = 50 points");
			p1Score +=50;
			wait1s();
		}
		
		// if player has maximum num of wealth points
		if (MaxWealthPlayer == player1){
			System.out.println("You got the highest number of Wealth pts = 50 points");
			p1Score +=50;
			wait1s();
		}
		
		// every loan = - 100 points
		System.out.println("Loans points = -" + (int)((player1.loans)*100));
		p1Score -= (int)((player1.loans)*100);
		wait1s();
		
		// calculate total of p1
		System.out.println("Total points of " + player1.name + " = "+ p1Score);
		wait3s();
		
		System.out.println("\n-------------------------------------\n");
		
		// p2 --------------------------------------------------------
		
		int p2Score = 0;
		System.out.println("The score of "+player2.name+":");
		wait1s();
		
		// every 1K = 1 point
		System.out.println("Money points = "+player2.cash);
		p2Score += player2.cash; 
		wait1s();
		
		// every attribute point = 20 point
		System.out.println("Attributes points = "+ (int)((player2.happiness + player2.knowledge + player2.wealth)*20));
		p2Score +=(int)((player2.happiness + player2.knowledge + player2.wealth)*20);
		wait1s();
		
		// every child = 50 poits
		System.out.println("Children points = " + (int)(player2.children * 50));
		p2Score +=(int)(player2.children * 50);
		wait1s();
		
		
		// if player has maximum num of knowledge points
		if (MaxKnowPlayer == player2){
			System.out.println("You got the highest number of knowledge pts = 50 points");
			p2Score +=50;
			wait1s();
		}
		
		// if player has maximum num of happiness points
		if (MaxHappyPlayer == player2){
			System.out.println("You got the highest number of Happiness pts = 50 points");
			p2Score+=50;
			wait1s();
		}
		
		// if player has maximum num of wealth points
		if (MaxWealthPlayer == player2){
			System.out.println("You got the highest number of Wealth pts = 50 points");
			p2Score +=50;
			wait1s();
		}
		
		// every loan = - 100 points
		System.out.println("Loans points = -" + (int)((player2.loans)*100));
		p2Score -= (int)((player2.loans)*100);
		wait1s();
		
		// calc points
		System.out.println("Total points of " + player2.name + " = "+ p2Score);
		wait3s();
		
		// ------------------ winner ------------------
		System.out.println("=================================");
		if (p1Score > p2Score)  // winner = p1
			System.out.println("---The winner is (" + player1.name +")---");
		else if (p2Score > p1Score) // winner = p2
			System.out.println("---The winner is (" + player2.name +")---");
		else
			System.out.println("---Draw---");
	}
	
	
	public static void endGame3(Player player1, Player player2, Player player3) {
		System.out.println("===============\nEnd of the game\n===============");
		wait1s();
		
		System.out.println("The score of "+player1.name+":");
		wait1s();
		
		// every 1K = 1 point
		System.out.println("Money points = "+player1.cash);
		player1.score += player1.cash; 
		wait1s();
		
		// every attribute point = 20 point
		System.out.println("Attributes points = "+ (int)((player1.happiness + player1.knowledge + player1.wealth)*20));
		player1.score +=(int)((player1.happiness + player1.knowledge + player1.wealth)*20);
		wait1s();
		
		// every child = 50 poits
		System.out.println("Children points = " + (int)(player1.children * 50));
		player1.score +=(int)(player1.children * 50);
		wait1s();
		
		// if player has maximum num of knowledge points
		if (MaxKnowPlayer == player1){
			System.out.println("You got the highest number of knowledge pts = 50 points");
			player1.score +=50;
			wait1s();
		}
		
		// if player has maximum num of happiness points
		if (MaxHappyPlayer == player1){
			System.out.println("You got the highest number of Happiness pts = 50 points");
			player1.score+=50;
			wait1s();
		}
		
		// if player has maximum num of wealth points
		if (MaxWealthPlayer == player1){
			System.out.println("You got the highest number of Wealth pts = 50 points");
			player1.score +=50;
			wait1s();
		}
		
		// every loan = - 100 points
		System.out.println("Loans points = -" + (int)((player1.loans)*100));
		player1.score -= (int)((player1.loans)*100);
		wait1s();
		
		// calculate total of p1
		System.out.println("Total points of " + player1.name + " = "+ player1.score);
		wait3s();
		
		System.out.println("\n-------------------------------------\n");
		
		// p2 --------------------------------------------------------
		
		System.out.println("The score of "+player2.name+":");
		wait1s();
		
		// every 1K = 1 point
		System.out.println("Money points = "+player2.cash);
		player2.score += player2.cash; 
		wait1s();
		
		// every attribute point = 20 point
		System.out.println("Attributes points = "+ (int)((player2.happiness + player2.knowledge + player2.wealth)*20));
		player2.score +=(int)((player2.happiness + player2.knowledge + player2.wealth)*20);
		wait1s();
		
		// every child = 50 poits
		System.out.println("Children points = " + (int)(player2.children * 50));
		player2.score +=(int)(player2.children * 50);
		wait1s();
		
		// if player has maximum num of knowledge points
		if (MaxKnowPlayer == player2){
			System.out.println("You got the highest number of knowledge pts = 50 points");
			player2.score +=50;
			wait1s();
		}
		
		// if player has maximum num of happiness points
		if (MaxHappyPlayer == player2){
			System.out.println("You got the highest number of Happiness pts = 50 points");
			player2.score +=50;
			wait1s();
		}
	
		// if player has maximum num of wealth points
		if (MaxWealthPlayer == player2){
			System.out.println("You got the highest number of Wealth pts = 50 points");
			player2.score +=50;
			wait1s();
		}
		
		// every loan = - 100 points
		System.out.println("Loans points = -" + (int)((player2.loans)*100));
		player2.score -= (int)((player2.loans)*100);
		wait1s();
		//calc total
		System.out.println("Total points of " + player2.name + " = "+ player2.score);
		wait3s();
		
		System.out.println("\n-------------------------------------\n");
		
		
		// p3 -----------------------------------------
		
		System.out.println("The score of "+player3.name+":");
		wait1s();
		
		// every 1K = 1 point
		System.out.println("Money points = "+player3.cash);
		player3.score += player3.cash; 
		wait1s();
		
		// every attribute point = 20 point
		System.out.println("Attributes points = "+ (int)((player3.happiness + player3.knowledge + player3.wealth)*20));
		player3.score +=(int)((player3.happiness + player3.knowledge + player3.wealth)*20);
		wait1s();
		
		// every child = 50 poits
		System.out.println("Children points = " + (int)(player3.children * 50));
		player3.score +=(int)(player3.children * 50);
		wait1s();
		
		
		// if player has maximum num of knowldge points
		if (MaxKnowPlayer == player3){
			System.out.println("You got the highest number of knowledge pts = 50 points");
			player3.score+=50;
			wait1s();
		}
		
		// if player has maximum num of happiness points
		if (MaxHappyPlayer == player3){
			System.out.println("You got the highest number of Happiness pts = 50 points");
			player3.score +=50;
			wait1s();
		}
		
		// if player has maximum num of wealth points
		if (MaxWealthPlayer == player3){
			System.out.println("You got the highest number of Wealth pts = 50 points");
			player3.score+=50;
			wait1s();
		}
		
		// every loan = - 100 points
		System.out.println("Loans points = -" + (int)((player3.loans)*100));
		player3.score -= (int)((player3.loans)*100);
		wait1s();
				
		
		// calculate total of p3
		System.out.println("Total points of " + player3.name + " = "+ player3.score);
		wait3s();
		// ------------------ winner ------------------
		
		System.out.println("=================================");
		
		if (player1.score > player2.score && player1.score > player3.score)  // winner = p1
			System.out.println("---The winner is (" + player1.name +")---");
		else if (player2.score > player1.score && player2.score > player3.score) // winner = p2
			System.out.println("---The winner is (" + player2.name +")---");
		else if (player3.score > player1.score && player3.score > player2.score)
			System.out.println("---The winner is (" + player3.name +")---");
		else
			System.out.println("---Draw---");
	}
	
	public static void endGame4(Player player1, Player player2, Player player3, Player player4) {

		System.out.println("===============\nEnd of the game\n===============");
		wait1s();
		
		System.out.println("The score of "+player1.name+":");
		wait1s();
		
		// every 1K = 1 point
		System.out.println("Money points = "+player1.cash);
		player1.score += player1.cash; 
		wait1s();
		
		// every attribute point = 20 point
		System.out.println("Attributes points = "+ (int)((player1.happiness + player1.knowledge + player1.wealth)*20));
		player1.score +=(int)((player1.happiness + player1.knowledge + player1.wealth)*20);
		wait1s();
		
		// every child = 50 poits
		System.out.println("Children points = " + (int)(player1.children * 50));
		player1.score +=(int)(player1.children * 50);
		wait1s();
		
		// if player has maximum num of knowledge points
		if (MaxKnowPlayer == player1){
			System.out.println("You got the highest number of knowledge pts = 50 points");
			player1.score +=50;
			wait1s();
		}
		
		// if player has maximum num of happiness points
		if (MaxHappyPlayer == player1){
			System.out.println("You got the highest number of Happiness pts = 50 points");
			player1.score +=50;
			wait1s();
		}
		
		// if player has maximum num of wealth points
		if (MaxWealthPlayer == player1){
			System.out.println("You got the highest number of Wealth pts = 50 points");
			player1.score +=50;
			wait1s();
		}
		
		// every loan = - 100 points
		System.out.println("Loans points = -" + (int)((player1.loans)*100));
		player1.score -= (int)((player1.loans)*100);
		wait1s();
		
		// calculate total of p1
		System.out.println("Total points of " + player1.name + " = "+ player1.score);
		wait3s();
		
		System.out.println("\n-------------------------------------\n");
		// p2 --------------------------------------------------------
		
		System.out.println("The score of "+player2.name+":");
		wait1s();
		
		// every 1K = 1 point
		System.out.println("Money points = "+player2.cash);
		player2.score += player2.cash; 
		wait1s();
		
		// every attribute point = 20 point
		System.out.println("Attributes points = "+ (int)((player2.happiness + player2.knowledge + player2.wealth)*20));
		player2.score +=(int)((player2.happiness + player2.knowledge + player2.wealth)*20);
		wait1s();
		
		// every child = 50 poits
		System.out.println("Children points = " + (int)(player2.children * 50));
		player2.score +=(int)(player2.children * 50);
		wait1s();
	
		// if player has maximum num of knowledge points
		if (MaxKnowPlayer == player2){
			System.out.println("You got the highest number of knowledge pts = 50 points");
			player2.score+=50;
			wait1s();
		}
		
		// if player has maximum num of happiness points
		if (MaxHappyPlayer == player2){
			System.out.println("You got the highest number of Happiness pts = 50 points");
			player2.score +=50;
			wait1s();
		}
		
		// if player has maximum num of wealth points
		if (MaxWealthPlayer == player2){
			System.out.println("You got the highest number of Wealth pts = 50 points");
			player2.score +=50;
			wait1s();
		}
		
		// loans
		System.out.println("Loans points = -" + (int)((player2.loans)*100));
		player2.score -= (int)((player2.loans)*100);
		wait1s();
		
		//calc total
		System.out.println("Total points of " + player2.name + " = "+ player2.score);
		wait3s();
		
		System.out.println("\n-------------------------------------\n");
		
		// p3 -----------------------------------------
		System.out.println("The score of "+player3.name+":");
		wait1s();
		
		// every 1K = 1 point
		System.out.println("Money points = "+player3.cash);
		player3.score += player3.cash; 
		wait1s();
		
		// every attribute point = 20 point
		System.out.println("Attributes points = "+ (int)((player3.happiness + player3.knowledge + player3.wealth)*20));
		player3.score +=(int)((player3.happiness + player3.knowledge + player3.wealth)*20);
		wait1s();
		
		// every child = 50 poits
		System.out.println("Children points = " + (int)(player3.children * 50));
		player3.score +=(int)(player3.children * 50);
		wait1s();
		
		// if player has maximum num of knowledge points
		if (MaxKnowPlayer == player3){
			System.out.println("You got the highest number of knowledge pts = 50 points");
			player3.score +=50;
			wait1s();
		}
		
		// if player has maximum num of happiness points
		if (MaxHappyPlayer == player3){
			System.out.println("You got the highest number of Happiness pts = 50 points");
			player3.score +=50;
			wait1s();
		}
		
		// if player has maximum num of wealth points
		if (MaxWealthPlayer == player3){
			System.out.println("You got the highest number of Wealth pts = 50 points");
			player3.score +=50;
			wait1s();
		}
		
		// every loan = - 100 points
		System.out.println("Loans points = -" + (int)((player3.loans)*100));
		player3.score -= (int)((player3.loans)*100);
		wait1s();
		
		// calculate total of p3
		System.out.println("Total points of " + player3.name + " = "+ player3.score);
		wait3s();
		
		System.out.println("\n-------------------------------------\n");
		
		
		// p4 -----------------------------------------
		System.out.println("The score of "+player4.name+":");
		wait1s();
		
		// every 1K = 1 point
		System.out.println("Money points = "+player4.cash);
		player4.score += player4.cash; 
		wait1s();
		
		// every attribute point = 20 point
		System.out.println("Attributes points = "+ (int)((player4.happiness + player4.knowledge + player4.wealth)*20));
		player4.score +=(int)((player4.happiness + player4.knowledge + player4.wealth)*20);
		wait1s();
		
		// every child = 50 poits
		System.out.println("Children points = " + (int)(player4.children * 50));
		player4.score +=(int)(player4.children * 50);
		wait1s();
		
		// if player has maximum num of knowledge points
		if (MaxKnowPlayer == player4){
			System.out.println("You got the highest number of knowledge pts = 50 points");
			player4.score +=50;
			wait1s();
		}
		
		// if player has maximum num of happiness points
		if (MaxHappyPlayer == player4){
			System.out.println("You got the highest number of Happiness pts = 50 points");
			player4.score+=50;
			wait1s();
		}
		
		// if player has maximum num of wealth points
		if (MaxWealthPlayer == player4){
			System.out.println("You got the highest number of Wealth pts = 50 points");
			player4.score +=50;
			wait1s();
		}
		
		// every loan = - 100 points
		System.out.println("Loans points = -" + (int)((player4.loans)*100));
		player4.score -= (int)((player4.loans)*100);
		wait1s();
		
		// calculate total of p3
		System.out.println("Total points of " + player4.name + " = "+ player4.score);
		wait3s();
		
		
		// ------------------ winner ------------------
		System.out.println("=================================");
		
		if (player1.score > player2.score && player1.score > player3.score && player1.score > player4.score)  // winner = p1
			System.out.println("---The winner is (" + player1.name +")---");
		else if (player2.score > player1.score && player2.score > player3.score && player2.score > player4.score) // winner = p2
			System.out.println("---The winner is (" + player2.name +")---");
		else if (player3.score > player1.score && player3.score > player2.score && player3.score > player4.score)
			System.out.println("---The winner is (" + player3.name +")---");
		else if (player4.score > player1.score && player4.score > player2.score && player4.score > player3.score)
			System.out.println("---The winner is (" + player4.name +")---");
		else
			System.out.println("---Draw---");
	
	}
}
