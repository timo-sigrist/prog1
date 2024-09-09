package ch.zhaw.catan;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.awt.*;
import java.util.Map;


/***
 * TODO Write your own tests in this class.
 *
 * Note:  Have a look at {@link ch.zhaw.catan.games.ThreePlayerStandard}. It can be used
 * to get several different game states.
 *
 */
public class SiedlerGameTest {

    private SiedlerGame siedlerGame;
    private int winPoints = 10;
    private int numberOfPlayers = 4;

    @BeforeEach
    public void initialize() {
        siedlerGame = new SiedlerGame(winPoints, numberOfPlayers);
    }

    public void distributeResourcesToPlayer(Player player) {
        player.addResources(Config.Resource.WOOL, 20);
        player.addResources(Config.Resource.GRAIN, 20);
        player.addResources(Config.Resource.LUMBER, 20);
        player.addResources(Config.Resource.ORE, 20);
        player.addResources(Config.Resource.BRICK, 20);
    }

    @Test
    public void testSwitchToNextPlayer() {
        for (int i=0; i<numberOfPlayers; i++) {
            Assertions.assertEquals(i, siedlerGame.getPlayers().indexOf(siedlerGame.getCurrentPlayer()), "check if it's the next player");
            siedlerGame.switchToNextPlayer();
        }
        Assertions.assertEquals(0, siedlerGame.getPlayers().indexOf(siedlerGame.getCurrentPlayer()));
    }

    @Test
    public void testSwitchToPreviousPlayer() {
        for (int i=numberOfPlayers; i>0; i--) {
            siedlerGame.switchToPreviousPlayer();
            Assertions.assertEquals(i-1, siedlerGame.getPlayers().indexOf(siedlerGame.getCurrentPlayer()));
        }
        Assertions.assertEquals(0, siedlerGame.getPlayers().indexOf(siedlerGame.getCurrentPlayer()));
    }

    @Test
    public void testInitialSettlementPlacement() {
        assertTrue(siedlerGame.placeInitialSettlement(new Point(7, 3),false), "build initial Settlement at empty spot");
        assertEquals(siedlerGame.getCurrentPlayer().getFaction().toString(), siedlerGame.getBoard().getCorner(new Point(7,3)), "settlement was built");
        assertFalse(siedlerGame.placeInitialSettlement(new Point(7, 3),false), "build initial Settlement at occupied spot");
        assertFalse(siedlerGame.placeInitialSettlement(new Point(7, 1),false), "build initial Settlement in water");
        assertNull(siedlerGame.getBoard().getCorner(new Point(7, 1)), "no settlement was built");
    }

    @Test
    public void testInitialRoadPlacement() {
        assertFalse(siedlerGame.placeInitialRoad(new Point(7, 7), new Point(7,9)), "Build Road not connected to Settlement");
        assertNull(siedlerGame.getBoard().getEdge(new Point(7, 7), new Point(7,9)), "no road was built");
        siedlerGame.placeInitialSettlement(new Point(7,7), false);
        assertTrue(siedlerGame.placeInitialRoad(new Point(7, 7), new Point(7,9)), "Build Road connected to Settlement");
        assertEquals(siedlerGame.getCurrentPlayer().getFaction().toString(), siedlerGame.getBoard().getEdge(new Point(7, 7), new Point(7,9)), "Road was build");
        siedlerGame.placeInitialSettlement(new Point(7,3), false);
        assertFalse(siedlerGame.placeInitialRoad(new Point(7, 3), new Point(7,1)), "Build Road on water");
    }

    @Test
    public void testInitialResourceDistribution() {
        Player currentPlayer = siedlerGame.getCurrentPlayer();
        siedlerGame.placeInitialSettlement(new Point(8, 4), true);
        assertEquals(2, currentPlayer.getResources().get(Config.Resource.WOOL), "two WOOLs was distributed");
        siedlerGame.placeInitialSettlement(new Point(5, 7), true);
        assertEquals(1,currentPlayer.getResources().get(Config.Resource.LUMBER), "one LUMBER was distributed");
        assertEquals(1, currentPlayer.getResources().get(Config.Resource.GRAIN), "one GRAIN was distributed");
        assertEquals(1, currentPlayer.getResources().get(Config.Resource.ORE), "one ORE was distributed");

    }

