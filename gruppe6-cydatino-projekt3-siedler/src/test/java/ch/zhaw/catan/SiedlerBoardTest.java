package ch.zhaw.catan;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class SiedlerBoardTest {
    SiedlerBoard siedlerBoard;
    SiedlerGame siedlerGame;

    @BeforeEach
    public void initialize() {
        siedlerGame = new SiedlerGame(10, 3);
        siedlerBoard = siedlerGame.getBoard();
    }

    @Test
    public void testIsWaterOnly() {
        assertTrue(siedlerBoard.isCornerSurroundedByWater(new Point(3, 1)), "Only Water");
        assertFalse(siedlerBoard.isCornerSurroundedByWater(new Point(11, 9)), "Water and Resource");
        assertFalse(siedlerBoard.isCornerSurroundedByWater(new Point(5, 7)), "Resources only");
    }

    @Test
    public void testHasAdjacentRoad() {
        assertFalse(siedlerBoard.hasAdjacentRoad(new Point(5, 3), siedlerGame.getCurrentPlayer().getFaction()), "No road build yet");
        siedlerGame.placeInitialSettlement(new Point(6,4),false);
        siedlerGame.placeInitialRoad(new Point(5,3), new Point(6, 4));
        assertTrue(siedlerBoard.hasAdjacentRoad(new Point(5, 3), siedlerGame.getCurrentPlayer().getFaction()), "road next to coordinate");
        siedlerGame.switchToNextPlayer();
        assertFalse(siedlerBoard.hasAdjacentRoad(new Point(5, 3), siedlerGame.getCurrentPlayer().getFaction()), "road from other faction");
        siedlerGame.placeInitialSettlement(new Point(4,4),false);
        siedlerGame.placeInitialRoad(new Point(5,3), new Point(4, 4));
        assertTrue(siedlerBoard.hasAdjacentRoad(new Point(5, 3), siedlerGame.getCurrentPlayer().getFaction()), "second player also have a road next to coordinate");
    }

    @Test
    public void testRoadIsAdjacentToSecondSettlement() {
        siedlerGame.placeInitialSettlement(new Point(6, 4), false);
        siedlerGame.placeInitialRoad(new Point(6,4), new Point(6,6));
        siedlerGame.placeInitialSettlement(new Point(9,3), false);
        assertTrue(siedlerBoard.roadIsAdjacentToSecondSettlement(new Point(9,3), new Point(8,4), siedlerGame.getCurrentPlayerFaction()), "road is by second settlement");
        assertFalse(siedlerBoard.roadIsAdjacentToSecondSettlement(new Point(6,4), new Point(7,3), siedlerGame.getCurrentPlayerFaction()), "road is by first settlement");
        assertFalse(siedlerBoard.roadIsAdjacentToSecondSettlement(new Point(8,6), new Point(7,7), siedlerGame.getCurrentPlayerFaction()), "road is by no settlement");
        siedlerGame.switchToNextPlayer();
        assertFalse(siedlerBoard.roadIsAdjacentToSecondSettlement(new Point(9,3), new Point(8,4), siedlerGame.getCurrentPlayerFaction()), "road is by enemy settlement with no road");
    }

}
