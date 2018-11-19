package GUI;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.scene.layout.GridPane;
import schach.backend.BackendSpielAdminStub;
import schach.backend.BackendSpielStub;
import schach.daten.D;
import schach.daten.Xml;

public class Schachbrett extends GridPane {
    private final GUI gui;
    private final Kachel[][] kachelArray = new Kachel[8][8];
    private final Tools tools;
    private static int id_spiel = 1;
    private static String pfad_spiel = "spielgruppe1";
    private static String rest_basis = "http://www.game-engineering.de:8080/rest/";
    private static boolean logging = true;
    BackendSpielAdminStub admin = new BackendSpielAdminStub(rest_basis, logging);
    BackendSpielStub spiel = new BackendSpielStub(rest_basis, logging);

    // Erzeuge neues Schachbrett
    public Schachbrett(final GUI gui) {
	super();

	// Assoziation zur Tool-Klasse
	this.tools = new Tools(this);
	this.gui = gui;

	// Durchlaufe jede Kachel
	for (int x = 0; x < this.kachelArray[0].length; x++) {
	    for (int y = 0; y < this.kachelArray[1].length; y++) {

		// Ermittle Farbe der Kachel
		final boolean farbe = ((x + y) % 2 == 0);

		// Erzeuge die Kachel (Koordinaten zur besseren Lesbarkeit getauscht)
		this.kachelArray[x][y] = new Kachel(farbe, x, y, this);

		// Setze Kachel in das GridPane
		this.add(this.kachelArray[x][y], x, y);

	    }
	}
    }

    // Setze konkreten Inhalt des Schachbretts
    private void updateSchachbrett(final ArrayList<D> belegung) {

	// Entferne alte Belegung
	this.removeSchachbrettBelegung();

	belegung.forEach((figur) -> {
	    if(figur.getClass().getSimpleName().toString().equals("D_Figur")) {
		if(figur.getProperties().getProperty("position").toString() != "") {

		    final int[] position = this.tools.getPositionFromString(figur.getProperties().getProperty("position").toString());
		    final String typ = figur.getProperties().getProperty("typ").toString();
		    String figurColor = figur.getProperties().getProperty("isWeiss").toString();
		    if(figurColor.equals("false")) {
			figurColor = "black";
		    } else {
			figurColor = "white";
		    }

		    // Setze die Figur auf das Brett (bzw. eine Kachel)
		    this.setFigur(position, typ, figurColor);

		}
	    }
	});
	System.out.println("Fertig");

    }

    // Gibt die Assoziation zur GUI zurueck
    public GUI getGUI() {
	return this.gui;
    }

    // Entfern die komplette Schachbrettbelegung
    private void removeSchachbrettBelegung() {
	for (int x = 0; x < this.kachelArray[0].length; x++) {
	    for (int y = 0; y < this.kachelArray[1].length; y++) {
		this.entferneAlleMarker();
		this.kachelArray[x][y].removeContent();
	    }
	}
    }

    // Setzt die Figur auf eine bestimmte Kachel
    private void setFigur(final int[] position, final String typ, final String figureColor) {

	// Hole Koordinaten
	final int xKoordinate = position[0];
	final int yKoordinate = position[1];

	// 1. Buchstabe klein
	final String newTyp = typ.toLowerCase();

	// Ermittle dynamischen Pfad
	final String resourceIdentifier = newTyp + "_" + figureColor + ".png";

	// Spreche die benoetigte Kachel an
	this.kachelArray[xKoordinate][yKoordinate].setContent("GUI/resources/" + resourceIdentifier);

    }

    // Lade Spiel
    public void loadGame() {

	// Obwohl das Spiel korrekt gespeichert wird, kann das Spiel serverseitig nicht gefunden werden
	// -> Fehler bei Dopatka?
	this.admin.ladenSpiel(id_spiel, pfad_spiel);

    }

    // Speichere Spiel
    public void saveGame() {
	this.admin.speichernSpiel(id_spiel, pfad_spiel);
    }

    // Erzeuge neues Spiel
    public void newGame() {
	this.admin.neuesSpiel(id_spiel);
	this.holeAktuelleBelegung();
	this.getToolClass().setCurrentPlayerColor(false);
	this.getToolClass().setCurrentPlayerLayout();
    }

    // Verlasse Spiel
    public void onQuit() {
	Platform.exit();
	System.exit(0);
    }

    // Holt aktuelle Belegung und ruft damit updateSchachbrett auf
    public void holeAktuelleBelegung() {
	final ArrayList<D> belegung = Xml.toArray(this.spiel.getAktuelleBelegung(id_spiel));
	this.updateSchachbrett(belegung);
    }