    @Test
    public void testBuildSettlement() {
        SiedlerBoard siedlerBoard = siedlerGame.getBoard();
        Player currentPlayer = siedlerGame.getCurrentPlayer();

        distributeResourcesToPlayer(currentPlayer);

        siedlerGame.placeInitialSettlement(new Point(7, 3),false);
        siedlerGame.placeInitialRoad(new Point(7,3), new Point(6,4));
        assertFalse(siedlerGame.buildSettlement(new Point(6,4)), "build settlement after one connected road");
        assertNull(siedlerBoard.getCorner(new Point(6 ,4)), "no settlement was build");
        siedlerGame.buildRoad(new Point(6,4), new Point(6,6));
        assertTrue(siedlerGame.buildSettlement(new Point(6,6)), "build settlement after two connected roads");
        assertEquals(currentPlayer.getFaction().toString(), siedlerBoard.getCorner(new Point(6,6)), "settlement was built");
        assertFalse(siedlerGame.buildSettlement(new Point(8,4)), "build with no connected roads");
        assertNull(siedlerBoard.getCorner(new Point(8 ,4)), "no settlement was build");
        assertFalse(siedlerGame.buildSettlement(new Point(6,7)), "build settlement at invalid coordinate");
        assertFalse(siedlerGame.buildSettlement(new Point(6,6)), "occupied spot");

        siedlerGame.placeInitialSettlement(new Point(10,16), false);
        siedlerGame.placeInitialSettlement(new Point(3,15), false);
        siedlerGame.buildRoad(new Point(3,15), new Point(4,16));
        siedlerGame.buildRoad(new Point(4,16), new Point(4,18));
        assertTrue(siedlerGame.buildSettlement(new Point(4,18)), "Settlment-limit of 5 was not reached with 5. settlement");
        siedlerGame.buildRoad(new Point(4,18), new Point(5,19));
        siedlerGame.buildRoad(new Point(5,19), new Point(6,18));
        assertFalse(siedlerGame.buildSettlement(new Point(6,18)), "Settlment-limit of 5 was reached with 6. settlement");
    }

    @Test
    public void testBuildRoad() {
        siedlerGame.placeInitialSettlement(new Point(6,10), false);

        distributeResourcesToPlayer(siedlerGame.getCurrentPlayer());

        assertTrue(siedlerGame.buildRoad(new Point(6,10), new Point(5, 9)), "build street next to own settlement");
        assertEquals(siedlerGame.getCurrentPlayer().getFaction().toString(), siedlerGame.getBoard().getEdge(new Point(6,10), new Point(5, 9)), "street was built");
        assertTrue(siedlerGame.buildRoad(new Point(5, 9), new Point(5, 7)), "build second street connect to the first build street");
        assertEquals(siedlerGame.getCurrentPlayer().getFaction().toString(), siedlerGame.getBoard().getEdge(new Point(5, 9), new Point(5, 7)), "second street was built");
        assertFalse(siedlerGame.buildRoad(new Point(9, 7), new Point(9,9)), "build street without any adjacent settlement or road");
        assertNull(siedlerGame.getBoard().getEdge(new Point(9, 7), new Point(9,9)), "no road was built");
        assertFalse(siedlerGame.buildRoad(new Point(1, 13), new Point(3,13)), "build illegal street over field right");

        siedlerGame.switchToNextPlayer();

        assertFalse(siedlerGame.buildRoad(new Point(6, 6), new Point(5, 7)), "connect a street to a enemy street without settlement");
        siedlerGame.placeInitialSettlement(new Point(6,6), false);
        assertFalse(siedlerGame.buildRoad(new Point(6, 6), new Point(5, 7)), "build a street with no resources");

        distributeResourcesToPlayer(siedlerGame.getCurrentPlayer());

        assertTrue(siedlerGame.buildRoad(new Point(6, 6), new Point(5, 7)), "connect a street to a enemy street with settlement");

        siedlerGame.placeInitialSettlement(new Point(7, 3), false);
        assertFalse(siedlerGame.buildRoad(new Point(7, 3), new Point(7,1)), "build street into the water");
        assertNull(siedlerGame.getBoard().getEdge(new Point(7, 3), new Point(7,1)), "no road was built");

        assertFalse(siedlerGame.buildRoad(new Point(5, 7), new Point(5, 9)), "occupied spot");
        // Add 13 roads to structurelist
        for (int i = 0; i < 13; i++) {
            siedlerGame.getCurrentPlayer().addRoad();
        }
        assertTrue(siedlerGame.buildRoad(new Point(7, 3), new Point(8,4)), "Road limit with 15. road was not reached");
        assertEquals(siedlerGame.getCurrentPlayer().getFaction().toString(), siedlerGame.getBoard().getEdge(new Point(7, 3), new Point(8,4)), "street was built");
        assertFalse(siedlerGame.buildRoad(new Point(8, 4), new Point(9,3)), "Road limit with 16. road was reached");
        assertNull(siedlerGame.getBoard().getEdge(new Point(8, 4), new Point(9,3)), "no road was built");
    }

