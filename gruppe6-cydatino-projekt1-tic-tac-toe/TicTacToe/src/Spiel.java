/**
 * Diese Klasse repräsentiert ein Spiel inkl. dessen Spiellogik.
 *
 * @author baumgnoa
 * @version 19.10.2021
 */
public class Spiel {
    private Spielfeld spielfeld;
    private Spieler spieler1;
    private Spieler spieler2;
    private TextAusgabe textAusgabe;

    private int aktuellerSpieler;
    private int anzahlZuege;
    private boolean spielBeendet;

    /**
     * Konstruktor für Objekte der Klasse Spiel
     */
    public Spiel() {
        spielfeld = new Spielfeld();
        textAusgabe = new TextAusgabe();
        aktuellerSpieler = 1;
        anzahlZuege = 0;
    }

    /**
     * Den aktuellen Spieler ermitteln
     */
    private Spieler gibAktuellenSpieler() {
        Spieler spieler;

        if (aktuellerSpieler == 1) {
            spieler = spieler1;
        } else {
            spieler = spieler2;
        }
        return spieler;
    }

    /**
     * Prüft, ob ein Feld bereits befüllt
     *
     * @param spielfeldReferenz externer Feldname
     * @return gibt an, ob das Feld bereits befüllt wurde
     */
    private boolean istFeldBefuellt(String spielfeldReferenz) {
        boolean befuellt = false;
        int feld = spielfeld.gibFeld(spielfeldReferenz);

        if (feld != 0) {
            befuellt = true;
        }
        return befuellt;
    }

    /**
     * Prüft, ob das eingegebene Feld ungültig sei
     *
     * @param spielfeldReferenz externer Feldname
     * @return gibt an, ob das Feld ungültig sei
     */
    private boolean istEingabeUngueltig(String spielfeldReferenz) {
        String buchstabe = "";
        String ziffer = "";
        boolean ungueltig = true;

        if (spielfeldReferenz.length() == 2) {
            buchstabe = spielfeldReferenz.substring(0, 1);
            ziffer = spielfeldReferenz.substring(1, 2);

            if ((buchstabe.equals("A") || buchstabe.equals("B") || buchstabe.equals("C")) &&
                    (ziffer.equals("1") || ziffer.equals("2") || ziffer.equals("3"))) {
                ungueltig = false;
            }
        }

        return ungueltig;
    }

    /**
     * Überprüfung, ob das Spiel gewonnen wurde
     *
     * @return sieg
     */
    private boolean istGewonnen() {
        boolean sieg = false;
        int feldA1 = spielfeld.gibFeld("A1");
        int feldA2 = spielfeld.gibFeld("A2");
        int feldA3 = spielfeld.gibFeld("A3");
        int feldB1 = spielfeld.gibFeld("B1");
        int feldB2 = spielfeld.gibFeld("B2");
        int feldB3 = spielfeld.gibFeld("B3");
        int feldC1 = spielfeld.gibFeld("C1");
        int feldC2 = spielfeld.gibFeld("C2");
        int feldC3 = spielfeld.gibFeld("C3");

        if ((feldA1 != 0 && feldA1 == feldA2 && feldA2 == feldA3) ||
                (feldB1 != 0 && feldB1 == feldB2 && feldB2 == feldB3) ||
                (feldC1 != 0 && feldC1 == feldC2 && feldC2 == feldC3)) {
            sieg = true;
        }
        if ((feldA1 != 0 && feldA1 == feldB1 && feldB1 == feldC1) ||
                (feldA2 != 0 && feldA2 == feldB2 && feldB2 == feldC2) ||
                (feldA3 != 0 && feldA3 == feldB3 && feldB3 == feldC3)) {
            sieg = true;
        }
        if ((feldA1 != 0 && feldA1 == feldB2 && feldB2 == feldC3) ||
                (feldA3 != 0 && feldA3 == feldB2 && feldB2 == feldC1)) {
            sieg = true;
        }
        return sieg;
    }

