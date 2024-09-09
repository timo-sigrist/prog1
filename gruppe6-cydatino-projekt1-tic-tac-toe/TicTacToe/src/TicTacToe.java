/**
 *
 * Diese Klasse repräsentiert die main-Methode des TicTacToe-Spiels.
 *
 * @author brunndar
 * @version 19.10.2021
 */
public class TicTacToe {

    /**
     * Kreiert und startet die Spielsimulation
     *
     * @param args Start-Argumente
     */
    public static void main(String[] args) {
        Spielsimulation spielsimulation = new Spielsimulation();

        spielsimulation.simuliereSpiel(new Spiel(), "Ruedi", "Wolfgang");
        spielsimulation.simuliereUnentschieden(new Spiel(), "Ruedi", "Wolfgang");
    }

}