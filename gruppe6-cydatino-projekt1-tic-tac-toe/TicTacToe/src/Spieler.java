/**
 * Diese Klasse repräsentiert einen Spieler für das TicTacToe-Spiel
 *
 * @author sigritim
 * @version 01.10.2021
 */
public class Spieler {

    private int spielerNummer;
    private String spielerName;

    /**
     * Konstruktor für die Objekte der Klasse Spieler.
     *
     * @param spielerNummer Die Nummer, welche der Spieler auf dem Feld repräsentiert. Zugelassenen Werte: 1 oder 2
     * @param spielerName Der Name des Spielers
     */
    public Spieler(int spielerNummer, String spielerName) {
        this.spielerNummer = spielerNummer;
        this.spielerName = spielerName;
    }

    /**
     * Gib die Spielernummmer des Spielers zurück. Diese Nummer wird für das Spielfeld gebraucht
     *
     * @return Spielernummer für das Spielfeld
     */
    public int gibSpielerNummer() {
        return spielerNummer;
    }

    /**
     * Gibt den Nicknamen des Spielers zurück
     *
     * @return Spielername
     */
    public String gibSpielerName() {
        return spielerName;
    }

}
