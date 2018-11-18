package GUI;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import schach.backend.BackendSpielAdminStub;
import schach.backend.BackendSpielStub;
import schach.daten.D;
import schach.daten.Xml;

public class GUI extends Application {

    private Schachbrett brett;
    private boolean currentPlayerColor;
    private static int id_spiel = 1;
    private static String pfad_spiel = "spiel1";
    private static String rest_basis = "http://www.game-engineering.de:8080/rest/";
    private static boolean logging = true;
    BackendSpielAdminStub admin = new BackendSpielAdminStub(rest_basis, logging);
    BackendSpielStub spiel = new BackendSpielStub(rest_basis, logging);

    // Starte FX
    public static void main(final String[] args) {
	launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {

	// Spieler Schwarz startet!
	this.setCurrentPlayerColor(true);

	// Setze Titel und Favicon des Hauptfensters
	primaryStage.setTitle("Schach");
	primaryStage.getIcons().add(new Image("GUI/resources/border_image.png"));

	// Setze LayoutManager, Contentmanager, Resizable und CSS-File
	final BorderPane root = new BorderPane();
	final Scene firstScene = new Scene(root);
	primaryStage.setScene(firstScene);
	primaryStage.setResizable(false);
	firstScene.getStylesheets().add("GUI/styles_primary_stage.css");

	// Setze das Schachbrett auf die rechte Seite (preferred width ausschlaggebend)
	this.brett = new Schachbrett();
	root.setRight(this.brett);

	// Erzeuge die Toolbar am Kopf
	final MenuBar menuBar = this.chessMenuBar();
	root.setTop(menuBar);

	// Erzeuge vertikale Box (Welcher Spieler ist dran?)
	final VBox currentPlayerDisplay = new VBox();
	currentPlayerDisplay.setId("current-player-box");
	// Setze Inhalt der Box (statisch)
	final Text title = new Text("Aktueller Spieler:");
	title.setId("current-player-headline");
	final Text currentPlayerColor = new Text(this.getCurrentPlayerColorString());
	currentPlayerColor.setId("current-player-color");
	currentPlayerDisplay.getChildren().add(title);
	// Workaround wegen fehlendem margin-Attribut
	currentPlayerDisplay.getChildren().add(new Text());
	currentPlayerDisplay.getChildren().add(currentPlayerColor);

	// Setze die vertikale Box ins Layout
	root.setLeft(currentPlayerDisplay);

	// Erzeuge die Brettbelegung
	this.displayBelegung();

	// Zeige Fenster an
	primaryStage.show();

    }

    // Erzeugt die Toolbar
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

    // Lade Spiel
    private void loadGame() {
	this.admin.ladenSpiel(id_spiel, pfad_spiel); 				// geht glaube ich noch nicht
    }

    // Speichere Spiel
    private void saveGame() {
	this.admin.speichernSpiel(id_spiel, pfad_spiel);
    }

    // Erzeuge neues Spiel
    private void newGame() {
	this.admin.neuesSpiel(id_spiel);

    }

    // Verlasse Spiel
    private void onQuit() {
	Platform.exit();
	System.exit(0);
    }

    // Erzeuge das Brett dynamisch
    private void displayBelegung() {
	final ArrayList<D> belegung = Xml.toArray(this.spiel.getAktuelleBelegung(id_spiel));
	this.brett.updateSchachbrett(belegung);
    }

    // Setzt den aktuellen Spieler
    // true = black, false = white
    public void setCurrentPlayerColor(final boolean color) {
	this.currentPlayerColor = color;
    }

    // Gibt den aktuellen Spieler als Boolean zurueck
    // true = black, false = white
    public boolean getCurrentPlayerColorBool() {
	return this.currentPlayerColor;
    }

    // Gibt den aktuellen Spieler als String zurueck
    // true = black, false = white
    public String getCurrentPlayerColorString() {

	if(this.currentPlayerColor) {
	    return "schwarz";
	} else {
	    return "weiss";
	}

    }

}
