/**
 * Diese Klasse repräsentiert das Spielfeld für das TikTacToe-Spiel.
 * Das Konzept des Feldes ist folgendes:
 *
 *      A1    |   A2    |   A3
 *    ---------------------------
 *      B1    |   B2    |  B3
 *    ---------------------------
 *      C1    |  C2     |  C3
 *
 *
 * Die Felder sind wie folgt aufgeschlüsselt:
 * 0 = leer, 1 = Spieler 1 besetzt das Feld, 2 = Spieler 2 besetzt das Feld
 *
 * @author sigritim
 * @version 19.10.2021
 */
public class Spielfeld {

    private int feldA1;
    private int feldA2;
    private int feldA3;
    private int feldB1;
    private int feldB2;
    private int feldB3;
    private int feldC1;
    private int feldC2;
    private int feldC3;

    /**
     * Konstruktor für die Objekte der Klasse Spielfeld. Setzt alle Felder auf leer(0).
     */
    public Spielfeld() {
        feldA1 = feldA2 = feldA3 = feldB1 = feldB2 = feldB3 = feldC1 = feldC2 = feldC3 = 0;
    }

    /**
     * Gibt den Spielerwert des Feldes der angegebenen Referenz aus
     *
     * @param spielfeldReferenz Spielfeld-Referenz auf das Feld
     * @return Gibt den das Spielerwert des Feldes zurück, wenn leer = 0
     */
    public int gibFeld (String spielfeldReferenz) {
        int gewaehltesFeld;
        switch (spielfeldReferenz) {
            case "A1":
                gewaehltesFeld = feldA1;
                break;
            case "A2":
                gewaehltesFeld = feldA2;
                break;
            case "A3":
                gewaehltesFeld = feldA3;
                break;
            case "B1":
                gewaehltesFeld = feldB1;
                break;
            case "B2":
                gewaehltesFeld = feldB2;
                break;
            case "B3":
                gewaehltesFeld = feldB3;
                break;
            case "C1":
                gewaehltesFeld = feldC1;
                break;
            case "C2":
                gewaehltesFeld = feldC2;
                break;
            case "C3":
                gewaehltesFeld = feldC3;
                break;
            default:
                gewaehltesFeld = 0;
                break;
        };
        return gewaehltesFeld;
    }

    /**
     * Setzt den Spielerwert auf das Feld der angegebenen Referenz
     *
     * @param feldReferenz Die Referenz des Feldes
     * @param spielerWert Der Spielerwert der auf das angegebene Feld gesetzt werden soll
     */
    public void setzeFeld (String feldReferenz, int spielerWert) {
        switch (feldReferenz) {
            case "A1":
                feldA1 = spielerWert;
                break;
            case "A2":
                feldA2 = spielerWert;
                break;
            case "A3":
                feldA3 = spielerWert;
                break;
            case "B1":
                feldB1 = spielerWert;
                break;
            case "B2":
                feldB2 = spielerWert;
                break;
            case "B3":
                feldB3 = spielerWert;
                break;
            case "C1":
                feldC1 = spielerWert;
                break;
            case "C2":
                feldC2 = spielerWert;
                break;
            case "C3":
                feldC3 = spielerWert;
                break;
            default:
                break;
        }
    }


    /**
     * Diese Methode gibt das aktuelle Spielfeld in der Konsole aus
     */
    public void zeichneSpielfeld() {
        String spielfeldSeparatorVertikal = "    |   ";
        String spielfeldSeparatorHorizontal = "---------------------------";

        System.out.println("   1        2        3  ");

        System.out.println("A  " + uebersetzteSpielfeldMarkierung(feldA1)+ spielfeldSeparatorVertikal
                +uebersetzteSpielfeldMarkierung(feldA2)+ spielfeldSeparatorVertikal
                +uebersetzteSpielfeldMarkierung(feldA3)+"   ");

        System.out.println(spielfeldSeparatorHorizontal);

        System.out.println("B  " + uebersetzteSpielfeldMarkierung(feldB1)+ spielfeldSeparatorVertikal
                +uebersetzteSpielfeldMarkierung(feldB2)+ spielfeldSeparatorVertikal
                +uebersetzteSpielfeldMarkierung(feldB3)+"   ");

        System.out.println(spielfeldSeparatorHorizontal);

        System.out.println("C  " + uebersetzteSpielfeldMarkierung(feldC1)+ spielfeldSeparatorVertikal
                +uebersetzteSpielfeldMarkierung(feldC2)+ spielfeldSeparatorVertikal
                +uebersetzteSpielfeldMarkierung(feldC3)+"   ");
    }

    /**
     * Diese Methode übersetzt den Int Wert eines Feldes mit dem jeweiligen Spielersymbol
     * Die Spielersymbole werden wiefolgt aufgeschlüsselt:
     * 0 = leer, 1 = "X", 2 = "O"
     *
     * @param feld Das Feld welches übersetzt werden soll
     * @return Spielersymbol des Feldes
     */
    private String uebersetzteSpielfeldMarkierung(int feld) {
        String spielerMarkierung = " ";

        if (feld == 1) {
            spielerMarkierung = "X";
        } else if (feld == 2) {
            spielerMarkierung = "O";
        }

        return spielerMarkierung;
    }

}
