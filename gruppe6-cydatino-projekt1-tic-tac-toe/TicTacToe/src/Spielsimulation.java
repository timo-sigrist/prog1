/**
 * Diese Klasse repräsentiert eine Spielsimulation für das TicTacToe-Spiel
 *
 * @author brunndar
 * @version 19.10.2021
 */
public class Spielsimulation {

    /**
     * Simuliert ein Spiel, bei welchem X gewinnt
     * Zudem wird im Spiel einmal die Sprache gewechselt sowie ein Fehleingabe gemacht
     *
     * @param spiel Instanz eines Spiels
     * @param spieler1 Instanz des ersten Spielers
     * @param spieler2 Instanz des zweiten Spielers
     */
    public void simuliereSpiel(Spiel spiel, String spieler1, String spieler2) {
        spiel.setzeSprache("de");
        spiel.setzeSpieler1(spieler1);
        spiel.setzeSpieler2(spieler2);
        spiel.starteSpiel();
        spiel.spieleNaechstenZug("B2");
        spiel.spieleNaechstenZug("A2");
        spiel.spieleNaechstenZug("falsche Eingabe");
        spiel.spieleNaechstenZug("A1");
        spiel.spieleNaechstenZug("lang");
        spiel.wechsleSprache("en");
        spiel.spieleNaechstenZug("C3");
        spiel.spieleNaechstenZug("C1");
        spiel.spieleNaechstenZug("C1");
        spiel.spieleNaechstenZug("B1");
        spiel.spieleNaechstenZug("A3");

    }

    /**
     * Simuliert ein Spiel, bei welchem das Ergebnis auf ein Unentschieden hinausläuft
     * Zudem wird am Ende ein weiterer Zug gemacht, um die entsprechende Fehlermeldung zu zeigen
     *
     * @param spiel Instanz eines Spiels
     * @param spieler1 Instanz des ersten Spielers
     * @param spieler2 Instanz des zweiten Spielers
     */
    public void simuliereUnentschieden(Spiel spiel, String spieler1, String spieler2) {
        spiel.setzeSprache("de");
        spiel.setzeSpieler1(spieler1);
        spiel.setzeSpieler2(spieler2);
        spiel.starteSpiel();
        spiel.spieleNaechstenZug("B2");
        spiel.spieleNaechstenZug("A3");
        spiel.spieleNaechstenZug("A2");
        spiel.spieleNaechstenZug("C2");
        spiel.spieleNaechstenZug("C3");
        spiel.spieleNaechstenZug("A1");
        spiel.spieleNaechstenZug("B1");
        spiel.spieleNaechstenZug("B3");
        spiel.spieleNaechstenZug("C1");
        spiel.spieleNaechstenZug("C3");
    }
}
