package GUI;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class Kachel extends VBox {

    // Eigenschaften des Objekts
    private final ImageView imgView;
    private final Text subtitle;
    private final int xKoordinate;
    private final int yKoordinate;


    // Erzeuge eine neue Kachel
    public Kachel(final boolean farbe, final int x, final int y) {
	super();

	// Setze die Koordinaten der Kachel
	this.xKoordinate = x;
	this.yKoordinate = y;

	// Setze CSS-Klasse fuer die Kachel
	this.getStyleClass().add("kachel-object");

	// Je nach Feldposition muss die Kachel grau / weiss sein
	if(farbe) {
	    this.getStyleClass().add("kachel-gray");
	} else {
	    this.getStyleClass().add("kachel-white");
	}

	// Setze Elemente in die Kachel
	this.imgView = new ImageView();
	// Design via Java - ueber CSS nicht moeglich
	this.imgView.setPreserveRatio(true);
	this.imgView.setSmooth(true);
	this.imgView.setFitHeight(40);
	this.imgView.setFitWidth(40);
	this.getChildren().add(this.imgView);
	this.subtitle = new Text();
	this.subtitle.getStyleClass().add("kachel-object-text");
	this.getChildren().add(this.subtitle);

	// Setze Elemente der Kachel auf zentriert
	this.setAlignment(Pos.CENTER);

    }

    // Setzt den Inhalt der Kachel -> Bild
    // Unproblematisch, da diese Methode erst nach Initialisierung des Objekts aufgerufen wird!
    public void setContent(final String resource) {
	if(resource != null) {
	    final Image img = new Image(resource);
	    this.imgView.setImage(img);
	    this.subtitle.setText(this.xKoordinate + " " + this.yKoordinate);
	}
    }

}
