import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class InputReaderTest {

    private InputReader inputReader;

    @BeforeEach
    public void init() {
        inputReader = new InputReader();
    }

    @Test
    void testFilterInput() {
        assertEquals("Test", inputReader.filterInput("Test"));
        assertEquals("Test2", inputReader.filterInput("T*e^^%s+t+2$"));
        String sonderzeichenloserSatz = "Das ist ein normaler, Testsatz \"ohne\" Sonderzeichen!";
        assertEquals(sonderzeichenloserSatz, inputReader.filterInput(sonderzeichenloserSatz));
    }
}
