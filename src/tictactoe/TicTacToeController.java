/*
Davis' Tic Tac Toe - Controller
This script (part of the program) handles all the logic behind the game and it's functions
By Davis Lenover
April 14th, 2020
*/

package tictactoe;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class TicTacToeController {
	
	//Define main game variables (check which player is currently active and if anyone has won)
	public int currentPlayer = 1;
	public int isWinner = 0;
	
	//Define start of game and end of game for changing settings
	public static boolean isGameAtStart = true;
	public static boolean isGameAtEnd = false;
	
	//Define new stages for window pop-ups
	static Stage secondaryStage;
	static Stage tertiaryStage;
	static Stage quaternaryStage;
	
	//Tic Tac Toe board buttons
	@FXML Button b0;
	@FXML Button b1;
	@FXML Button b2;
	@FXML Button b3;
	@FXML Button b4;
	@FXML Button b5;
	@FXML Button b6;
	@FXML Button b7;
	@FXML Button b8;
	
	@FXML GridPane gameBoard;
	
	//Settings checkboxes
	@FXML CheckBox aiCheck;
	@FXML CheckBox randomPlayer;
	@FXML CheckBox secondPlayer;
	@FXML CheckBox printAI;
	public static boolean isAIOutput = false;
	@FXML Slider difficultySlider;
	@FXML Button resetScore;
	
	//Settings booleans which are linked to the checkboxes
	public static boolean isAIActive = false;
	public static boolean isPlayerStartRandom = false;
	public static boolean isPlayer2Start = false;
	public static int aiDifficulty = 0;
	
	//Point counters
	@FXML TextField oWinCounter;
	@FXML TextField xWinCounter;
	@FXML TextField tieCounter;
	
	//Point counters linked to textfields
	static int oWin = 0;
	static int xWin = 0;
	static int tieCount = 0;
	
	//Define button array in the form of a "magic square", any diagonal/horizontal will add up to 15, confirming a win
	int[] magicSquare = new int[] {8,1,6,
								   3,5,7,
								   4,9,2};
	
	//Define the board the players will use, acts as an array
	String[] playerBoard = new String[] {" "," ", " ",
										" ", " ", " ",
										" ", " ", " "};
										
	//Buttons on game board when clicked are handled in this method
	public void buttonClickHandler(ActionEvent evt) {
		
		Button clickedButton = (Button) evt.getTarget();
		String buttonLabel = clickedButton.getText();
		String index;
		
		//If A.I. is active (based on user settings)
		if (isAIActive) {
			//Check to see if the button on the board selected is empty and that the current player is selected
			if ("".equals(buttonLabel) && currentPlayer == 1) {
				//The fx:id of the button matches it's position on the array and is used to determine where to place the letter (X or O)
				index = clickedButton.getId();
				//returnIndexValue will remove the "b" in front of the fx:id of the button, as it's number indicates it's index value
				playerBoard[returnIndexValue(index)] = "O";
				clickedButton.setText("O");
				//Once a letter is placed, check to see if someone has won
				isWinner = find3InArow();
				//If no one has won yet (or tied), find3InArow will return 4
				if (isWinner == 4) {
					//Reset integer to 0
					isWinner = 0;
					//Change to the next player
					currentPlayer = 2;
					//Since the user has defined that the a.i. is active, the aiTurn function is called. The a.i. will make it's move
					aiTurn();
					//Once the a.i. has made it's move a check will be made once again if anyone has won
					isWinner = find3InArow();	
				}
			}
		}
		
		//Non a.i. games are handled here
		//If A.I. is not active, the current player is #1 and the button selected has not been selected previously...
		if ("".equals(buttonLabel) && currentPlayer == 1 && !isAIActive) {
				//Get the fx:id of the button and perform the actions to place a letter down
				index = clickedButton.getId();			
				playerBoard[returnIndexValue(index)] = "O";
				clickedButton.setText("O");
				isWinner = find3InArow();
				currentPlayer = 2;
				//If the game just started and a letter was placed, set the boolean to false
				if (isGameAtStart) {
					isGameAtStart = false;
				}
			//Second Player if statement and performs the actions to place a letter down
			} else if ("".equals(buttonLabel) && currentPlayer == 2 && !isAIActive) {
				index = clickedButton.getId();			
				playerBoard[returnIndexValue(index)] = "X";
				clickedButton.setText("X");
				isWinner = find3InArow();
				currentPlayer = 1;
				//isGameAtStart is checked here because a random starting player is possible based on the users settings
				if (isGameAtStart) {
					isGameAtStart = false;
				}
			}
		//Win states are handled here
		//1 - X Wins, 2 - O Wins, 3 - Tie, 4 - No Winner as of yet
		if (isWinner == 1) {
			isGameAtEnd = true;
			currentPlayer = 0;
			//Give X a point
			xWin++;
			//Call the method to update the score on the program (textfields)
			scoreKeeper();
			System.out.println("X Wins!");
			isWinner = 0;
		} else if (isWinner == 2) {
			isGameAtEnd = true;
			currentPlayer = 0;
			oWin++;
			scoreKeeper();
			System.out.println("O Wins!");
			isWinner = 0;
		} else if (isWinner == 3) {
			isGameAtEnd = true;
			currentPlayer = 0;
			tieCount++;
			scoreKeeper();
			System.out.println("It's a tie!");
			isWinner = 0;
		}
			
	}
	
	//Method to update "score board" on-screen
	private void scoreKeeper() {
		//Convert integers to strings and update each textfield accordingly
		String setTie = Integer.toString(tieCount);
		tieCounter.setText(setTie);
		String setXWin = Integer.toString(xWin);
		xWinCounter.setText(setXWin);
		String setOWin = Integer.toString(oWin);
		oWinCounter.setText(setOWin);
		
	}
	
	//Method to remove the "b" from each game board button fx:id since the number in their id's correspond to the playerBoard array
	public int returnIndexValue (String buttonID) {
		int indexValue;
		//.substring(1) removes the first letter in the string
		indexValue = Integer.parseInt(buttonID.substring(1));
		return indexValue;
	}
	
	//Method used by the a.i. to place a letter on a button
	public void setButtonText(int buttonID) {
		//switch statement for which value the a.i. returns and a letter will be placed on the corresponding value
		switch (buttonID) {
		case 0:
			b0.setText("X");
			break;
		case 1:
			b1.setText("X");
			break;
		case 2:
			b2.setText("X");
			break;
		case 3:
			b3.setText("X");
			break;
		case 4:
			b4.setText("X");
			break;
		case 5:
			b5.setText("X");
			break;
		case 6:
			b6.setText("X");
			break;
		case 7:
			b7.setText("X");
			break;
		case 8:
			b8.setText("X");
			break;
		}
	}
	
	//Method used by the functions which return a string button value and need a type Button to call a function that requires a button type in it's arguments
	public Button returnButton (String buttonID) {
		switch (buttonID) {
		case ("b0"):
			return b0;
		case ("b1"):
			return b1;
		case ("b2"):
			return b2;
		case ("b3"):
			return b3;
		case ("b4"):
			return b4;
		case ("b5"):
			return b5;
		case ("b6"):
			return b6;
		case ("b7"):
			return b7;
		case ("b8"):
			return b8;
		default:
			return b0;
		}
	}
	
	//Method for checkboxes in the settings window is dealt with here
	public void settingClickHandler(ActionEvent evt) {
		//Check to make sure the game is at the start or just finished (if a setting is changed during play, instability may result)
		if (isGameAtStart || isGameAtEnd) {
			//Set appropriate booleans to their checkboxes
			//Is the a.i. active?
			isAIActive = aiCheck.isSelected();
			//Is the player who is starting to be chosen at random?
			isPlayerStartRandom = randomPlayer.isSelected();
			//Should the console output the decisions the A.I. is making internally?
			isAIOutput = printAI.isSelected();
			//Print to console, verifying settings
			System.out.println("A.I. as player 2 has been set to: " + isAIActive);
			System.out.println("Random player going first has been set to: " + isPlayerStartRandom);
			System.out.println("A.I. computing output to console has been set to: " + isAIOutput);
			//There are two settings that can contradict each-other as one will randomize who will go first and another which will force player 2 to go first
			//This if statement is to check if the random player start option is selected and the user attempts to set force player 2 to true, which will not be possible
			if (isPlayerStartRandom && secondPlayer.isSelected()) {
				//Warm user
				System.out.println("Please disable random player start to force player 2");
				//Set player 2 option to false 
				secondPlayer.setSelected(false);
				isPlayer2Start=false;
			//If the random player option is not selected, allow the user to set force player 2 to true
			} else if (!isPlayerStartRandom) {
				isPlayer2Start=secondPlayer.isSelected();
				System.out.println("Force player 2 start: " + isPlayer2Start);
			}
		//If the user is currently playing a game and wishes to change settings, this will be displayed in console
		} else {
			System.out.println("Please restart (by pressing the Play button in the Actions Menu) or finish the game before making changes!");
		}
	}
	
	//Reset score method if the button in settings is pressed
	public void resetScores(ActionEvent evt) {
		//Reset variables
		oWin = 0;
		xWin = 0;
		tieCount = 0;
		//Output completion to console
		System.out.println("Score has been reset!");
		//NOTE: Score changes and anything applied in settings will NOT take affect until the user clicks "Play" in the "Actions" menu!
	}
	
	//Method to deal with difficulty slider in settings
	public void difficultysettingClickHandler(MouseEvent evt) {
		//Check to make sure user is not in the middle of a game
		if (isGameAtStart || isGameAtEnd) {
			//Set appropriate variable to slider value (parse to integer for a whole number as it's easier to deal with)
			aiDifficulty = (int) difficultySlider.getValue();
			//Output completion to console
			System.out.println("A.I. difficulty has been set to: " + aiDifficulty);
		//Warn user if they are mid-game (and not make changes to any variables)
		} else {
			System.out.println("Please restart or finish the game before making changes!");
		}
	}
	
	//Method for dealing with the menu
	public void menuClickHandler(ActionEvent evt) {
		MenuItem clickedMenu = (MenuItem) evt.getTarget();
		String menuLabel = clickedMenu.getText();
		
		//Check which menu option was selected
		//"Play" resets the game
		if ("Play".equals(menuLabel)) {
			//Call scoreKeeper to update the score values if need be (such as if a user reset them in settings)
			scoreKeeper();
			//Since the game is at the start, set booleans appropriately 
			isGameAtStart = true;
			isGameAtEnd = false;
			
			//This is where the random player will be called from (depending on the users settings)
			if (isPlayerStartRandom && !isPlayer2Start) {
				//Set current player to method determining random player
				currentPlayer = randomStartingPlayer();
				//Print to console which player will start
				System.out.println("Player who will start the game is player #: " + currentPlayer);
			//If neither settings changing the default player 1 start, set player 1 to start
			} else if (!isPlayerStartRandom && !isPlayer2Start){
				currentPlayer = 1;
				System.out.println("Player 1 (O) will start");
			//If the force player 2 setting is set to true, player 2 will begin the game
			} else if (!isPlayerStartRandom && isPlayer2Start) {
				currentPlayer = 2;
				System.out.println("Player 2 (X) will start");
			}
			ObservableList<Node> buttons = gameBoard.getChildren();
			
			//Reset game board button text
			buttons.forEach(btn -> {
				((Button) btn).setText("");
				
				//Remove any style class changes from the end of the game
				btn.getStyleClass().remove("winning-button");
				btn.getStyleClass().remove("tie-button");
				btn.setStyle("-fx-text-fill: blue");
			});
			
			//Reset game board (playerBoard) array for the next game
			playerBoard = new String[] {" "," ", " ",
										" ", " ", " ",
										" ", " ", " "};
			
			//If player two starts and the a.i. is active, start the game with the a.i. moving first
			if (currentPlayer == 2 && isAIActive) {
				//Set game start to false
				isGameAtStart = false;
				//Call a.i. to make a move
				aiTurn();
				//Check if a win state occurred
				find3InArow();
			}
		}
		
		//Separate pop-up windows and other program behaviors in the menu are dealt with here
		//Check to see if any option selected in the menu corresponds to any labels mentioned here
		if ("How to Play".equals(menuLabel)) {
			//Call method to open specific window
			openHowToWindow();
		} else if ("Quit".equals(menuLabel)) {
			//Quit the game
			System.out.println("Quitng...");
			//Exit all scenes
			Platform.exit();
			//Terminate the program
			System.exit(0);
		} else if ("About".equals(menuLabel)) {
			openAboutWindow();
		} else if ("Settings".equals(menuLabel)) {
			openSettingsWindow();
		}
		
	}
	
	//Random player going first selection is made here
	public int randomStartingPlayer() {
		//Get a random integer between 1 and 2
		int rand = 1 + (int)(Math.random()*((2-1)+1));
		//Return the value
		return rand;
	}
	
	//Method to open the settings pop-up window (where the player can make changes to the game)
	private void openSettingsWindow() {
		try {
			//Load the pop up, get the FXML file to load
			Pane settings = (Pane)FXMLLoader.load(getClass().getResource("Settings.fxml"));
			//Create the new scene
			Scene settingsScene = new Scene(settings,300,230);
			//Add CSS to that scene from the main css file for the program	
			settings.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			//Create new stage to put the scene into
			secondaryStage = new Stage();
			//Change name
			secondaryStage.setTitle("Settings");
			//Set the icon for the pop-up
			secondaryStage.getIcons().add(new Image ("file:icons/settings.png"));
			secondaryStage.setScene(settingsScene);
			secondaryStage.setResizable(false);
			secondaryStage.show();
			System.out.println("Settings loaded!");
			} catch(Exception e) {
					e.printStackTrace();
			}
	}
	
	//Method to open the About pop-up window, same setup as settings window except with a different stage to avoid window interference
	private void openAboutWindow() {
		try {
			Pane about = (Pane)FXMLLoader.load(getClass().getResource("About.fxml"));
			Scene aboutScene = new Scene(about,300,150);	
			aboutScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			tertiaryStage = new Stage();
			tertiaryStage.setTitle("About");
			tertiaryStage.getIcons().add(new Image ("file:icons/question.png"));
			tertiaryStage.setScene(aboutScene);
			tertiaryStage.setResizable(false);
			System.out.println("About loaded!");
			tertiaryStage.showAndWait();
			} catch(Exception e) {
					e.printStackTrace();
			}
	}
	
	//Method to open the How To Play pop-up window, same setup as settings window except with a different stage to avoid window interference
	private void openHowToWindow() {
		try {
			Pane howTo = (Pane)FXMLLoader.load(getClass().getResource("HowToPlay.fxml"));
			Scene howToScene = new Scene(howTo,300,250);	
			howToScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			quaternaryStage = new Stage();
			quaternaryStage.setTitle("How to Play");
			quaternaryStage.getIcons().add(new Image ("file:icons/question.png"));
			quaternaryStage.setScene(howToScene);
			quaternaryStage.setResizable(false);
			System.out.println("How to Play loaded!");
			quaternaryStage.showAndWait();
			} catch(Exception e) {
					e.printStackTrace();
			}

	}
	
	//Method to close both the how-to and about windows (to avoid windows closing each-other). The Settings window can be closed just by pressing the X button.
	public void closeWindowButtonClickHandler(ActionEvent evt) {
		Button clickedButton = (Button) evt.getTarget();
		//Check which close button was pressed and close the scene
		if (clickedButton.getId().equals("alertButtonAbout")) {
			tertiaryStage.close();
		} else if (clickedButton.getId().equals("alertButtonHowTo")) {
			quaternaryStage.close();
		}
	}

	//Different method to check if someone has won or tied is completed here (instead of the brute force method)
	//Instead, the magic square method is used to find a winner
	private int find3InArow() {
		//Test each index the player has entered for magic number, using three for loops
		for (int sumValue = 0; sumValue < 9; sumValue++) {
			for (int sumValue2 = 0; sumValue2 < 9; sumValue2++) {
				for (int sumValue3 = 0; sumValue3 < 9; sumValue3++) {
					//If none of the magic numbers are equal to each-other and all the spaces placed on the board are the same letter, add them up, if 15 is the result, the player has won
					if (sumValue != sumValue2 && sumValue != sumValue3 && sumValue2 != sumValue3) {
						if (playerBoard[sumValue]=="X" && playerBoard[sumValue2]=="X" && playerBoard[sumValue3]=="X"){
							//Magic square array is laid out exactly how the game board is and in essence, each button index has it's own value and any diagonal added up will equal 15
							if (magicSquare[sumValue] + magicSquare[sumValue2] + magicSquare[sumValue3] == 15) {
								//Change CSS on the winning buttons (getting the actual names of the buttons by calling the returnButton method)
								highlightWinningCombo(returnButton("b"+sumValue), returnButton("b"+sumValue2), returnButton("b"+sumValue3));
								//Return 1 to indicate X has won
								return 1;
							}
						//If each spot on the board checked is all "O"s instead of "X"s, check to see if all the "O"s being tested add up to 15
						} else if (playerBoard[sumValue]=="O" && playerBoard[sumValue2]=="O" && playerBoard[sumValue3]=="O") {
							if (magicSquare[sumValue] + magicSquare[sumValue2] + magicSquare[sumValue3] == 15) {
								highlightWinningCombo(returnButton("b"+sumValue), returnButton("b"+sumValue2), returnButton("b"+sumValue3));
								//Return 2 to indicate O has won
								return 2;
							}
						} 
					}
				}
			}
			
		}
		//This part checks to see if a tie has occurred
		boolean checkIfTie = false;
		//Loop through all game board spaces
		for (int checkEmpty = 0; checkEmpty < 9; checkEmpty++) {
			//If any spaces are still empty, there is still spaces to be played by each player and therefore, there is no tie
			if (playerBoard[checkEmpty] == " ") {
				//Set the tie boolean to false
				checkIfTie = false;
				//Break the for loop 
				break;
			}
			//If the for loop goes through every space and finds no empty spot, checkIfTie will be set to true
			checkIfTie = true;
		}
		//If a tie is confirmed...
		if (checkIfTie){
			//Change CSS of entire board
			highlightTie();
			//Return 3 meaning a tie
			return 3;
		}
		//If all other loops and if statements fail to return anything, the method will automatically return 4, indicating there is no win state
		return 4;
	}
	
	//Method to change CSS for winning combination of buttons on the game board
	private void highlightWinningCombo(Button first, Button second, Button third) {
		//Add winning-button CSS class to the appropriate buttons
		first.getStyleClass().add("winning-button");
		//Fill the text with red too
		first.setStyle("-fx-text-fill: red");
		second.getStyleClass().add("winning-button");
		second.setStyle("-fx-text-fill: red");
		third.getStyleClass().add("winning-button");
		third.setStyle("-fx-text-fill: red");
	}
	
	//Method to change CSS for a tie
	private void highlightTie() {
		//Add tie-button CSS class to all buttons
		b0.getStyleClass().add("tie-button");
		b1.getStyleClass().add("tie-button");
		b2.getStyleClass().add("tie-button");
		b3.getStyleClass().add("tie-button");
		b4.getStyleClass().add("tie-button");
		b5.getStyleClass().add("tie-button");
		b6.getStyleClass().add("tie-button");
		b7.getStyleClass().add("tie-button");
		b8.getStyleClass().add("tie-button");
	}
	
	//All of the methods below deal with the functions of the A.i. and how it calculates which moves to make
	//When the a.i. has been set to active, this is where the first method called when the a.i. begins its move is defined
	public void aiTurn() {
		//Double-check to make sure the current player is the a.i. (#2) to avoid any glitches/bugs
		if (currentPlayer == 2) {
			System.out.println("A.I. is calculating it's move...");
			//In the settings window, the user can configure how difficult they want the a.i. to be, any number from 0-100 with 0 being very easy and 100 being impossible
			//The a.i. will generate (randomly) it's own number between 1-100 and compare it to the difficulty set by the user. If the user chooses a difficulty of 0, the a.i. will only make random non-calculated moves
			int difficultySelector = (int)(Math.random()*(100-1)+1);
			//If the random number generated is smaller than or equal to the slider difficulty set, the computer will make a calculated move
			if (difficultySelector <= aiDifficulty) {
				//Test to see if the user has checked the debug option in the settings window
				if (isAIOutput) {
					//If the setting was checked, then all the calculations made the by a.i. will be displayed to the console
					System.out.println("A.I. is calculating the best move possible");
				}
				System.out.println("Hold tight, this may take sometime!");
				//Since the a.i. is going to make a calculated move, the program will call the bestMove method to do so, it will return the index value of the button the a.i. thinks is the best move to make given the current situation
				int moveToMake = bestMove();
				//Mark the position the a.i. will perform on the game board
				playerBoard[moveToMake] = "X";
				//Set the appropriate button text
				setButtonText(moveToMake);
				//Change current player (not the a.i.'s turn anymore)
				currentPlayer = 1;
			//If the random number generated is larger than or equal to the slider difficulty set, the computer will make a random (easy) move
			} else if (difficultySelector >= aiDifficulty) {
				if (isAIOutput) {
					System.out.println("A.I. is calculating a random move");
				}
				//Since the a.i. is going to make a random move (easy), the program will call the randomMove method to do so, it will return the index value of the button the a.i. will randomly press
				int moveToMake = randomMove();
				playerBoard[moveToMake] = "X";
				setButtonText(moveToMake);
				currentPlayer = 1;
			}
		}
	}
	
	//This is the method which the a.i. will make a random move on the game board if triggered to do so
	public int randomMove() {
		//By default, to run the while loop, set the spot available boolean to false
		boolean isSpotAvailable = false;
		while (!isSpotAvailable) {
			//Generate a random number between 0-8. These numbers are the index values of the board
			int checkTile = 0 + (int)(Math.random()*((8-0) + 1));
			//Is the spot available?
			if (playerBoard[checkTile]==" ") {
				//If it is, place "X"
				playerBoard[checkTile]="X";
				//Stop while loop
				isSpotAvailable = true;
				System.out.println("System has determined button index: "+ checkTile);
				//Return tile number to display to screen
				return checkTile;
			}
		}
		//By default, if the method encounters an error, it will return 0
		return 0;
	}

	//If the a.i. has been determined to make a calculated move, this is the method that will be called to do so
	//The calculated algorithm the a.i. will use is known as Minimax. It will visualize all possible moves it can make and play simulations of the game to determine the best move possible by evaluating each of the moves (given a score)
	public int bestMove() {
		int move = 0;
		//The a.i. is going to try and find the best score possible based on the moves it can make, the highest score indicates winning moves for the a.i., this is known as it "maximizing" (the "max" part in minimax)
		//Set the best score to negative infinity to start off so right off the start, the a.i. will make a high score to beat
		double bestScore = Double.NEGATIVE_INFINITY;
		//AI Determines (It is the A.I. (X)s turn)
		//Check all open positions using a for loop
		for (int spotIndex = 0; spotIndex < 9; spotIndex++) {
			//Is the spot available?
			if (playerBoard[spotIndex]==" ") {
				//If the spot is available on the game board, place a letter down on the array to test that position
				playerBoard[spotIndex]="X";
				if (isAIOutput) {
					System.out.println("Testing Move on index: " + spotIndex);
				}
				//The game board is then copied into the minimax method to create a simulation of the game at a depth of 0 (each new move is one depth, it's like a tree diagram)
				//Since it has finished "maximizing" it's score it will try and get the lowest score possible now (hence why it takes in false as the boolean), the lowest score indicates loosing moves against the player, known as "minimizing" (the "mini" in minimax)
				double score = minimaxTest(playerBoard, 0, false);
				//Remove the move from the actual board since the a.i. does not want to manipulate that, it wants to play with it's own copy so none of the actual game is affected
				playerBoard[spotIndex]=" ";
				//In this "maximizing" method, if the score the a.i. finds (based on the minimax method) is higher than the bestScore, then that move that was tested is the best for the a.i.
				if (score > bestScore) {
					//Set the new bestScore
					bestScore = score;
					//Since the move tested was good, make it the new move value to return
					move = spotIndex;
				}
			}
			if (isAIOutput) {
				System.out.println("Button #: " + spotIndex + " Score: " + bestScore);
			}
		}
		System.out.println("System has determined button index: " + move);
		return move;
	}
	
	//These are the possible scores the a.i. can get, with a X win being a +10 score (the a.i. winning), O being a -10 score (the user winning) and a tie being a score of 0
	//1 - X, -1 - O, 0 - Tie
	int[] scores = new int[] {10,-10,0};
	
	//This method takes in the move the bestMove method gave and plays out actual simulations of the game by calling itself as many times as needed to determine the absolute best move possible
	public double minimaxTest (String[] board, int depth, boolean isMaximizing) {
		//Firstly check if someone has won
		//Call function to test the current possible placement (based on the move that the bestMove method gave to this method) and see if anyone won
		int result = findAdvanceWin(board);
		//If someone has won, the function will not return 4 but instead, the index at which player won or tied
		if (result != 4) {
			//(scores array) to give score points
			return scores[result];
		}
		
		//If the win check method (findAdvanceWin) returns a value of 4, it means no one has won or tied yet so the function will continue on
		//As mentioned earlier, the method will continue to call itself and switch between "minimizing" (the users moves) and maximizing (the a.i.'s moves)
		//If the minimaxTest method took in true as its boolean argument, then the following below will happen. It is the a.i.'s theoretical turn
		if (isMaximizing) {
			//Set the high score to the lowest possible so the a.i. can get a score right off the start
			double bestScore = Double.NEGATIVE_INFINITY;
			//For loop to choose a position on the copied board
			for (int spotIndex = 0; spotIndex < 9; spotIndex++) {
				if (board[spotIndex]==" ") {
					board[spotIndex]="X";
					//Call itself to determine a score based on the move and create another copy of the board which will create a new depth (again, it's pretty much creating a tree diagram of all possibilities)
					//Now since the a.i. has theoretically played, it is the players theoretical turn so false will be inputed as the boolean argument as the a.i. is now "minimizing"
					double score = minimaxTest(board, depth + 1, false);
					//Even though this is a copy of another copy of the game board, we still set the spot to be blank again to save on some memory (as this process can be resource heavy depending on how many calculations the a.i. needs to make)
					board[spotIndex]=" ";
					//Get the best score possible based on what is returned from itself
					bestScore = Math.max(score, bestScore);
				}
				if (isAIOutput) {
					System.out.println("Button #: " + spotIndex + " Score: " + bestScore);
				}
			}
			if (isAIOutput) {
				System.out.println("Best Score: " + bestScore);
				System.out.println("Depth: " + depth);
			}
			//Return the best score it calculated
			return bestScore;
		//If it is the players theoretical turn, the a.i. is now "minimizing"
		} else {
			//Set the high score the infinity (because the a.i. wants to get the lowest score possible here)
			double bestScore = Double.POSITIVE_INFINITY;
			//Do the exact same thing but the a.i. is acting like the player now so it will place down a "O" and see if the player can win from the next (or this current "O" placement) depth
			for (int spotIndex = 0; spotIndex < 9; spotIndex++) {
				if (board[spotIndex]==" ") {
					board[spotIndex]="O";
					//a.i. is now going to make it's theoretical play so a boolean of true is passed over while the function calls itself again
					double score = minimaxTest(board, depth + 1, true);
					board[spotIndex]=" ";
					//Get the lowest possible score
					bestScore = Math.min(score, bestScore);
					if (isAIOutput) {
						System.out.println("Button #: " + spotIndex + " Score: " + bestScore);
					}
				}
			}
			if (isAIOutput){
				System.out.println("Best Score: " + bestScore);
				System.out.println("Depth: " + depth);
			}
			//Return that lowest score (in this case the "best (lowest) score")
			return bestScore;
		}	
	}
	
	//This method functions exactly like the real find3InARow method, it's return values have been changed to match the corresponding index' of the scores array for points to help the a.i.
	public int findAdvanceWin(String[] board) {
		for (int sumValue = 0; sumValue < 9; sumValue++) {
			for (int sumValue2 = 0; sumValue2 < 9; sumValue2++) {
				for (int sumValue3 = 0; sumValue3 < 9; sumValue3++) {
					if (sumValue != sumValue2 && sumValue != sumValue3 && sumValue2 != sumValue3) {
						if (board[sumValue]=="X" && board[sumValue2]=="X" && board[sumValue3]=="X"){
							if (magicSquare[sumValue] + magicSquare[sumValue2] + magicSquare[sumValue3] == 15) {
								//X theoretically wins
								return 0;
							}
						} if (board[sumValue]=="O" && board[sumValue2]=="O" && board[sumValue3]=="O") {
							if (magicSquare[sumValue] + magicSquare[sumValue2] + magicSquare[sumValue3] == 15) {
								//O theoretically wins
								return 1;
							}
						} 
					}
				}
			}	
		}
		boolean checkIfTie = false;
		for (int checkEmpty = 0; checkEmpty < 9; checkEmpty++) {
			if (board[checkEmpty] == " ") {
				checkIfTie = false;
				break;
			}
			checkIfTie = true;
		}
		if (checkIfTie){
			//It is a theoretical tie
			return 2;
		}
		//Neither player has theoretically won (or tied) yet
		return 4;
	}
}
//End of Controller

