package GUI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GUI extends Application {

    private Schachbrett brett;
    private Text currentPlayerColor;

    // Starte FX
    public static void main(final String[] args) {
	launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {

	/*
	 * Die Aufgabenstellung wurde wie folgt interpretiert:
	 * Das Spiel kann auf verschiedenen Rechnern zu zweit gespielt werden.
	 * Das Spielfeld wird somit bei beiden Clients nach einem Zug aktualisiert.
	 * Sicherheit wird bei dieser Implementierung allerdings nicht berücksichtigt,
	 * da beide Clients nicht nur die eigenen Figuren steuern können, sondern auch die des Gegners.
	 * Dieses Problem ist mit unseren eingeschränkten Möglichkeiten (keinen developer-Zugriff auf den Server)
	 * nicht lösbar, da das Frontend ohne die Hilfe einer zentralen Stelle keine Unterscheidung der beiden
	 * Clients vornehmen kann. Beispiel:
	 * Client A startet die Software und wird Spieler weiß, Client B startet die Software und wird Spieler schwarz.
	 * Beenden die beiden Clients das Spiel und starten es erneut, kann nicht mehr unterschieden werden, wer
	 * welche Farbe hatte. Dies ist nur mit Hilfe einem Server möglich, der jedem Client eine spezifische
	 * Kennung gibt, anhand der man die Clients serverseitig einer Farbe zuordnen kann.
	 *
	 * Aufgrund dieses Problems bleibt eine Client-Eingabesperre in dieser Software aus.
	 */


	// Assoziation zum Schachbrett
	this.brett = new Schachbrett(this);

	// Welcher Spieler startet? -> setzen
	this.brett.getToolClass().setCurrentPlayerColor(this.brett.ermittleSpielerFarbe());

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
	this.currentPlayerColor = new Text(this.brett.getToolClass().getCurrentPlayerColorString());
	this.currentPlayerColor.setId("current-player-color");
	currentPlayerDisplay.getChildren().add(title);
	// Workaround wegen fehlendem margin-Attribut
	currentPlayerDisplay.getChildren().add(new Text());
	currentPlayerDisplay.getChildren().add(this.currentPlayerColor);

	// Setze die vertikale Box ins Layout
	root.setLeft(currentPlayerDisplay);

	// Erzeuge die Brettbelegung
	this.brett.holeAktuelleBelegung();

	// Zeige Fenster an
	primaryStage.show();

	// Empfange Neuerungen
	this.brett.vergleicheFelderForever();

	// Empfange Ereignis, wenn Fenster geschlossen wird
	// 2. Weg ueber Toolbar -> Exit
	primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	    @Override
	    public void handle(final WindowEvent e) {
		Platform.exit();
		System.exit(0);
	    }
	});

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

	menuItemExit.setOnAction(e -> this.brett.onQuit());
	menuItemNewGame.setOnAction(e -> this.brett.newGame());
	menuItemSaveGame.setOnAction(e -> this.brett.saveGame());
	menuItemLoadGame.setOnAction(e -> this.brett.loadGame());

	gameMenu.getItems().add(menuItemNewGame);
	gameMenu.getItems().add(menuItemLoadGame);
	gameMenu.getItems().add(menuItemSaveGame);
	gameMenu.getItems().add(menuItemExit);

	return menuBar;
    }

    // Gibt das Textfeld mit dem aktuellen Spieler zurueck
    public Text getPlayerColorText() {
	return this.currentPlayerColor;
    }

}
