import java.util.Scanner;

/**
 * Diese Klasse dient als Wrapperklasse für den Scanner
 * Desweiteren wird hier der Input von allen Sonderzeichen und von Buchstaben bereinigt,
 * die nicht in der deutschen Sprache vorkommen.
 *
 * @author sigritim
 * @version 07.11.2021
 */
public class InputReader {
    private Scanner scanner;

    /**
     * Konstruktor von der Klasse InputReader
     */
    public InputReader() {
        scanner = new Scanner(System.in);
    }

    /**
     * Diese Methode filtert alle Sonderzeichen heraus, die keine Satzzeichen sind und alle Buchstaben,
     * die nicht in der deutschen Sprache verwendet werden.
     *
     * @param input Benutzereingabe
     * @return Gefilterte Benutzereingabe
     */
    String filterInput(String input) {
        // Regex: [^a-z] ersetzt alle Zeichen von A-Z die nicht in diesem Bereich liegen
        return input.replaceAll("[^a-zA-Z0-9äöüÄÖÜ .,?!\":\\-;]", "");
    }

    /**
     * Mit dieser Methode wird ein Befehl vom Benutzer entgegengenommen.
     * Der Befehl wird von Sonderzeichen und von Buchstaben,
     * die nicht in der deutschen Sprache vorkommen, bereinigt.
     *
     * @return Gefilterte Benutzereingabe
     */
    public String getCommand() {
        System.out.print("> ");
        return filterInput(scanner.nextLine());
    }

    /**
     * Mit dieser Methode wird eine Texteingabe vom Benutzer entgegengenommen.
     * Die Texteingabe wird von Sonderzeichen und von Buchstaben,
     * die nicht in der deutschen Sprache vorkommen, bereinigt.
     *
     * @return Gefilterte Benutzereingabe
     */
    public String getText(String consoleOutput) {
        System.out.print(consoleOutput);
        return filterInput(scanner.nextLine());
    }
}