    // Holt alle moeglichen Zuege fuer eine aktuelle Position
    public void holeMoeglicheZuegeFuerEinFeld(final int[] position) {
	final String posSendbar = this.tools.getStringFromPosition(position);
	final ArrayList<D> zuege = Xml.toArray(this.spiel.getErlaubteZuege(id_spiel, posSendbar));
	this.markiereKacheln(zuege, posSendbar);
    }

    // Markiert alle Kacheln, auf die gewechselt werden koennen
    private void markiereKacheln(final ArrayList<D> zuege, final String posGesendet) {

	// Jedes Listenelement durchlaufen
	zuege.forEach((zug) -> {

	    // Sicherstellen, dass Element der D_Zug-Klasse zugehoerig ist
	    if(zug.getClass().getSimpleName().toString().equals("D_Zug")) {

		// Sicherstellen, dass benoetigte Parameter gesetzt sind
		if(zug.getProperties().getProperty("nach").toString() != "") {

		    final int[] position = this.tools.getPositionFromString(zug.getProperties().getProperty("nach").toString());

		    // Markiere die auswaehlbaren Kacheln
		    this.setMarker(position, posGesendet);

		}

	    }

	});

    }

    // Markiert bestimmte Kacheln
    private void setMarker(final int[] position, final String markedFrom) {

	// Hole Koordinaten
	final int xKoordinate = position[0];
	final int yKoordinate = position[1];

	// Setze Hintergrund
	this.kachelArray[xKoordinate][yKoordinate].getStyleClass().add("kachel-object-marked");
	this.kachelArray[xKoordinate][yKoordinate].setMarked(true);
	this.kachelArray[xKoordinate][yKoordinate].setMarkedFrom(markedFrom);

    }

    // Entfernt von allen Kacheln die
    public void entferneAlleMarker() {
	// Durchlaufe jede Kachel
	for (int x = 0; x < this.kachelArray[0].length; x++) {
	    for (int y = 0; y < this.kachelArray[1].length; y++) {
		// Wenn Kachel markiert ist
		if(this.kachelArray[x][y].isMarked()) {
		    // Entferne Status
		    this.kachelArray[x][y].getStyleClass().remove("kachel-object-marked");
		    this.kachelArray[x][y].setMarked(false);
		    this.kachelArray[x][y].setMarkedFrom("null");
		}
	    }
	}
    }

    // Fuehrt geprueften Zug durch und aktualisiert das Schachbrett
    public void fuehreZugDurch(final int[] position, final String markedFrom) {
	final String posSendbar = this.tools.getStringFromPosition(position);

	final ArrayList<D> rueckmeldung = Xml.toArray(this.spiel.ziehe(id_spiel, markedFrom, posSendbar));

	// Rueckgesendete Elemente durchlaufen
	rueckmeldung.forEach((zug) -> {

	    // Wenn Zug ungueltig -> Spieler nicht wechseln!
	    if(!(zug.getClass().getSimpleName().toString().equals("D_Fehler"))) {

		this.tools.changeCurrentPlayer();

	    }

	});

	this.holeAktuelleBelegung();
    }

    // Ermittelt wie folgt die Spielerfarbe:
    // Hole alle erlaubten Belegungen
    // -> Zufaellige Figur
    // -> Farbe der Figur ist Spielerfarbe
    public boolean ermittleSpielerFarbe() {

	// Hole alle erlaubten Zuege
	final ArrayList<D> zuege = Xml.toArray(this.spiel.getAlleErlaubtenZuege(id_spiel));

	// 1. Element auswaehlen
	if(zuege.get(0).getClass().getSimpleName().toString().equals("D_Zug")) {
	    final String von = zuege.get(0).getProperties().getProperty("von").toString();
	    if(von != "") {

		// Ermittle Farbe der Figur auf dieser Position
		final ArrayList<D> figur = Xml.toArray(this.spiel.getFigur(id_spiel, von));

		// 1. Element auswaehlen
		if(figur.get(0).getClass().getSimpleName().toString().equals("D_Figur")) {
		    final String isWeiss = figur.get(0).getProperties().getProperty("isWeiss").toString();

		    if(isWeiss.equals("true")) {
			return false;
		    } else if(isWeiss.equals("false")) {
			return true;
		    }
		}

	    }
	}

	return false;
    }

    // Gibt die Verbindung zur Tool-Klasse zurueck
    public Tools getToolClass() {
	return this.tools;
    }

}