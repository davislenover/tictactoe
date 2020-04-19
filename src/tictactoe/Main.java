/*
Davis' Tic Tac Toe - Main Launcher
This program plays the classic game, Tic Tac Toe with a bunch of customization!
This program features:
- A fully functional settings window!
- Different play modes (such as two human players or a human versus a.i.)!
- A fully customizable a.i. difficulty!
- Performance functions!
- A scoring system!
- Custom icons!
By Davis Lenover
April 14th, 2020
-----NOTES-----
Attempting to apply settings while the game is in play will not work!
If the user is in a game, either, finish the game or press the "Play" button found in the "Actions" menu. Then, make any changes desired and press the "Play" button again to apply the changes made
Please be aware that if the "Random Starting Player" and "A.I. is active" options are both selected and applied, the user must wait until the game is finished to make any changes (to which after the changes are made, press the "Play" button to apply them)
To learn more about how to play, select the "How to Play" option in the "Help" menu
*/

package tictactoe;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;

public class Main extends Application {
	@Override
	//Open game window
	public void start(Stage primaryStage) {
		try {
			System.out.println("Loading game FXML file...");
			//Load the game window, get the FXML file to load
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("TicTacToe.fxml"));
			//Create a the new scene
			Scene scene = new Scene(root,300,420);
			System.out.println("Loading game CSS file...");
			//Add CSS to that scene from the main css file for the program	
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			//Change name
			primaryStage.setTitle("Tic Tac Toe");
			//Set the icon for the pop-up
			primaryStage.getIcons().add(new Image ("file:icons/main.png"));
			primaryStage.setResizable(false);
			primaryStage.show();
			System.out.println("Game loaded!");
			//Call method to open settings window
			openStartSettingsWindow();
			
		} catch(Exception e) {
			System.out.println("An error occured while loading the game window!");
			e.printStackTrace();
		}
	}
	
	//Open Settings window along with game window
	private void openStartSettingsWindow() {
		try {
			System.out.println("Loading settings FXML file...");
			//Load the pop up, get the FXML file to load
			Pane settings = (Pane)FXMLLoader.load(getClass().getResource("Settings.fxml"));
			//Create a new scene
			Scene settingsScene = new Scene(settings,300,230);
			System.out.println("Loading settings CSS file...");
			//Add CSS to that scene from the main css file for the program	
			settings.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			//Create new stage to put the scene into
			Stage secondaryStage = new Stage();
			//Change name
			secondaryStage.setTitle("Settings");
			//Set the icon for the pop-up
			secondaryStage.getIcons().add(new Image ("file:icons/settings.png"));
			secondaryStage.setScene(settingsScene);
			secondaryStage.setResizable(false);
			secondaryStage.show();
			System.out.println("Settings loaded!");
			} catch(Exception e) {
				System.out.println("An error occured while loading the settings window!");
					e.printStackTrace();
			}
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
