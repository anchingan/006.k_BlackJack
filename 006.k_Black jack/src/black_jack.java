/*
 * Practice 006.j_Black jack
 * Date 20170807
 */

import java.util.Scanner;
import java.util.Arrays;

public class black_jack {

	public static Scanner scanner = new Scanner(System.in);
	public static int[][] cards;
	public enum Result {win, lose, tie};
	public static int playerHold = 0, bankerHold = 0;
	
	public static void main(String[] args) {
		
		//Create cards.
		createCards();
		
		int status = 0;
		int[][] playersCards = new int [10][2], bankersCards = new int [10][2];
		int playersPoints = 0, bankersPoints = 0;
		int chips, stake = 0;
		
		System.out.print("Total chips: ");
		chips = scanner.nextInt();
		
		do {
			switch (status) {
			case 0: //Start to play game.
				//Create arrays to store player's and banker's cards.
				shuffle(cards, 52);
				playersCards = new int [10][2];
				bankersCards = new int [10][2];
				playerHold = 0;
				bankerHold = 0;
				playersPoints = 0;
				
				//Ask player to input lay of this turn.
				System.out.print("Lay: ");
				stake = scanner.nextInt();
				if (stake > chips) {
					System.out.println("Chips insufficient!");
					break;
				}
					
				
				//Deal two cards.
				System.arraycopy(drawCard(2), 0, playersCards, 0, 2);
				playerHold += 2;
				System.arraycopy(drawCard(2), 0, bankersCards, 0, 2);
				bankerHold += 2;
				System.out.println("Banker got two cards.");
				System.out.println("Player got two cards:");
				
				status = 1;
				break;

			case 1: //Player's turn.
				int input;
				do {
					//Show cards and points of player.
					printCard(playersCards, playerHold);
					playersPoints = doPoint(playersCards, playerHold);
					printPoints(playersPoints);
					if (playersPoints == 21) {
						status = 3;
						break;
					}
					if (playersPoints > 21) {
						status++;
						break;
					}
					
					//Ask player to hit or not
					do {
						System.out.print("1) Hit. 2) Stay. :");
						input = scanner.nextInt();
						if (input == 1) {
							status++;
							break;
						}
						else if (input == 2) {
							System.arraycopy(drawCard(1), 0, playersCards, (playerHold++), 1);
							break;
						}
						else 
							System.out.print("Wrong input!");
					} while (true);
				} while (status == 1);
				break;
				
			case 2: //Banker's turn.
				System.out.print("Banker's turn!\nBanker's cards:\n");
				bankersPoints = doPoint(bankersCards, bankerHold);
				while (bankersPoints < 17) {
					System.arraycopy(drawCard(1), 0, bankersCards, (bankerHold++), 1);
					bankersPoints = doPoint(bankersCards, bankerHold);
				}
				printCard(bankersCards,bankerHold);
				printPoints(bankersPoints);
				status++;
				break;
				
			case 3: //Show game result and calculate chips.
				Result gameResult;
				if (playersPoints > 21) 
					gameResult = Result.lose;
				else if (bankersPoints > 21)
					gameResult = Result.win;
				else if (playersPoints == 21) 
					gameResult = Result.win;
				else if (bankersPoints == 21)
					gameResult = Result.lose;
				else if (playersPoints == bankersPoints)
					gameResult = Result.tie;
				else if (playersPoints > bankersPoints)
					gameResult = Result.win;
				else
					gameResult = Result.lose;
				
				if (gameResult == Result.win) {
					chips += stake;
					System.out.printf("Player Won!\nYou got %d chips, you have %d chips in total!\n", 
							stake, chips);
				}
				else if (gameResult == Result.lose) {
					chips -= stake;
					System.out.printf("Banker Won!\nYou lose %d chips, you have %d chips in total!\n", 
							stake, chips);
				}
				else if (gameResult == Result.tie) {
					System.out.printf("Tied game!\nYou have %d chips in total!\n", chips);
				}
				
				status = 4;
				break;
				
			case 4:
				System.out.print("Play again? 1)Yes. 2)No.");
				input = scanner.nextInt();
				if (input == 1)
					status = 0;
				else if (input == 2)
					status = -1;
				else
					System.out.print("Wrong input!");
				break;
			}

		} while (status != -1);
		
		System.out.print("\nThanks for playing! See you!");

	}
	
	public static void createCards() {
		//cards[52][0] = suit, cards[52][1] = points, suit(0,1,2,3) = (clubs, diamonds, hearts, spades)
		cards = new int [52][2];
		
		for (int i = 0; i < 52; i++) {
			//Fill in suits.
			cards[i][0] = i / 13;
			//Fill in points.
			cards[i][1] = i % 13 + 1;
		}
	}
	
	public static void shuffle(int[][] array, int n) {
		int k; 
		int[][] temp = new int [1][2];
		for (int i = 0; i < n; i++) {
			k = (int)(Math.random()*52);
			temp[0] = array[i];
			array[i] = array[k];
			array[k] = temp[0];
		}
	}
	
	public static int[][] drawCard(int n) {
		
		int[][] drawn = new int [n][2];
		for (int i = 0; i < n; i++) 
			drawn[i] = cards[playerHold + bankerHold + i];
		
		return drawn;
	}
	
	public static void printCard(int[][] array, int n) {
		String[] suit = {"Club", "Cube", "Heart", "Spade" };
		String point;
		for (int i = 0; i < n; i++) {
			//decide point
			if (array[i][1] == 1)
				point = "Ace";
			else if (array[i][1] == 11)
				point = "Jack";
			else if (array[i][1] == 12)
				point = "Queen";
			else if (array[i][1] == 13)
				point = "King";
			else
				point = Integer.toString(array[i][1]);
			
			System.out.printf("%s %s", suit[array[i][0]], point);
			if (i < n - 1)
				System.out.print(", ");
		}
	}
	
	public static int doPoint(int[][] array, int n) {
		int total = 0;
		for (int i = 0; i < n; i++) {
			if (array[i][1] == 1) //Ace
				total += 11;
			else if (array[i][1] == 11) //jack
				total += 10;
			else if (array[i][1] == 12) //queen
				total += 10;
			else if (array[i][1] == 13) //King
				total += 10;
			else
				total += array[i][1] ;
		}
		
		//If total points is more than 21, check if there is any ace.
		if (total > 21) {
			for (int i = 0; i < n; i++) 
				if (array[i][1] == 1) {
					total -= 10;
					if (total <= 21)
						break;
				}
		}
		return total;
	}
	
	public static void printPoints(int n) {
		if (n == 21)
			System.out.printf("\nPoints: %d (Black Jack!)\n", n);
		else if (n > 21)
			System.out.printf("\nPoints: %d (Busted!)\n", n);
		else
			System.out.printf("\nPoints: %d\n", n);
	}
	

}
