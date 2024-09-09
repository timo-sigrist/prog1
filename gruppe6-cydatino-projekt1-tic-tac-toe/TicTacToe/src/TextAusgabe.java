/**
 * Diese Klasse gibt die Textbausteine in der korrekten Sprache auf der Konsole aus.
 *
 * @author bergecyr
 * @version 18.10.2021
 */
public class TextAusgabe {

    //Die vom Spieler gewählte Sprache: 0 = keine Sprache, 1 = Deutsch, 2 = Englisch
    private int gewaehlteSprache;

    //Texte in Englisch und Deutsch
    private String textWillkommen;
    private String textKeineGueltigeSprache;
    private String textSpracheWaehlen;

    //Textbausteine werden je nach Sprache initialisiert
    private String textSpieler;
    private String textSpielzug;
    private String textAusgabeSieger;
    private String fehlerFeldUngueltig;
    private String fehlerFeldBesetzt;
    private String fehlerTextBaustein;
    private String textGewaehlteSprache;
    private String textUnentschieden;
    private String textSpielBeendet;

    /**
     * Konstruktor gibt Willkommenstext in beiden Sprachen aus und initialisiert die zweisprachigen Texte.
     */
    public TextAusgabe() {
        gewaehlteSprache = 0;

        //initialisiere zweisprachige Texte
        textWillkommen = "Willkommen zu TicTacToe/ Welcome to TicTacToe\n";
        textSpracheWaehlen = "\nBitte wählen Sie Ihre Sprache (\"de\" für Deutsch oder \"en\" für Englisch) \nPlease choose your language (\"de\" for German or \"en\" for English)\nEingabe/Input:";
        textKeineGueltigeSprache = "Sie haben keine gültige Sprache gewählt (\"de\" für Deutsch oder \"en\" für Englisch) " +
                                        "\nNo valid language was choosen (\"de\" for German or \"en\" for English)";

        System.out.print(textWillkommen + textSpracheWaehlen);
    }

    /**
     * Methode nimmt die gewählte Sprache entgegen, setzt diese und initialisiert die Textbausteine in der richtigen Sprache:
     * 0 = keine Sprache, 1 = Deutsch, 2 = Englisch 
     *
     * @param gewaehlteSprache Die vom Benutzer Gewünschte Sprache
     */
    public void setzeSprache(String gewaehlteSprache) {

        if (gewaehlteSprache.equals("de")) {
            this.gewaehlteSprache = 1;

            textSpieler = "Spieler";
            textSpielzug = "Bitte wählen Sie Ihr Feld (Bsp. \"A1\" für Reihe A und Spalte 1). Geben Sie \"lang\" ein um die Sprache zu wechseln \nEingabe:";
            textAusgabeSieger = "Gratulation, Sie haben gewonnen!!! :)";
            fehlerFeldUngueltig = "Das eingegebene Feld ist ungültig";
            fehlerFeldBesetzt = "Das gewählte Feld ist bereits besetzt";
            fehlerTextBaustein = "Text nicht gefunden";
            textGewaehlteSprache = "Sprache Deutsch wurde gewählt";
            textUnentschieden = "Unentschieden!";
            textSpielBeendet = "Das Spiel ist bereits beendet. Es können keine weiteren Züge mehr vorgenommen werden";

            gibTextAus("textGewaehlteSprache");

        } else if (gewaehlteSprache.equals("en")) {
            this.gewaehlteSprache = 2;

            textSpieler = "Player";
            textSpielzug = "Please select your field as reference (e.g. \"A1\" for row A und column 1). Type \"lang\" to change the language \nInput:" ;
            textAusgabeSieger = "Congratulations, you won the game!!! :)";
            fehlerFeldUngueltig = "The selected field is not valid";
            fehlerFeldBesetzt = "The field you have selected is already used";
            fehlerTextBaustein = "Text not found";
            textGewaehlteSprache = "You have choosen English as your language";
            textUnentschieden = "Tie!";
            textSpielBeendet = "The game is over. It is not possible to make any further moves";

            gibTextAus("textGewaehlteSprache");

        } else {
            this.gewaehlteSprache = 0;
            
            System.out.println(textKeineGueltigeSprache);
        }

    }

    /**
     * Methode Gibt die entsprechenden Textbausteine zurück. Sprache wird auf Deutsch (Standardsprache) gesetzt, falls noch keine Sprache gewählt wurde.
     *
     * @param gewaehlterText Gewünschter Textbaustein: textSpracheWaehlen, textGewaehlteSprache, textUntenschieden, textSpielBeendet
     */
    public void gibTextAus(String gewaehlterText) {

        //Setzt Sprache auf de (Standard Sprache) falls keine Sprache gewählt.
        pruefeUndSetzeSprache();

        //Wählt den aktuellen Textbaustein zur Ausgabe
        String textAusgabe;
        switch (gewaehlterText) {
            case "textSpracheWaehlen":
                textAusgabe = textSpracheWaehlen;
                break;
            case "textGewaehlteSprache":
                textAusgabe = textGewaehlteSprache + "\n\n";
                break;
            case "textUnentschieden":
                textAusgabe = textUnentschieden + "\n";
                break;
            case "textSpielBeendet":
                textAusgabe = textSpielBeendet + "\n";
                break;
            default:
                textAusgabe = fehlerTextBaustein + "\n";
                break;
        }

        //Ausgabe Textbaustein
        System.out.print(textAusgabe);

    }

    /**
     * Methode Gibt die auf die Spieler ausgerichteten Textbausteine zurück. 
     * Sprache wird auf Deutsch (Standardsprache) gesetzt, falls noch keine Sprache gewählt wurde.
     *
     * @param gewaehlterText Gewünschter Textbaustein: textEingabe, textAusgabeSieger, fehlerFeldUngueltig, fehlerFeldBesetzt
     * @param spieler Aktueller Spieler für den die Nachricht bestimmt ist
     */
    public void gibTextMitSpielerNameAus(String gewaehlterText, String spieler) {

        //Setzt Sprache auf de (Standard Sprache) falls keine Sprache gewählt.
        pruefeUndSetzeSprache();

        //Setzt den aktuellen Spieler
        String textAusgabe = "[" + textSpieler + " " + spieler + "] ";

        //Wählt den aktuellen Textbaustein zur Ausgabe
        switch (gewaehlterText) {
            case "textEingabe":
                textAusgabe += textSpielzug;
                break;
            case "textAusgabeSieger":
                textAusgabe += textAusgabeSieger + "\n";
                break;
            case "fehlerFeldUngueltig":
                textAusgabe += fehlerFeldUngueltig + "\n";
                break;
            case "fehlerFeldBesetzt":
                textAusgabe += fehlerFeldBesetzt + "\n";
                break;
            default:
                textAusgabe = fehlerTextBaustein + "\n";
                break;
        }

        //Ausgabe Textbaustein
        System.out.print(textAusgabe);
    }

    /**
     * Methode Setzt Sprache auf de (Standardsprache) falls keine Sprache gewählt wurde.
     */
    private void pruefeUndSetzeSprache() {
        if (gewaehlteSprache == 0) {
            setzeSprache("de");
            gewaehlteSprache = 1;
        }
    }

}