    @Test
    public void testBuildCity() {
        siedlerGame.placeInitialSettlement(new Point(6,10), false);

        distributeResourcesToPlayer(siedlerGame.getCurrentPlayer());

        assertFalse(siedlerGame.buildCity(new Point(6, 6)), "placing city without settlement");
        assertTrue(siedlerGame.buildCity(new Point(6, 10)), "placing city on a settlement");
        assertEquals(siedlerGame.getCurrentPlayer().getFaction().toString().toUpperCase(), siedlerGame.getBoard().getCorner(new Point(6,10)), "city was built");
        assertFalse(siedlerGame.buildCity(new Point(6, 10)), "placing city on a city");

        siedlerGame.placeInitialSettlement(new Point(4,6), false);
        siedlerGame.buildCity(new Point(4, 6));
        siedlerGame.placeInitialSettlement(new Point(10,4), false);
        siedlerGame.buildCity(new Point(10, 4));
        siedlerGame.placeInitialSettlement(new Point(11,9), false);
        assertTrue(siedlerGame.buildCity(new Point(11, 9)), "City-limit not reached with 4. city");
        siedlerGame.placeInitialSettlement(new Point(5,13), false);
        assertFalse(siedlerGame.buildCity(new Point(5, 13)), "City-limit is reached with 5. city");
    }

    @Test
    public void testIsSettlementStructurelimitReached() {
        Player currentPlayer = siedlerGame.getCurrentPlayer();
        Settlement settlement = new Settlement(Config.Structure.SETTLEMENT, currentPlayer.getFaction().toString());

        assertFalse(siedlerGame.isBuildinglimitReached(settlement), "Settlement-limit with 0 not reached, no nullpointer");
        for(int i = 0; i < Config.Structure.SETTLEMENT.getStockPerPlayer() -1; i++) {
            currentPlayer.addStructure(settlement);

        }
        assertFalse(siedlerGame.isBuildinglimitReached(settlement), "Settlement-limit with 4 not reached");
        currentPlayer.addStructure(settlement);
        assertTrue(siedlerGame.isBuildinglimitReached(settlement), "Settlement-limit with 5 is reached, limit >= 5");
        currentPlayer.addStructure(settlement);
        assertTrue(siedlerGame.isBuildinglimitReached(settlement), "Settlement-limit reached with 6");
    }

    @Test
    public void testIsCityStructurelimitReached() {

        Player currentPlayer = siedlerGame.getCurrentPlayer();
        City city = new City(Config.Structure.CITY, currentPlayer.getFaction().toString());

        assertFalse(siedlerGame.isBuildinglimitReached(city), "City-limit with 0 not reached, no nullpointer");
        for(int i = 0; i < Config.Structure.CITY.getStockPerPlayer() - 1; i++) {
            currentPlayer.addStructure(city);
        }
        assertFalse(siedlerGame.isBuildinglimitReached(city), "City-limit with 3 not reached");
        currentPlayer.addStructure(city);
        assertTrue(siedlerGame.isBuildinglimitReached(city), "City-limit with 4 is reached, limit >= 4");
        currentPlayer.addStructure(city);
        assertTrue(siedlerGame.isBuildinglimitReached(city), "City-limit reached with 5");
    }

