package GUI;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import schach.daten.Xml;
import schach.daten.D;
import schach.daten.D_Belegung;
import schach.backend.BackendSpielAdminStub;
import schach.backend.BackendSpielStub;

public class GUI extends Application {
	
	private Schachbrett brett;
	private boolean playerWhite;
	private static int id_spiel = 1;
	private static String pfad_spiel = "spiel1";
	private static String url = "http://www.game-engineering.de:8080/rest/";
	private static boolean logging = true;
	BackendSpielAdminStub admin = new BackendSpielAdminStub(url, logging);
	BackendSpielStub spiel = new BackendSpielStub(url, logging);

	public static void main(String[] args) {

		launch(args);

	}

	@Override
	public void start(Stage firstStage) throws Exception {
		firstStage.setTitle("Schach");
		firstStage.getIcons().add(new Image("GUI/icons/konigin-schach-stuck-schwarze-form_318-60263.jpg"));

		BorderPane root = new BorderPane();
		Scene firstScene = new Scene(root);
		firstStage.setScene(firstScene);
		firstScene.getStylesheets().add("GUI/stylesheet.css");

		brett = new Schachbrett(playerWhite);
		root.setCenter(brett);

		MenuBar menuBar = chessMenuBar();
		root.setTop(menuBar);

		VBox currentPlayerDisplay = new VBox();
		currentPlayerDisplay.setPadding(new Insets(2));
		currentPlayerDisplay.setSpacing(10);

		Text title = new Text("Aktueller Spieler:");
		title.setFont(Font.font(18));
		Text playerBlackText = new Text("Schwarz");
		playerBlackText.setFont(Font.font(16));
		Text playerWhiteText = new Text("Weiss");
		playerWhiteText.setFont(Font.font(16));
		this.displayBelegung();

		currentPlayerDisplay.getChildren().add(title);

		currentPlayerDisplay.getChildren().add(playerWhiteText);

		// if (blabla schwarzer spieler. dran)
		// currentPlayerDisplay.getChildren().add(playerBlack);

		root.setLeft(currentPlayerDisplay);

		
		firstStage.show();

	}

	private MenuBar chessMenuBar() {
		MenuBar menuBar = new MenuBar();
		Menu gameMenu = new Menu("Spiel");

		menuBar.getMenus().add(gameMenu);

		MenuItem menuItemExit = new MenuItem("Exit");
		MenuItem menuItemNewGame = new MenuItem("Neues Spiel");
		MenuItem menuItemSaveGame = new MenuItem("Speichere Spiel");
		MenuItem menuItemLoadGame = new MenuItem("Lade Spiel");

		menuItemExit.setOnAction(e -> onQuit());
		menuItemNewGame.setOnAction(e -> newGame());
		menuItemSaveGame.setOnAction(e -> saveGame());
		menuItemLoadGame.setOnAction(e -> loadGame());

		gameMenu.getItems().add(menuItemNewGame);
		gameMenu.getItems().add(menuItemLoadGame);
		gameMenu.getItems().add(menuItemSaveGame);
		gameMenu.getItems().add(menuItemExit);

		return menuBar;
	}

	private void loadGame() {
		admin.ladenSpiel(id_spiel, pfad_spiel); // geht glaube ich noch
															// nicht
	}

	private void saveGame() {
		admin.speichernSpiel(id_spiel, pfad_spiel);
	}

	private void newGame() {
		admin.neuesSpiel(id_spiel);

	}


	private void onQuit() {
		Platform.exit();
		System.exit(0);
	}

	private void displayBelegung() {
		ArrayList<D> belegung = Xml.toArray(spiel.getAktuelleBelegung(id_spiel));
		brett.updateSchachbrett(belegung);
	}
}
