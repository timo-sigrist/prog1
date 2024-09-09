import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TextEditorTest {

    private TextEditor textEditor;

    @BeforeEach
    public void init() {
        textEditor = new TextEditor();
    }

    @Test
    void positiveTestCommandValid() {
        assertTrue(textEditor.checkCommandValid("ADD"), "Test ADD");
        assertTrue(textEditor.checkCommandValid("ADD 5"), "Test ADD 5");
        assertTrue(textEditor.checkCommandValid("DEL 0"), "Test DEL 5");
        assertTrue(textEditor.checkCommandValid("DUMMY"), "Test DUMMY");
        assertTrue(textEditor.checkCommandValid("DUMMY 6"), "Test DUMMY 6");
        assertTrue(textEditor.checkCommandValid("FORMAT RAW"), "Test FORMAT RAW");
        assertTrue(textEditor.checkCommandValid("FORMAT FIX 6"), "Test FORMAT FIX 6");
        assertTrue(textEditor.checkCommandValid("INDEX"), "Test INDEX");
        assertTrue(textEditor.checkCommandValid("PRINT"), "Test PRINT");
        assertTrue(textEditor.checkCommandValid("REPLACE"), "Test REPLACE");
        assertTrue(textEditor.checkCommandValid("REPLACE 5"), "Test REPLACE 5");
        assertTrue(textEditor.checkCommandValid("HELP"), "Test HELP");
        assertTrue(textEditor.checkCommandValid("EXIT"), "Test EXIT");
    }

    @Test
    void negativeTestCommandValid() {

        //Syntax Fehler
        assertFalse(textEditor.checkCommandValid("ADD 5a"), "Test ADD 5a");
        assertFalse(textEditor.checkCommandValid("ADD a a"), "Test ADD a a");
        assertFalse(textEditor.checkCommandValid("DEL -1"), "Test DEL -1");
        assertFalse(textEditor.checkCommandValid("DUMMY ab"), "Test DUMMY ab");
        assertFalse(textEditor.checkCommandValid("FORMAT RAW 6"), "Test FORMAT RAW 6");
        assertFalse(textEditor.checkCommandValid("FORMAT raw 5"), "Test FORMAT raw 5");
        assertFalse(textEditor.checkCommandValid("FORMAT FIX"), "Test FORMAT FIX");
        assertFalse(textEditor.checkCommandValid("INDEX a"), "Test INDEX a");
        assertFalse(textEditor.checkCommandValid("PRINT 5"), "Test PRINT 5");
        assertFalse(textEditor.checkCommandValid("REPLACE ab"), "Test REPLACE ab");
        assertFalse(textEditor.checkCommandValid("EXIT 12"), "Test EXIT 12");

        //Falsche Befehle
        assertFalse(textEditor.checkCommandValid(""), "Test Empty String");
        assertFalse(textEditor.checkCommandValid("12"), "Test 12");
        assertFalse(textEditor.checkCommandValid("ab-cd"), "Test abcd");
        assertFalse(textEditor.checkCommandValid("Lorem ipsum dolor sit amet, consetetur sadipscing elitr"), "Test Lorem ipsum dolor sit amet...");
    }

    @Test
    void testExitPositive() {
        assertTrue(textEditor.callFunction(new String[]{"EXIT"}), "Test if Program ends");
    }

    @Test
    void testExitNegative() {
        assertFalse(textEditor.callFunction(new String[]{"DUMMY"}), "Test if Program does not halts");
    }

    @Test
    void testParseInputToIndex() {
        assertEquals(1, textEditor.parseInputToIndex(null, true), "Test index for null, for new Entries");
        assertEquals(0, textEditor.parseInputToIndex(null, false), "Test index for null");
        assertEquals(-1, textEditor.parseInputToIndex("-1", false), "Test negative index");
        assertEquals(1, textEditor.parseInputToIndex("5", true), "Test bigger index than list");
    }

}
