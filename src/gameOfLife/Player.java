package gameOfLife;
import java.util.Scanner;

public class Player {
	public String name; // the name of the user/player
	public int cash = 200;           // the cash the player has
	public Job job = new Job("Jobless", 0,0);	// the job of the player and its salary and tax
	public int score= 0;
	
	int salary; // in case of raise .. the salary should be a player attribute too.
	
	// the attributes of the player all on there default option
	public boolean married = false, retired = false, college = false; 
	public int children = 0, loans = 0, happiness = 0, knowledge = 0, wealth = 0 ;
	
	// the houses is represented in an array that contains objects of the class house
	// because each house has different name, buying price, selling price
	public House [] housesList = new House[10];
	
	public Map playerMap = new Map(); // each player has a map to save their progress
	public int currentPostion = 1; // to save the current step the player is on
	
	public String crownKnow = ""; // is put next to the player that has most knowledge points
	public String crownWealth = "";	// is put next to the player that has most WEALTH points
	public String crownHappy = ""; // is put next to the player that has most happiness points

	// constructer to declare the players name
	public Player(String name) {
		this.name = name;
	}
	
	public String toString() { // to print the stats of the player
		return "-------------------\n" + name + " ("+job.jobTitle+") stats:\n-------------------"
				+"\nMoney: "+ cash +"K\nHappiness Points: "+happiness + crownHappy +"\n"
				+ "Knowledge Points: "+knowledge+ crownKnow +"\nWealth Points: "+ wealth + crownWealth + "\n"
						+ "Loans: " + loans + "\n-------------------\n";
	}
	
	public void setJob(Job job) {
		this.job = job;
		this.salary = job.salary;
	}
	
	public void moneyAdd(int amount) {
		System.out.println(name + "'s Money + "+ amount + "K$");
		cash += amount;
		Main.wait1s();
	}
	
	public void moneySub(int amount) {
		System.out.println(name + "'s Money - "+ amount + "K$");
		cash -= amount;
		loanChecker();
		Main.wait1s();
	}
	
	public void knowAdd(int amount) {
		System.out.println(name + "'s Knowledge + "+ amount+ " points");
		knowledge += amount;
		Main.wait1s();
	}
	
	public void happyAdd(int amount) {
		System.out.println(name + "'s Happiness + "+ amount+ " points");
		happiness += amount;
		Main.wait1s();
	}
	
	public void wealthAdd(int amount) {
		System.out.println(name + "'s Wealth + "+ amount+ " points");
		wealth += amount;
		Main.wait1s();
	}
	
	public void loanChecker() {
		while (cash < 0) {
			loans += 1;
			System.out.println("Uh oh! "+ name +" in debt! You'll need to take out a loan to keep playing");
			if (loans == 1)
				System.out.println(name +" got "+loans+" loan");
			else if (loans > 1)
				System.out.println(name +" got "+loans+" loans");
			moneyAdd(60);
		}
	}
	
	public void housePrint() {
		for (int i = 0; i < house_counter(housesList); i ++) {
			System.out.println((i+1)+"- "+ housesList[i].houseName+"\nYou bought it with "+housesList[i].buyingPrice+"K$\n"
					+ "You can Sell it for maximum = "+ housesList[i].sellingPrice + "K$");
		}
	}
	
	public static int house_counter(House [] house_list) {
		int counter = 0;
		for (int i = 0; i < house_list.length; i++) {
			if (house_list[i]!=(null))
				counter+=1;
		}
		return counter;
	}
	