    /**
     * Wechsle Spieler von 1 zu 2 und von 2 zu 1
     */
    private void wechsleSpieler() {
        if (this.aktuellerSpieler == 1) {
            aktuellerSpieler = 2;
        } else {
            aktuellerSpieler = 1;
        }
    }

    /**
     * Gibt Leerzeile aus.
     */
    private void gibLeerzeileAus() {
        System.out.println();
    }

    /**
     * Gibt Eingabe aus
     */
    private void gibEingabeAus(String eingabe) {
        System.out.println(" " + eingabe);
    }

    /**
     * Gibt Spielfeld aus
     */
    private void gibSpielfeldAus() {
        gibLeerzeileAus();
        spielfeld.zeichneSpielfeld();
        gibLeerzeileAus();
    }

    /**
     * Setzt Sprache für die Text-Ausgabe
     *
     * @param sprache eingegebene Sprache
     */
    public void setzeSprache(String sprache) {
        gibEingabeAus(sprache);
        textAusgabe.setzeSprache(sprache);
    }

    /**
     * Wechselt Sprache und gibt Eingabe-Text wieder aus
     *
     * @param sprache eingegebene Sprache
     */
    public void wechsleSprache(String sprache) {
        setzeSprache(sprache);
        textAusgabe.gibTextMitSpielerNameAus("textEingabe", gibAktuellenSpieler().gibSpielerName());
    }

    /**
     * Spieler 1 wird gesetzt
     *
     * @param spielername Spielername des Spielers 1
     */
    public void setzeSpieler1(String spielername) {
        spieler1 = new Spieler(1, spielername);
    }

    /**
     * Spieler 2 wird gesetzt
     *
     * @param spielername Spielername des Spielers 2
     */
    public void setzeSpieler2(String spielername) {
        spieler2 = new Spieler(2, spielername);
    }

    /**
     * Startet Spiel und gibt Spielfeld ein erstes Mal aus
     */
    public void starteSpiel() {
        gibSpielfeldAus();
        textAusgabe.gibTextMitSpielerNameAus("textEingabe", gibAktuellenSpieler().gibSpielerName());
    }

    /**
     * Spielt den nächsten Spielzug.
     * Dabei wird das Feld mit dem aktuellen Spieler befüllt, auf einen Sieg überprüft und der Spieler gewechselt
     *
     * @param spielfeldReferenz externer Feldname
     */
    public void spieleNaechstenZug(String spielfeldReferenz) {
        Spieler aktuellerSpieler = gibAktuellenSpieler();

        if(spielBeendet) {
            textAusgabe.gibTextAus("textSpielBeendet");
            return;
        }

        gibEingabeAus(spielfeldReferenz);
        if (spielfeldReferenz.equals("lang")) {
            textAusgabe.gibTextAus("textSpracheWaehlen");
            return;
        }

        if (istEingabeUngueltig(spielfeldReferenz)) {
            textAusgabe.gibTextMitSpielerNameAus("fehlerFeldUngueltig", aktuellerSpieler.gibSpielerName());
        } else if (istFeldBefuellt(spielfeldReferenz)) {
            textAusgabe.gibTextMitSpielerNameAus("fehlerFeldBesetzt", aktuellerSpieler.gibSpielerName());
        } else {
            spielfeld.setzeFeld(spielfeldReferenz, aktuellerSpieler.gibSpielerNummer());
            gibSpielfeldAus();

            spielBeendet = istGewonnen();
            if (spielBeendet) {
                textAusgabe.gibTextMitSpielerNameAus("textAusgabeSieger", aktuellerSpieler.gibSpielerName());
            } else {
                wechsleSpieler();
                aktuellerSpieler = gibAktuellenSpieler();
                anzahlZuege++;

                if (anzahlZuege == 9) {
                    textAusgabe.gibTextAus("textUnentschieden");
                    spielBeendet = true;
                }
            }
        }

        if (spielBeendet) {
            gibLeerzeileAus();
        } else {
            textAusgabe.gibTextMitSpielerNameAus("textEingabe", aktuellerSpieler.gibSpielerName());
        }
    }
}
