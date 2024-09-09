import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TextOutputTest {

    @Test
    void testLineWrap() {
        String expectedFormattedText = "Lorem ipsum dolor \nsit amet, consetetur \nsadipscing elitr";

        List<String> paragraphs = new ArrayList<>();
        paragraphs.add("Lorem ipsum dolor sit amet, consetetur sadipscing elitr");

        TextOutput textOutput = new TextOutput(paragraphs);
        textOutput.setFormatFix(20);

        assertEquals(expectedFormattedText, textOutput.wrapParagraph(paragraphs.get(0)));
    }

    @Test
    void testLineWrapLongWords() {
        String expectedFormattedText = "Lorem \nipsumipsumipsumipsumipsumipsum \ndolor sit amet, \nconsetetur \nsadipscing elitr";

        List<String> paragraphs = new ArrayList<>();
        paragraphs.add("Lorem ipsumipsumipsumipsumipsumipsum dolor sit amet, consetetur sadipscing elitr");

        TextOutput textOutput = new TextOutput(paragraphs);
        textOutput.setFormatFix(20);

        assertEquals(expectedFormattedText, textOutput.wrapParagraph(paragraphs.get(0)));
    }
}