    @Test
    public void isRoadLimitReached() {
        Player currentPlayer = siedlerGame.getCurrentPlayer();

        assertFalse(siedlerGame.isRoadlimitReached(), "Road-limit with 0 not reached, no nullpointer");
        for(int i = 0; i < Config.Structure.ROAD.getStockPerPlayer() - 1; i++) {
            currentPlayer.addRoad();
        }
        assertFalse(siedlerGame.isRoadlimitReached(), "Road-limit with 14 not reached");
        currentPlayer.addRoad();
        assertTrue(siedlerGame.isRoadlimitReached(), "Road-limit with 15 is reached, limit >= 15");
        currentPlayer.addRoad();
        assertTrue(siedlerGame.isRoadlimitReached(), "Road-limit reached with 16");
    }

    @Test
    public void testPlaceThiefOnUnallowedPlaces() {
        siedlerGame.placeInitialSettlement(new Point(6,10), false);

        assertFalse(siedlerGame.placeThiefAndStealCard(new Point(6, 4)), "placing thief not on a field");
        assertFalse(siedlerGame.placeThiefAndStealCard(new Point(3, 5)), "placing thief in a water field");
        assertTrue(siedlerGame.placeThiefAndStealCard(new Point(5, 11)), "placing thief on a possible field");
    }

    @Test
    public void testThiefNotGivingResources() {
        siedlerGame.placeInitialSettlement(new Point(6,10), false);

        siedlerGame.placeThiefAndStealCard(new Point(5, 11));
        siedlerGame.throwDice(9);
        Map<Config.Resource, Integer> resources = siedlerGame.getCurrentPlayer().getResources();
        assertEquals(resources.get(Config.Resource.BRICK), 0);
    }

    @Test
    @Disabled("manual use only")
    public void printThiefPlacingTest() {
        testThiefChecksAndStealsCardsOfPlayers(true);
    }

    @ParameterizedTest
    @ValueSource(booleans = {false})
    public void testThiefChecksAndStealsCardsOfPlayers(boolean print) {
        siedlerGame.placeInitialSettlement(new Point(5,7), false);
        int redPlayerCardCount = 5;
        int bluePlayerCardCount = 9;
        int greenPlayerCardCount = 0;
        siedlerGame.getCurrentPlayer().addResources(Config.Resource.GRAIN, 1);
        siedlerGame.getCurrentPlayer().addResources(Config.Resource.BRICK, 1);
        siedlerGame.getCurrentPlayer().addResources(Config.Resource.LUMBER, 1);
        siedlerGame.getCurrentPlayer().addResources(Config.Resource.ORE, 1);
        siedlerGame.getCurrentPlayer().addResources(Config.Resource.WOOL, 1);
        siedlerGame.switchToNextPlayer();
        siedlerGame.placeInitialSettlement(new Point(7,7), false);
        siedlerGame.getCurrentPlayer().addResources(Config.Resource.WOOL, bluePlayerCardCount);
        siedlerGame.switchToNextPlayer();

        if (print) {
            for (Player player : siedlerGame.getPlayers()) {
                System.out.println(player.getFaction());
                System.out.println(player.getResources());
            }
        }

        siedlerGame.placeThiefAndStealCard(new Point(4, 8));
        if (print) {
            System.out.println(new SiedlerBoardTextView(siedlerGame.getBoard()));
        }

        for (Player player : siedlerGame.getPlayers()) {
            switch (player.getFaction()) {
                case RED:
                    assertEquals(redPlayerCardCount-1, player.getCardsCount());
                    break;
                case BLUE:
                    assertEquals(Math.round((float) bluePlayerCardCount/2), player.getCardsCount());
                    break;
                case GREEN:
                    assertEquals(greenPlayerCardCount+1, player.getCardsCount());
                    break;
                case YELLOW:
                    break;
            }
            if (print) {
                System.out.println(player.getFaction());
                System.out.println(player.getResources());
            }
        }
    }
}