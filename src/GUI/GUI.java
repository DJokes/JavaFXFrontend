package GUI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GUI extends Application {

    private Schachbrett brett;
    private Text currentPlayerColor, currentStatus;
    private Stage primaryStage;

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
	 * Sicherheit wird bei dieser Implementierung nicht berücksichtigt, da jeder Client
	 * durch einen Neustart der Software die gegnerischen Figuren steuern könnte.
	 *
	 * Beispiel:
	 * Client A startet die Software und wird Spieler weiß, Client B startet die Software und wird Spieler schwarz.
	 * Beenden die beiden Clients das Spiel und starten es erneut, kann nicht mehr unterschieden werden, wer
	 * welche Farbe hatte. Dies ist nur mit Hilfe eines Server möglich, der jedem Client eine spezifische
	 * Kennung gibt, anhand der man die Clients serverseitig einer Farbe zuordnen kann.
	 *
	 * Da dieses Problem mit den uns zur Verfügung stehenden Mitteln nicht lösbar ist,
	 * bleibt dieses Sicherheitsrisiko bestehen.
	 *
	 */

	// Hauptfenster global setzen
	this.primaryStage = primaryStage;

	// Assoziation zum Schachbrett
	this.brett = new Schachbrett(this);

	// Welcher Spieler startet? -> setzen
	this.brett.getToolClass().setCurrentPlayerColor(this.brett.ermittleSpielerFarbe());

	// Fragt Farbe ab
	this.showColorChooser();

    }

    // Zeigt das Spielfeld an
    private void showGameWindow() {

	// Setze Titel und Favicon des Hauptfensters
	this.primaryStage.setTitle("Schach");
	this.primaryStage.getIcons().add(new Image("GUI/resources/border_image.png"));

	// Setze LayoutManager, Contentmanager, Resizable und CSS-File
	final BorderPane root = new BorderPane();
	final Scene firstScene = new Scene(root);
	this.primaryStage.setScene(firstScene);
	this.primaryStage.setResizable(false);
	firstScene.getStylesheets().add("GUI/styles_primary_stage.css");

	// Setze das Schachbrett auf die rechte Seite (preferred width ausschlaggebend)
	root.setRight(this.brett);

	// Erzeuge die Toolbar am Kopf
	final MenuBar menuBar = this.chessMenuBar();
	root.setTop(menuBar);

	// Erzeuge vertikale Box (Welcher Spieler ist dran?, Welchen Status hat das Spiel?)
	final VBox sideBarInfo = new VBox();
	sideBarInfo.setId("sidebar-box");

	final Text title = new Text("Aktueller Spieler:");
	title.setId("sidebar-headline");
	this.currentPlayerColor = new Text(this.brett.getToolClass().getCurrentPlayerColorString());
	this.currentPlayerColor.setId("sidebar-subtitle");
	sideBarInfo.getChildren().add(title);
	VBox.setMargin(this.currentPlayerColor, new Insets(10, 0, 0, 0));
	sideBarInfo.getChildren().add(this.currentPlayerColor);

	final Text statusTitle = new Text("Aktueller Status:");
	VBox.setMargin(statusTitle, new Insets(40, 0, 0, 0));
	statusTitle.setId("sidebar-headline");
	this.currentStatus = new Text();
	this.currentStatus.setId("sidebar-subtitle");
	sideBarInfo.getChildren().add(statusTitle);
	VBox.setMargin(this.currentStatus, new Insets(10, 0, 0, 0));
	sideBarInfo.getChildren().add(this.currentStatus);


	// Setze die vertikale Box ins Layout
	root.setLeft(sideBarInfo);

	// Erzeuge die Brettbelegung
	this.brett.holeAktuelleBelegung();

	// Zeige Fenster an
	this.primaryStage.show();

	// Empfange Neuerungen
	this.brett.vergleicheFelderForever();

	// Empfange Ereignis, wenn Fenster geschlossen wird
	// 2. Weg ueber Toolbar -> Exit
	this.primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	    @Override
	    public void handle(final WindowEvent e) {
		Platform.exit();
		System.exit(0);
	    }
	});

    }

    // Zeigt die Farbauswahl an
    public void showColorChooser() {

	// Erzeuge neues Fenster
	final Stage tempStage = new Stage();

	// Setze Titel und Favicon des Abfragefensters
	tempStage.setTitle("Schach - Farbwahl");
	tempStage.getIcons().add(new Image("GUI/resources/border_image.png"));

	// Setze LayoutManager, Contentmanager, Resizable und CSS-File
	final BorderPane root = new BorderPane();
	final Scene firstScene = new Scene(root, 300, 168);
	tempStage.setScene(firstScene);
	tempStage.setResizable(false);
	firstScene.getStylesheets().add("GUI/styles_color_chooser.css");

	// Erzeuge horizontale Box (Welcher Spieler ist dran?)
	final HBox colorContainer = new HBox();
	colorContainer.setId("color-container");

	// Positioniere Elemente zentriert
	colorContainer.setAlignment(Pos.CENTER);

	// Setze Platz zwischen jedem Element
	colorContainer.setSpacing(4);

	// Setze Inhalt der Box (statisch)
	final VBox black = new VBox();
	black.setAlignment(Pos.CENTER);
	black.getStyleClass().add("color-element");
	final Text nameBlack = new Text("schwarz");
	nameBlack.getStyleClass().add("color-element-text");
	black.getChildren().add(nameBlack);

	final VBox white = new VBox();
	white.setAlignment(Pos.CENTER);
	white.getStyleClass().add("color-element");
	final Text nameWhite = new Text("weiss");
	nameWhite.getStyleClass().add("color-element-text");
	white.getChildren().add(nameWhite);

	colorContainer.getChildren().add(black);
	colorContainer.getChildren().add(white);

	// Setze die horizontale Box ins Layout
	root.setCenter(colorContainer);

	// Zeige Fenster an
	tempStage.show();

	// Empfange Ereignis, wenn Fenster geschlossen wird
	this.primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	    @Override
	    public void handle(final WindowEvent e) {
		Platform.exit();
		System.exit(0);
	    }
	});

	// Implementiert einen Mausklick-Listener als anonyme Klasse (reicht in unserem Fall aus)
	black.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
	    @Override
	    public void handle(final MouseEvent e) {
		GUI.this.brett.getToolClass().setIsBlack(true);
		GUI.this.showGameWindow();
		tempStage.close();
	    }
	});

	// Implementiert einen Mausklick-Listener als anonyme Klasse (reicht in unserem Fall aus)
	white.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
	    @Override
	    public void handle(final MouseEvent e) {
		GUI.this.brett.getToolClass().setIsBlack(false);
		GUI.this.showGameWindow();
		tempStage.close();
	    }
	});

    }

    // Zeigt die Farbauswahl an
    public void showMessage(final String headline, final String message) {

	// Erzeuge neues Fenster
	final Stage tempStage = new Stage();

	// Setze Breite auf 300 -> Hoehe variabel
	tempStage.setWidth(300);

	// Setze Titel und Favicon des Abfragefensters
	tempStage.setTitle("Mitteilung");
	tempStage.getIcons().add(new Image("GUI/resources/border_image.png"));

	// Zentriere die Stage im Hauptfenster
	final double centerXPosition = this.primaryStage.getX() + this.primaryStage.getWidth() / 2;
	final double centerYPosition = this.primaryStage.getY() + this.primaryStage.getHeight() / 2;
	tempStage.setOnShown(ev -> {
	    tempStage.setX(centerXPosition - tempStage.getWidth() / 2);
	    tempStage.setY(centerYPosition - tempStage.getHeight() / 2);
	});

	// Setze LayoutManager, Contentmanager, Resizable und CSS-File
	final BorderPane root = new BorderPane();
	final Scene firstScene = new Scene(root);
	tempStage.setScene(firstScene);
	tempStage.setResizable(false);
	firstScene.getStylesheets().add("GUI/styles_alert.css");

	// Setze Headline an Top
	final Text headlineText = new Text(headline);
	headlineText.setId("alert-headline");
	root.setTop(headlineText);
	BorderPane.setAlignment(headlineText, Pos.CENTER);
	BorderPane.setMargin(headlineText, new Insets(10, 0, 10, 0));

	// Setze Message in Center
	final Label messageText = new Label(message);
	messageText.setId("alert-message");
	messageText.setMaxWidth(300);
	messageText.setWrapText(true);
	messageText.setTextAlignment(TextAlignment.CENTER);
	root.setCenter(messageText);

	// Setze Button an Boden
	final Button ok = new Button("Verstanden");
	ok.setId("alert-button");
	root.setBottom(ok);
	BorderPane.setMargin(ok, new Insets(10, 0, 0, 0));

	// Zeige Fenster an
	tempStage.show();

	// Implementiert einen Mausklick-Listener als anonyme Klasse (reicht in unserem Fall aus)
	ok.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
	    @Override
	    public void handle(final MouseEvent e) {
		tempStage.close();
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

	menuBar.getMenus().add(new Menu("Du bist Spieler " + GUI.this.brett.getToolClass().isBlackString()));

	return menuBar;
    }

    // Gibt das Textfeld mit dem aktuellen Spieler zurueck
    public Text getPlayerColorText() {
	return this.currentPlayerColor;
    }

    // Gibt das Textfeld mit dem aktuellen Status zurueck
    public Text getStatusText() {
	return this.currentStatus;
    }

}
