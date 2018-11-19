package GUI;

public class Tools {

    private boolean currentPlayerColor;
    private final Schachbrett brett;

    public Tools(final Schachbrett brett) {
	this.brett = brett;
    }

    // Wandelt eine vom Server uebergebene Position (a1, h3...) in eine valide ArrayPosition (01, 00...) um
    public int[] getPositionFromString(final String position) {
	final char[] auswahl = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h' };

	for(int i = 0; i < auswahl.length; i++) {
	    if(auswahl[i] == position.charAt(0)) {
		return new int[] { i, Character.getNumericValue(position.charAt(1)-1) };
	    }
	}

	return null;

    }

    // Wandelt eine valide ArrayPosition (01, 00...) in eine Position um, die der Server versteht (a1, h3...)
    public String getStringFromPosition(final int[] position) {
	final char[] auswahl = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h' };

	return "" + auswahl[position[0]] + "" + (position[1] + 1);

    }

    // Setzt den aktuellen Spieler
    // true = black, false = white
    public void setCurrentPlayerColor(final boolean color) {
	this.currentPlayerColor = color;
    }

    // Gibt den aktuellen Spieler als Boolean zurueck
    // true = black, false = white
    public boolean getCurrentPlayerColorBool() {
	return this.currentPlayerColor;
    }

    // Wechselt den aktuellen Spieler und zeigt Ergebnis an
    public void changeCurrentPlayer() {
	if(this.currentPlayerColor) {
	    this.currentPlayerColor = false;
	} else {
	    this.currentPlayerColor = true;
	}

	this.setCurrentPlayerLayout();
    }

    public void setCurrentPlayerLayout() {
	this.brett.getGUI().getPlayerColorText().setText(this.getCurrentPlayerColorString());
    }

    // Gibt den aktuellen Spieler als String zurueck
    // true = black, false = white
    public String getCurrentPlayerColorString() {

	if(this.currentPlayerColor) {
	    return "schwarz";
	} else {
	    return "weiss";
	}

    }

}
