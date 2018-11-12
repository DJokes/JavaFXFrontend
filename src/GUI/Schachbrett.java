package GUI;

import java.util.ArrayList;

import javafx.scene.layout.GridPane;
import schach.daten.D;
import schach.daten.D_Belegung;
import schach.daten.D_Figur;

public class Schachbrett extends GridPane {
	public Kachel[][] kacheln = new Kachel[8][8];


	public Schachbrett(boolean playerWhite) {
		super();
		
		for (int x = 0; x < kacheln[0].length; x++) {
			for (int y = 0; y < kacheln[1].length; y++) {
				boolean farbe = ((x + y) % 2 != 0);

				kacheln[x][y] = new Kachel(farbe, x, y);
				if (playerWhite) {
					this.add(kacheln[x][y], x, 7 - y);
				} else {
					this.add(kacheln[x][y], 7 - x, y);
				}

			}
		}
	}

	public void updateSchachbrett(ArrayList<D> belegung) {
		belegung.forEach((figur) -> {
			if(figur.getClass().getSimpleName().toString().equals("D_Figur")) {
				if(figur.getProperties().getProperty("position").toString() != "") {
					
					int[] position = toArrayNotation(figur.getProperties().getProperty("position").toString());
					String typ = figur.getProperties().getProperty("typ").toString();
					String isWeiss = figur.getProperties().getProperty("isWeiss").toString();
					if(isWeiss.equals("false")) isWeiss = "white";
					else isWeiss = "black";
	//				this.kacheln[position[0]][position[1]].setImg("GUI/Pieces/Chess_" + typ + "_" + isWeiss + ".png");
					this.kacheln[position[0]][position[1]].setImg(typ);
					
				}
			}
		});
		System.out.println("Fertig");
	}
	
	private int[] toArrayNotation(String position) {

		return  new int[]{Character.getNumericValue(position.charAt(0)-49), Character.getNumericValue(position.charAt(1)-1)};
	}



}