import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Diese Klasse repräsentiert die Textausgabe auf der Konsole
 * inkl. der Formatierungen, welche vorgenommen werden können.
 *
 * @author baumgnoa
 * @version 08.11.2021
 */
public class TextOutput {
    private List<String> paragraphList;
    private int lineLength;
    private boolean outputRaw;

    /**
     * Konstruktor der Klasse TextOutput
     *
     * @param paragraphList Übergabe der Referenz der Absatz-Liste
     */
    public TextOutput(List<String> paragraphList) {
        this.paragraphList = paragraphList;
        outputRaw = true;
        lineLength = 0;
    }

    /**
     * Setzt die Textbreite für die formatierte Ausgabe
     *
     * @param lineLength maximale Zeilenlänge
     */
    public void setFormatFix(int lineLength) {
        this.lineLength = lineLength;
        outputRaw = false;
    }

    /**
     * Schaltet um zwischen roher Ausgabe und formatierter Ausgabe
     */
    public void setFormatRaw() {
        outputRaw = true;
    }

    /**
     * Gibt die Absätze gemäss den aktuellen Einstellungen auf der Konsole aus
     */
    public void print() {
        for (int paragraphIndex = 0; paragraphIndex < paragraphList.size(); paragraphIndex++) {
            String paragraph = paragraphList.get(paragraphIndex);

            if (outputRaw) {
                int paragraphNumber = paragraphIndex + 1;
                System.out.println(paragraphNumber + ": " + paragraph);
            } else {
                System.out.println(wrapParagraph(paragraph));
            }
        }
    }

    /**
     * Gibt den Willkommens-Text mit dem Hinweis auf den HELP-Befehl auf der Konsole aus
     */
    public void printWelcomeText() {
        System.out.println("Text-Editor");
        System.out.println("-----------");
        System.out.println("Herzlich Willkommen im Text-Editor. Informationen zu den Befehlen erhalten Sie\nüber den Befehl HELP.\n");
    }

    /**
     * Gibt einen eingegebenen Text als Fehler aus
     *
     * @param errorText Fehlertext
     */
    public void printError(String errorText) {
        System.err.println(errorText);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gibt die Hilfe über alle Befehle aus
     */
    public void printHelp() {
        System.out.println("ADD [n]\t\t\tFügt einen Absatz an der Stelle [n] ein. Wird kein Absatz angegeben,\n\t\t\t\tso wird der Absatz am Ende ergänzt.");
        System.out.println("DEL [n]\t\t\tLöscht einen Absatz an der Stelle [n]. Wird kein Absatz angegeben,\n\t\t\t\tso wird der letzte Absatz gelöscht.");
        System.out.println("DUMMY [n]\t\tFügt einen hardcodierten Text an Stelle [n] ein. Wird kein Absatz\n\t\t\t\tangegeben, so wird der Absatz am Ende ergänzt.");
        System.out.println("EXIT\t\t\tBeendet den Text-Editor.");
        System.out.println("FORMAT RAW <b>\tSchaltet um zwischen roher Ausgabe und formatierter Ausgabe.\n\t\t\t\tWobei die rohe Ausgabe die jeweilige Absatznummer enthält.");
        System.out.println("HELP\t\t\tZeigt Hilfeinformationen zum Text-Editor an.");
        System.out.println("INDEX\t\t\tGibt einen Index (Wortverzeichnis) aller Begriffe aus, die über alle\n\t\t\t\tAbsätze gesehen öfter als dreimal vorkommen.");
        System.out.println("PRINT\t\t\tGibt den Text gemäss den aktuellen Einstellungen aus.");
        System.out.println("REPLACE [n]\t\tErsetzt eine Zeichenfolge durch eine andere Zeichenfolge im \n\t\t\t\tprinAbsatz [n]. Wird kein Absatz angegeben, so wird die Zeichenfolge im letzten\n\t\t\t\tAbsatz ersetzt.");
    }

    /**
     * Gibt das Wörterbuch aus, mit allen Wörtern, die mehr als 3 Mal vorkommen inkl. der Absätze in denen die Wörter vorkommen
     *
     * @param dictionary Wörterbuch
     */
    public void printDictionary(Map<String, Set<Integer>> dictionary) {
        StringBuilder dictionaryItem;

        for (String word : dictionary.keySet()) {
            Set<Integer> occurrences = dictionary.get(word);
            dictionaryItem = new StringBuilder();
            for (Integer occurrence : occurrences) {
                if (!dictionaryItem.toString().isBlank()) {
                    dictionaryItem.append(",");
                }
                dictionaryItem.append(occurrence);
            }
            System.out.println(word + " " + dictionaryItem);
        }
    }

    /**
     * Fügt einem Absatz für die Ausgabe die Zeilenumbrüche an
     *
     * @param paragraph Roher Absatztext (ohne Formatierung)
     * @return Absatz mit Zeilenumbrüchen
     */
    String wrapParagraph(String paragraph) {
        final String EMPTY_STRING = "";
        final String SPACE = " ";

        StringBuilder formattedParagraphBuilder = new StringBuilder(EMPTY_STRING);

        if (paragraph.length() <= lineLength) {
            formattedParagraphBuilder = new StringBuilder(paragraph);
        } else {
            String[] words = paragraph.split(SPACE);
            StringBuilder singleLine = new StringBuilder(EMPTY_STRING);

            for (String word : words) {
                if (singleLine.length() + word.length() <= lineLength) {
                    singleLine.append(word).append(SPACE);
                } else {
                    formattedParagraphBuilder.append(singleLine);
                    formattedParagraphBuilder.append("\n");
                    singleLine = new StringBuilder(EMPTY_STRING);
                    singleLine.append(word).append(SPACE);
                }
            }

            if (singleLine.length() > 0) {
                formattedParagraphBuilder.append(singleLine.toString().trim());
            }
        }
        return formattedParagraphBuilder.toString();
    }
}
