package GUI;

import java.util.ArrayList;

import javafx.scene.layout.GridPane;
import schach.daten.D;

public class Schachbrett extends GridPane {
    public Kachel[][] kacheln = new Kachel[8][8];


    public Schachbrett(final boolean playerWhite) {
	super();

	for (int x = 0; x < this.kacheln[0].length; x++) {
	    for (int y = 0; y < this.kacheln[1].length; y++) {
		final boolean farbe = ((x + y) % 2 != 0);

		this.kacheln[x][y] = new Kachel(farbe, x, y);
		if (playerWhite) {
		    this.add(this.kacheln[x][y], x, 7 - y);
		} else {
		    this.add(this.kacheln[x][y], 7 - x, y);
		}

	    }
	}
    }

    public void updateSchachbrett(final ArrayList<D> belegung) {
	belegung.forEach((figur) -> {
	    if(figur.getClass().getSimpleName().toString().equals("D_Figur")) {
		if(figur.getProperties().getProperty("position").toString() != "") {

		    final int[] position = this.toArrayNotation(figur.getProperties().getProperty("position").toString());
		    final String typ = figur.getProperties().getProperty("typ").toString();
		    String isWeiss = figur.getProperties().getProperty("isWeiss").toString();
		    if(isWeiss.equals("false")) {
			isWeiss = "white";
		    } else {
			isWeiss = "black";
		    }
		    // this.kacheln[position[0]][position[1]].setImg("GUI/Pieces/Chess_" + typ + "_" + isWeiss + ".png");
		    this.kacheln[position[0]][position[1]].setImg(typ);

		}
	    }
	});
	System.out.println("Fertig");
    }

    private int[] toArrayNotation(final String position) {

	return  new int[]{Character.getNumericValue(position.charAt(0)-49), Character.getNumericValue(position.charAt(1)-1)};
    }



}