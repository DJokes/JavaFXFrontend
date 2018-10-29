package GUI;

import javafx.scene.layout.GridPane;

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



}