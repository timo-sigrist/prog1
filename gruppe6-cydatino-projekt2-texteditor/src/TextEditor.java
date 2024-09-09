import java.util.*;
import java.util.Map.Entry;

/**
 * Diese Klasse repräsentiert die Textverarbeitung, welcher die Befehle vom InputReader entgegen nimmt,
 * diese auf korrekte Syntax überprüft und wahlweise der Klasse TextProcessor zur Verarbeitung oder der Klasse
 * TextOutput zur Ausgabe übergibt
 *
 * @author bergecyr
 * @version 09.11.2021
 */
public class TextEditor {

    private final List<String> paragraphList;
    private final InputReader inputReader;
    private final TextProcessor textProcessor;
    private final TextOutput textOutput;

    //Map speichert die erlaubten Befehle und derren Syntax
    private static final Map<String, String> ALLOWED_COMMANDS = new HashMap<>();

    static {
        ALLOWED_COMMANDS.put("ADD", "ADD[\\s0-9]*");
        ALLOWED_COMMANDS.put("DEL", "DEL[\\s0-9]*");
        ALLOWED_COMMANDS.put("DUMMY", "DUMMY[\\s0-9]*");
        ALLOWED_COMMANDS.put("FORMAT", "(FORMAT\\sRAW)|(FORMAT\\sFIX\\s)[0-9]+");
        ALLOWED_COMMANDS.put("INDEX", "INDEX");
        ALLOWED_COMMANDS.put("PRINT", "PRINT");
        ALLOWED_COMMANDS.put("REPLACE", "REPLACE[\\s0-9]*");
        ALLOWED_COMMANDS.put("HELP", "HELP");
        ALLOWED_COMMANDS.put("EXIT", "EXIT");
    }

    /**
     * Konstruktor der Klasse TextEditor
     */
    public TextEditor() {
        paragraphList = new ArrayList<>();
        inputReader = new InputReader();
        textProcessor = new TextProcessor(paragraphList);
        textOutput = new TextOutput(paragraphList);
    }

    /**
     * Methode führt die Interaktion mit dem Benutzer über die Konsole durch
     */
    public void run() {
        boolean exit = false;

        textOutput.printWelcomeText();

        while (!exit) {
            String filteredInput = inputReader.getCommand().toUpperCase();

            if (!"".equals(filteredInput) && checkCommandValid(filteredInput)) {
                exit = callFunction(filteredInput.split(" "));
            }
        }

    }

    /**
     * Methode prüft die Eingabe des Benutzers, ob der Befehl gültig und die Syntax korrekt ist.
     * (Prüft Befehl und Parameter)
     *
     * @param filteredInput Die entsprechende Eingabe des Benutzers
     * @return Gibt true zurück, wenn die Eingabe gültig ist
     */
    boolean checkCommandValid(String filteredInput) {
        String[] words = filteredInput.split(" ");
        String command = words[0].toUpperCase();

        boolean commandValid = false;
        boolean syntaxValid = false;

        Iterator<Entry<String, String>> it = ALLOWED_COMMANDS.entrySet().iterator();
        while (it.hasNext() && !commandValid) {
            Map.Entry<String, String> allowedCommand = it.next();
            if (allowedCommand.getKey().equals(command)) {
                commandValid = true;
                if (filteredInput.matches(allowedCommand.getValue())) {
                    syntaxValid = true;
                }
            }
        }

        if (!commandValid) {
            textOutput.printError("Befehl nicht gefunden");
        } else {
            if (!syntaxValid) {
                textOutput.printError("Falscher Parameter für Befehl " + command);
            }
        }

        return commandValid && syntaxValid;
    }

    /**
     * callFunction ruft je nach Befehl die entsprechende Methode auf
     *
     * @param words Die einzelnen Komponenten (Befehl und Optionen)
     * @return Gibt zurück, ob Programm abgebrochen werden soll (exit)
     */
    boolean callFunction(String[] words) {
        String command = words[0];
        String option1 = (words.length >= 2) ? words[1] : null;
        String option2 = (words.length == 3) ? words[2] : null;

        boolean exit = false;
        switch (command) {
            case "ADD":
                String text = inputReader.getText("Text: ");
                textProcessor.addParagraph(parseInputToIndex(option1, true), text);
                break;
            case "DEL":
                textProcessor.deleteParagraph(parseInputToIndex(option1, false));
                break;
            case "DUMMY":
                textProcessor.addDummyParagraph(parseInputToIndex(option1, true));
                break;
            case "FORMAT":
                if (option1 != null) {
                    if ("RAW".equalsIgnoreCase(option1)) {
                        textOutput.setFormatRaw();
                    }
                    if ("FIX".equalsIgnoreCase(option1) && option2 != null) {
                        textOutput.setFormatFix(Integer.parseInt(option2));
                    }
                }
                break;
            case "INDEX":
                textOutput.printDictionary(textProcessor.buildDictionary());
                break;
            case "PRINT":
                textOutput.print();
                break;
            case "REPLACE":
                String oldText = inputReader.getText("Zu finden: ");
                String newText = inputReader.getText("Zu ersetzen: ");
                textProcessor.replace(parseInputToIndex(option1, false), oldText, newText);
                break;
            case "HELP":
                textOutput.printHelp();
                break;
            case "EXIT":
                exit = true;
                break;
            default:
                textOutput.printError("Befehl nicht gefunden");
        }

        return exit;
    }

    /**
     * Die Methode prüft ob der String im Parameter nicht null ist oder der Wert grösser als die Liste ist
     * und gibt sonst das Ende der paragraphList zurück
     * Wenn newEntry = true ist, so wird zum maximalen Index 1 dazugezählt
     * (für die Erstellungsbefehle)
     *
     * @param inputIndex Index der eingegeben wurde
     * @param newEntry   Definiert, ob mit dem Index neue Einträge angelegt werden
     * @return Index des Parameters oder Ende der Liste
     */
    int parseInputToIndex(String inputIndex, boolean newEntry) {
        int index;
        int paragraphCount = paragraphList.size();

        if (inputIndex == null) {
            index = paragraphCount;
            if (newEntry) index++;
        } else {
            index = Integer.parseInt(inputIndex);
            if (index > paragraphCount && newEntry) {
                index = paragraphCount;
                index++;
            }
        }
        return index;
    }
}