	public void sellHouse(int HouseNum) {
		HouseNum--;
		int bp = housesList[HouseNum].buyingPrice; // to save the buying price simply
		int sp = housesList[HouseNum].sellingPrice; // same for selling price
		
		int actualPrice = 0; // the amount of money the player choose an recieve
		
		int [] randomList = new int[6];
		int [] invalidNum = {-1,-1,-1}; // because value - 1 .. so the 0 is -1 now
		
		// generate prices that are > buying price and < maximum selling price
		for (int i = 0; i < 6; i++) {
			randomList[i] = (int)((int)Math.ceil(bp + Math.random() * (sp-bp))); //
		}
		
		// a random index should have the maximum selling price
		randomList[(int)((Math.random()*10)%6)] = sp;
		
		
		// the player should pick number to find the best price
		// in the game the player has 3 times of guessing
		// after retirement the player have 1 guess
		System.out.println("To Sell a house you have to pick a number from 1-6\n\nEach one of these numbers "
				+ "contains a price for the house and\nthe maximum you can get = "+ housesList[HouseNum].sellingPrice + "K$");
		System.out.print("\nEnter a number from 1-6: ");
		Scanner read = new Scanner(System.in);
		int value = read.nextInt()-1;
		
		// if the player is not retired gets 3 tries
		if (retired == false) {
			for (int i = 0; i < 3; i++) { // loop 3 times
				if(i != 2) {
					// to check the value the player entered is correct
					while (value > 5 || value < 0 || Map.arrayContains(invalidNum, value)) {
						System.out.print("\nEnter a valid number from 1-6: ");
						value = read.nextInt()-1;
					}
					
					//adds the value to the invalid values list so the player cant repeat it
					invalidNum[i] = value;
									
					// returns the price of the house the player randomly chose
					actualPrice = randomList[value];
					
					// if its the maximum price sell immeditley
					if(actualPrice == sp) {
						System.out.println("Congrats!! you got an offer with the maximum Price!!");
						Main.wait1s();
						moneyAdd(actualPrice);
						break;
					}
					
					// if its not the maximum price .. print it and ask if the player gonna take it
					System.out.println("You got an offer = "+ actualPrice + "K$");
					String choice = Main.choiceChecker("Yes", "Take it", "No", "Leave it"); // a choice
					// if the player choce to take it
					if (choice.equals("a")) { 
						System.out.println("\nYour "+housesList[HouseNum].houseName+" has been sold for: "+ actualPrice + "K$");
						moneyAdd(actualPrice);
						break;
					}
					// if the player choce to try again
					else {
						System.out.print("\nEnter a new number from 1-6: ");
						read = new Scanner(System.in);
						value = read.nextInt()-1;
					}
				}
				else { // if this was the last try of the player and have to take it
					while (value > 5 || value < 0 || Map.arrayContains(invalidNum, value)) { // check
						System.out.print("\nEnter a valid number from 1-6: ");
						value = read.nextInt()-1;
					}
						
					actualPrice = randomList[value];
										
					System.out.println("this was you last try so you have to take the offer :(\n"
						+ "You got an offer = "+ actualPrice + "K$");
					Main.wait1s();
					
					System.out.println("\nYour "+housesList[HouseNum].houseName+" has been sold for: "+ actualPrice + "K$");
					moneyAdd(actualPrice);	
				}
			}
		}
		else { // if the player is retired .. have only 1 try to sell the house
			System.out.println("You only have 1 try to sell this house");
			while (value > 5 || value < 0 || Map.arrayContains(invalidNum, value)) {
				System.out.println("\nEnter a valid number from 1-6: ");
				value = read.nextInt()-1;
			}
										
			actualPrice = randomList[value];
							
			System.out.println("\nYour "+housesList[HouseNum].houseName+" has been sold for: "+ actualPrice + "K$");
			Main.wait1s();
			moneyAdd(actualPrice);
		}
		Main.wait1s();
		System.out.println("\nYou made " + (int)(actualPrice-bp)+"K$ profit");
		
		// to delete the sold house from the array
		for (int i = HouseNum; i < house_counter(housesList)-1; i ++) {
			housesList[i] = housesList[i+1]; // to put the one after it in its place and so on
		}
		
		housesList[house_counter(housesList)-1] = null; // the last one should be null
		
		System.out.println();
		Main.wait3s();
		
	}
	
	public void buyHouse(House house) {
		housesList[house_counter(housesList)] = house;
		moneySub(house.buyingPrice);
		wealthAdd(1);
	}
		
	public int compareTo(Player otherPlayer) {
        return Integer.compare(otherPlayer.score, this.score);
    }
}
