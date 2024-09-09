import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TextProcessorTest {

    private TextProcessor textProcessor;
    private List<String> paragraphList;
    private static final String simpleParagraphText = "Mein erster Absatz.";

    @BeforeEach
    public void init() {
        paragraphList = new ArrayList<>();
        textProcessor = new TextProcessor(paragraphList);
    }

    @Test
    public void testAddParagraph() {
        textProcessor.addParagraph(1, simpleParagraphText);
        assertEquals(1, paragraphList.size());
        assertEquals(simpleParagraphText, paragraphList.get(0));
    }

    @Test
    public void testAddParagraphAnywhere() {
        paragraphList.add(0, "test");
        paragraphList.add(1, "to");
        paragraphList.add(2, "paragraph");
        paragraphList.add(3, "anywhere");
        textProcessor.addParagraph(3, "add");
        assertEquals(5, paragraphList.size());
        assertEquals("test", paragraphList.get(0));
        assertEquals("to", paragraphList.get(1));
        assertEquals("add", paragraphList.get(2));
        assertEquals("paragraph", paragraphList.get(3));
        assertEquals("anywhere", paragraphList.get(4));
    }

    @Test
    public void testDeleteLastParagraph() {
        paragraphList.add(0, "test");
        paragraphList.add(1, "to");
        paragraphList.add(2, "delete");
        textProcessor.deleteParagraph(3);
        assertEquals(2, paragraphList.size());
        assertEquals("test", paragraphList.get(0));
        assertEquals("to", paragraphList.get(1));
    }

    @Test
    public void testDeleteAnyParagraph() {
        paragraphList.add(0, "test");
        paragraphList.add(1, "to");
        paragraphList.add(2, "delete");
        paragraphList.add(3, "this");
        paragraphList.add(4, "paragraph");
        textProcessor.deleteParagraph(2);
        assertEquals(4, paragraphList.size());
        assertEquals("test", paragraphList.get(0));
        assertEquals("delete", paragraphList.get(1));
        assertEquals("this", paragraphList.get(2));
        assertEquals("paragraph", paragraphList.get(3));
    }

    @Test
    public void testAddDummyParagraph() {
        textProcessor.addDummyParagraph(1);
        textProcessor.addDummyParagraph(2);
        textProcessor.addDummyParagraph(3);
        assertEquals(3, paragraphList.size());
        assertNotNull(paragraphList.get(1));
    }

    @Test
    public void testReplaceParagraph() {
        paragraphList.add(0, simpleParagraphText);
        textProcessor.replace(1, "e", "E");
        textProcessor.replace(1, "Absatz", "Erfolg");
        assertEquals(1, paragraphList.size());
        assertEquals("MEin ErstEr Erfolg.", paragraphList.get(0));
    }

    @Test
    public void testBuildDictionary() {
        Map<String, Set<Integer>> dictionary;
        paragraphList.add(0, "Test Test Test Test2 Test2");
        dictionary = textProcessor.buildDictionary();
        assertEquals(0, dictionary.size());

        paragraphList.add(1, "Test");
        dictionary = textProcessor.buildDictionary();
        assertEquals(1, dictionary.size());

        paragraphList.add(2, "TEst2 TEST2");
        dictionary = textProcessor.buildDictionary();
        assertEquals(2, dictionary.size());

        paragraphList.add(3, "Test3 Test3 Test3 test3");
        dictionary = textProcessor.buildDictionary();
        assertEquals(2, dictionary.size());

        Set<Integer> testReferenzen = new HashSet<>();
        testReferenzen.add(1);
        testReferenzen.add(2);
        assertArrayEquals(testReferenzen.toArray(), dictionary.get("Test").toArray());

        Set<Integer> test2Referenzen = new HashSet<>();
        test2Referenzen.add(1);
        test2Referenzen.add(3);
        assertArrayEquals(test2Referenzen.toArray(), dictionary.get("Test2").toArray());
    }
}
