import java.util.*;

/**
 * Mit dieser Klasse wird der Text prozessiert und verarbeitet.
 *
 * @author brunndar & sigritim
 * @version 09.11.2021
 */
public class TextProcessor {

    private List<String> paragraphList;
    private TextOutput textOutput;
    private static final String DUMMY_PARAGRAPH = "Lorem Ipsum Dolor Sit Amet, Consetetur Sadipscing Elitr," +
            "Sed Diam Nonumy Eirmod Tempor Invidunt Ut Labore Et Dolore Magna Aliquyam Erat, Sed Diam Voluptua.";

    /**
     * Konstruktor der Klasse TextProcessor
     *
     * @param paragraphList Übergabe der Referenz der Absatz-Liste
     */
    public TextProcessor(List<String> paragraphList) {
        this.paragraphList = paragraphList;
        this.textOutput = new TextOutput(paragraphList);
    }

    /**
     * Fügt einen Absatz der Absatzliste hinzu.
     *
     * @param number Nummer des Absatzes
     * @param text   Roher Absatztext (ohne Formatierung)
     */
    public void addParagraph(int number, String text) {
        if (checkParagraphIndex(number, true)) {
            paragraphList.add(number - 1, text);
        } else {
            textOutput.printError("Beim hinzufügen eines Absatzes ist ein Fehler aufgetreten.\n" +
                    "Wählen Sie eine Zahl die grösser als 0 ist.");
        }
    }

    /**
     * Löscht einen Absatz der Absatzliste.
     *
     * @param number Nummer des Absatzes
     */
    public void deleteParagraph(int number) {
        if (checkParagraphIndex(number, false)) {
            paragraphList.remove(number - 1);
        } else {
            textOutput.printError("Beim löschen eines Absatzes ist ein Fehler aufgetreten.\n" +
                    "Wählen Sie eine Zahl zwischen 1 und " + (paragraphList.size() + 1));
        }
    }

    /**
     * Fügt einen Dummy-Absatz der Absatzliste hinzu.
     *
     * @param number Nummer des Dummy-Absatzes
     */
    public void addDummyParagraph(int number) {
        if (checkParagraphIndex(number, true)) {
            paragraphList.add(number - 1, DUMMY_PARAGRAPH);
        } else {
            textOutput.printError("Beim hinzufügen eines Dummy-Absatzes ist ein Fehler aufgetreten.\n" +
                    "Wählen Sie eine Zahl die grösser als 0 ist.");
        }
    }

    /**
     * Ersetzt einen Text in einem Absatz der Absatzliste.
     *
     * @param number  Nummer des Absatzes
     * @param oldText zu ersetzender Text
     * @param newText neuer Text
     */
    public void replace(int number, String oldText, String newText) {
        String paragraph = null;
        if (checkParagraphIndex(number, false)) {
            paragraph = paragraphList.get(number - 1);
        } else {
            textOutput.printError("Beim ersetzen eines Textes eines Absatzes ist ein Fehler aufgetreten.\n" +
                    "Wählen Sie eine Zahl zwischen 1 und " + (paragraphList.size() + 1));
        }
        if (paragraph != null && !paragraph.isEmpty()) {
            String newParagraph = paragraph.replace(oldText, newText);
            paragraphList.set(number - 1, newParagraph);
        }
    }

    /**
     * Diese Methode erstellt ein Wortverzeichnis, welches alle Begriffe die öfters als 3 mal vorkommen
     * und deren Zeilennummmern beinhaltet.
     * <p>
     * Wichtig: Bergriffe sind nur Wörter die gross geschrieben werden.
     *
     * @return Wortverzeichnis
     */
    public Map<String, Set<Integer>> buildDictionary() {
        Map<String, Set<Integer>> dictionary = new HashMap<>();
        Map<String, Set<Integer>> paragraphReference = new HashMap<>();
        Map<String, Integer> countWord = new HashMap<>();
        int paragraphIndex = 1;
        for (String paragraph : paragraphList) {
            for (String word : paragraph.split("[ .,?!\":;]")) {
                if (word.matches("[A-Z].*")) {
                    word = word.charAt(0) + word.substring(1).toLowerCase();
                    if (paragraphReference.containsKey(word)) {
                        paragraphReference.get(word).add(paragraphIndex);
                    } else {
                        Set<Integer> newParagraphRef = new HashSet<>();
                        newParagraphRef.add(paragraphIndex);
                        paragraphReference.put(word, newParagraphRef);
                    }
                    countWord.merge(word, 1, Integer::sum);
                    if (countWord.get(word) > 3) {
                        dictionary.put(word, paragraphReference.get(word));
                    }
                }
            }
            paragraphIndex++;
        }
        return dictionary;
    }

    /**
     * Überprüft den mitgegebenen Paragraphen-Index
     *
     * @param number   Indexnummer
     * @param newEntry Definiert, ob mit dem Index neue Einträge angelegt werden
     * @return Gibt true zurück, wenn der Paragraph-Index OK ist
     */
    private boolean checkParagraphIndex(int number, boolean newEntry) {
        boolean indexCorrect;

        if (newEntry) {
            indexCorrect = number > 0 && number <= paragraphList.size() + 1;
        } else {
            indexCorrect = number > 0 && number <= paragraphList.size();
        }
        return indexCorrect;
    }

}
