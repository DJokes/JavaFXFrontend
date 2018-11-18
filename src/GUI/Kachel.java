package GUI;

import javafx.scene.control.Button;

public class Kachel extends Button {

    //private ImageView imgview;


    public Kachel(final boolean farbe, final int x, final int y) {
	super();

	this.getStyleClass().add("chess-space");
	if (farbe) {
	    this.getStyleClass().add("chess-kachel-color");
	} else {
	    this.getStyleClass().add("chess-kachel-nocolor");
	}
    }

    public void setImg(final String location) {
	this.setText(location);
	//		try {
	//		this.imgview = new ImageView(new Image(location));
	//		this.imgview.setVisible(true);
	//		this.imgview.setFitWidth(7);
	//		this.imgview.setFitHeight(7);
	//		} catch(Exception e) {
	//			System.out.println(e.getMessage());
	//		}
    }




}
