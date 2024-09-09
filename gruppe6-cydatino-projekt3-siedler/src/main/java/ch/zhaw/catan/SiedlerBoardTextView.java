package ch.zhaw.catan;

import ch.zhaw.catan.Config.Land;
import ch.zhaw.hexboard.HexBoardTextView;
import ch.zhaw.hexboard.Label;

import java.awt.*;
import java.util.Map;

/**
 * This class implements the view of the siedler board, so it shows:
 * <ul>
 * <li>the chip numbers</li>
 * <li>the land types (lands)</li>
 * <li>the thief position</li>
 * </ul>
 *
 * @author baumgnoa, bergecyr, brundar, sigritim
 * @version 10.12.2021
 */
public class SiedlerBoardTextView extends HexBoardTextView<Land, String, String, String> {
    private SiedlerBoard board;
    private Point visibleThiefPoint;
    private Label byThiefCoveredChipNumber;

    /**
     * Standard constructor of the text view
     *
     * @param board
     */
    public SiedlerBoardTextView(SiedlerBoard board) {
        super(board);
        this.board = board;

        showChipNumbers();
        updateThiefPlacement();
    }

    /**
     * Updates the thief position in the text view and saves the point for the next update
     */
    public void updateThiefPlacement() {
        Point thiefField = board.getThiefField();

        if (visibleThiefPoint != null) {
            setLowerFieldLabel(visibleThiefPoint, byThiefCoveredChipNumber);
        }

        setLowerFieldLabel(thiefField, new Label('t', 'h'));
        Map<Point, Integer> chipNumberPlacement = Config.getStandardDiceNumberPlacement();
        byThiefCoveredChipNumber = getChipNumberAsLabel(chipNumberPlacement.get(thiefField));
        visibleThiefPoint = thiefField;
    }

    /**
     * Adds the chip numbers to the text view
     */
    private void showChipNumbers() {
        for (Map.Entry<Point, Integer> entry : Config.getStandardDiceNumberPlacement().entrySet()) {
            setLowerFieldLabel(entry.getKey(), getChipNumberAsLabel(entry.getValue()));
        }
    }

    /**
     * Transforms a chip number to a label
     *
     * @param chipNumber chip number to be converted
     * @return returned Label
     */
    private Label getChipNumberAsLabel(int chipNumber) {
        String chipNumberText = Integer.toString(chipNumber);
        if (chipNumberText.equals("0")) chipNumberText = "  ";
        if (chipNumberText.length() == 1) chipNumberText = "0" + chipNumberText;

        return new Label(chipNumberText.charAt(0), chipNumberText.charAt(1));
    }
}
