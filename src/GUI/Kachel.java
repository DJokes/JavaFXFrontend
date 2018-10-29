package GUI;

import javafx.scene.control.Button;

public class Kachel extends Button {


	public Kachel(boolean farbe, int x, int y) {
		super();
	
		this.getStyleClass().add("chess-space");
		if (farbe)
			this.getStyleClass().add("chess-kachel-color");
		else 
			this.getStyleClass().add("chess-kachel-nocolor");
		}
	
	
	

}
