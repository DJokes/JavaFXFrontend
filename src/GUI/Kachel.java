package GUI;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class Kachel extends VBox {

    // Eigenschaften des Objekts
    private final ImageView imgView;
    private final Text subtitle;
    private final int xKoordinate;
    private final int yKoordinate;
    private final Schachbrett brett;
    private boolean isMarked = false;
    private String isMarkedFrom;
    private String resource = "";


    // Erzeuge eine neue Kachel
    public Kachel(final boolean farbe, final int x, final int y, final Schachbrett brett) {
	super();

	// Assoziation zum Schachbrett
	this.brett = brett;

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


	// Implementiert einen Mausklick-Listener als anonyme Klasse (reicht in unserem Fall aus)
	this.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
	    @Override
	    public void handle(final MouseEvent e) {
		Kachel.this.kachelClicked();
	    }
	});

    }

    // Setzt den Inhalt der Kachel -> Bild
    // Unproblematisch, da diese Methode erst nach Initialisierung des Objekts aufgerufen wird!
    public void setContent(final String resource) {
	if(resource != null) {
	    this.resource = resource;
	    final Image img = new Image(resource);
	    this.imgView.setImage(img);
	    this.subtitle.setText(this.xKoordinate + " " + this.yKoordinate);
	}
    }

    // Setzt die Kachel zurueck
    public void removeContent() {
	this.imgView.setImage(null);
	this.subtitle.setText("");
    }

    // Setzt die Kachel als "markiert" / "unmarkiert"
    public void setMarked(final boolean value) {
	this.isMarked = value;
    }

    // Gibt den Wer zurueck, ob Kachel markiert ist
    public boolean isMarked() {
	return this.isMarked;
    }

    // Gibt die Kachel zurueck, von der markiert wurde
    public String getMarkedFrom() {
	return this.isMarkedFrom;
    }

    // Setzt einen Wert fuer die Kachel, von der markiert wurde
    public void setMarkedFrom(final String from) {
	this.isMarkedFrom = from;
    }

    // Wird beim Mausklick auf diese Kachel ausgefuehrt
    private void kachelClicked() {

	// Wenn Kachel markiert ist
	if(this.isMarked) {
	    this.brett.fuehreZugDurch(new int[] {this.xKoordinate, this.yKoordinate}, this.isMarkedFrom);
	    this.brett.entferneAlleMarker();
	} else {
	    // Zeige moegliche Zuege nur an, wenn richtiger Spieler drueckt
	    if((this.resource.contains(this.brett.getToolClass().getCurrentPlayerColorStringEnglish()))) {
		this.brett.entferneAlleMarker();
		this.brett.holeMoeglicheZuegeFuerEinFeld(new int[] {this.xKoordinate, this.yKoordinate});
	    } else {
		this.brett.entferneAlleMarker();
	    }
	}

    }

}
