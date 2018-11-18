package GUI;

import java.util.ArrayList;

import javafx.scene.layout.GridPane;
import schach.daten.D;

public class Schachbrett extends GridPane {
    public Kachel[][] kachelArray = new Kachel[8][8];

    /*
    public Schachbrett(final boolean playerWhite) {
	super();

	for (int x = 0; x < this.kacheln[0].length; x++) {
	    for (int y = 0; y < this.kacheln[1].length; y++) {

		final boolean farbe = ((x + y) % 2 != 0);
		this.kacheln[x][y] = new Kachel(farbe, x, y);

		if(playerWhite) {
		    this.add(this.kacheln[x][y], x, 7 - y);
		} else {
		    this.add(this.kacheln[x][y], 7 - x, y);
		}

	    }
	}
    }*/

    // Erzeuge neues Schachbrett
    public Schachbrett() {
	super();

	// Durchlaufe jede Kachel
	for (int x = 0; x < this.kachelArray[0].length; x++) {
	    for (int y = 0; y < this.kachelArray[1].length; y++) {

		// Ermittle Farbe der Kachel
		final boolean farbe = ((x + y) % 2 == 0);

		// Erzeuge die Kachel (Koordinaten zur besseren Lesbarkeit getauscht)
		this.kachelArray[x][y] = new Kachel(farbe, x, y);

		// Setze Kachel in das GridPane
		this.add(this.kachelArray[x][y], x, y);

	    }
	}
    }

    // Setze konkreten Inhalt des Schachbretts
    public void updateSchachbrett(final ArrayList<D> belegung) {
	belegung.forEach((figur) -> {
	    if(figur.getClass().getSimpleName().toString().equals("D_Figur")) {
		if(figur.getProperties().getProperty("position").toString() != "") {

		    final int[] position = this.toArrayNotation(figur.getProperties().getProperty("position").toString());
		    final String typ = figur.getProperties().getProperty("typ").toString();
		    String figurColor = figur.getProperties().getProperty("isWeiss").toString();
		    if(figurColor.equals("false")) {
			figurColor = "white";
		    } else {
			figurColor = "black";
		    }

		    // Setze die Figur auf das Brett (bzw. eine Kachel)
		    this.setFigur(position, typ, figurColor);

		}
	    }
	});
	System.out.println("Fertig");
    }


    private int[] toArrayNotation(final String position) {
	return  new int[]{Character.getNumericValue(position.charAt(0)-49), Character.getNumericValue(position.charAt(1)-1)};
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

}