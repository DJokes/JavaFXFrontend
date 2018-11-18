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
import schach.backend.BackendSpielAdminStub;
import schach.backend.BackendSpielStub;
import schach.daten.D;
import schach.daten.Xml;

public class GUI extends Application {

    private Schachbrett brett;
    private boolean playerWhite;
    private static int id_spiel = 1;
    private static String pfad_spiel = "spiel1";
    private static String url = "http://www.game-engineering.de:8080/rest/";
    private static boolean logging = true;
    BackendSpielAdminStub admin = new BackendSpielAdminStub(url, logging);
    BackendSpielStub spiel = new BackendSpielStub(url, logging);

    public static void main(final String[] args) {

	launch(args);

    }

    @Override
    public void start(final Stage firstStage) throws Exception {
	firstStage.setTitle("Schach");
	firstStage.getIcons().add(new Image("GUI/icons/konigin-schach-stuck-schwarze-form_318-60263.jpg"));

	final BorderPane root = new BorderPane();
	final Scene firstScene = new Scene(root);
	firstStage.setScene(firstScene);
	firstScene.getStylesheets().add("GUI/stylesheet.css");

	this.brett = new Schachbrett(this.playerWhite);
	root.setCenter(this.brett);

	final MenuBar menuBar = this.chessMenuBar();
	root.setTop(menuBar);

	final VBox currentPlayerDisplay = new VBox();
	currentPlayerDisplay.setPadding(new Insets(2));
	currentPlayerDisplay.setSpacing(10);

	final Text title = new Text("Aktueller Spieler:");
	title.setFont(Font.font(18));
	final Text playerBlackText = new Text("Schwarz");
	playerBlackText.setFont(Font.font(16));
	final Text playerWhiteText = new Text("Weiss");
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
	final MenuBar menuBar = new MenuBar();
	final Menu gameMenu = new Menu("Spiel");

	menuBar.getMenus().add(gameMenu);

	final MenuItem menuItemExit = new MenuItem("Exit");
	final MenuItem menuItemNewGame = new MenuItem("Neues Spiel");
	final MenuItem menuItemSaveGame = new MenuItem("Speichere Spiel");
	final MenuItem menuItemLoadGame = new MenuItem("Lade Spiel");

	menuItemExit.setOnAction(e -> this.onQuit());
	menuItemNewGame.setOnAction(e -> this.newGame());
	menuItemSaveGame.setOnAction(e -> this.saveGame());
	menuItemLoadGame.setOnAction(e -> this.loadGame());

	gameMenu.getItems().add(menuItemNewGame);
	gameMenu.getItems().add(menuItemLoadGame);
	gameMenu.getItems().add(menuItemSaveGame);
	gameMenu.getItems().add(menuItemExit);

	return menuBar;
    }

    private void loadGame() {
	this.admin.ladenSpiel(id_spiel, pfad_spiel); // geht glaube ich noch nicht
    }

    private void saveGame() {
	this.admin.speichernSpiel(id_spiel, pfad_spiel);
    }

    private void newGame() {
	this.admin.neuesSpiel(id_spiel);

    }


    private void onQuit() {
	Platform.exit();
	System.exit(0);
    }

    private void displayBelegung() {
	final ArrayList<D> belegung = Xml.toArray(this.spiel.getAktuelleBelegung(id_spiel));
	this.brett.updateSchachbrett(belegung);
    }
}
