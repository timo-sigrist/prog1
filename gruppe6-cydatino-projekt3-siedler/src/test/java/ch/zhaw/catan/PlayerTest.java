package ch.zhaw.catan;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/***
 * This class tests the functionalities of Player.
 *
 */
public class PlayerTest {

    private Player player;

    @BeforeEach
    public void initializeTests() {
        player = new Player();

    }

    @Test
    public void testPlayerInitializing() {
        int numberOfPlayers = 3;

        List<Player> playerList = Player.initializePlayers(numberOfPlayers);
        assertEquals(numberOfPlayers, playerList.size());
        for (int i=0; i < numberOfPlayers; i++) {
            assertNotNull(playerList.get(i).getFaction(), "check player got faction");
            if (i > 0) {
                assertNotEquals(playerList.get(i-1).getFaction(), playerList.get(i).getFaction(), "check if players haven't got the same faction");
            }
        }
    }

    @Test
    public void testAddResources() {
        int amountToAdd = 40;
        player.addResources(Config.Resource.LUMBER, amountToAdd);
        player.addResources(Config.Resource.GRAIN, amountToAdd);
        assertEquals(player.getResources().get(Config.Resource.LUMBER), amountToAdd, "are added LUMBERs here");
        assertEquals(player.getResources().get(Config.Resource.GRAIN), amountToAdd, "are added GRAINs here");
    }

    @Test
    public void testRemoveResources() {
        Map<Config.Resource, Integer> resources = new HashMap<>();
        resources.put(Config.Resource.LUMBER, 100);
        player.setResources(resources);
        player.removeResources(Config.Resource.LUMBER, 1);
        assertEquals(99, player.getResources().get(Config.Resource.LUMBER), "check if there really 99 lumber");
    }

    @Test
    public void testIsPaymentPossiblePositive() {
        Map<Config.Resource, Integer> resources = new HashMap<>();

        resources.put(Config.Resource.LUMBER, 1);
        resources.put(Config.Resource.BRICK, 1);
        player.setResources(resources);
        assertTrue(player.isPaymentPossible(Config.Structure.ROAD), "Road can be paid");

        resources = new HashMap<>();
        resources.put(Config.Resource.LUMBER, 1);
        resources.put(Config.Resource.BRICK, 1);
        resources.put(Config.Resource.WOOL, 1);
        resources.put(Config.Resource.GRAIN, 1);
        player.setResources(resources);
        assertTrue(player.isPaymentPossible(Config.Structure.SETTLEMENT), "Settlement can be paid");

        resources = new HashMap<>();
        resources.put(Config.Resource.ORE, 3);
        resources.put(Config.Resource.GRAIN, 2);
        player.setResources(resources);
        assertTrue(player.isPaymentPossible(Config.Structure.CITY), "City can be paid");
    }

    @Test
    public void testIsPaymentPossibleNegative() {
        Map<Config.Resource, Integer> resources = new HashMap<>();

        player.setResources(resources);
        assertFalse(player.isPaymentPossible(Config.Structure.ROAD), "Road can not be paid");

        resources = new HashMap<>();
        resources.put(Config.Resource.ORE, 1);
        resources.put(Config.Resource.GRAIN, 2);
        player.setResources(resources);
        assertFalse(player.isPaymentPossible(Config.Structure.SETTLEMENT), "Settlement can not be paid");

        resources = new HashMap<>();
        resources.put(Config.Resource.ORE, 1);
        resources.put(Config.Resource.GRAIN, 2);
        player.setResources(resources);
        assertFalse(player.isPaymentPossible(Config.Structure.CITY), "City can not be paid");
    }

    @Test
    public void testPay() {
        Map<Config.Resource, Integer> resources = new HashMap<>();

        resources.put(Config.Resource.ORE, 4);
        resources.put(Config.Resource.GRAIN, 3);
        player.setResources(resources);
        player.pay(Config.Structure.CITY);

        Map<Config.Resource, Integer> resourcesExpected = new HashMap<>();
        resourcesExpected.put(Config.Resource.ORE, 1);
        resourcesExpected.put(Config.Resource.GRAIN, 1);
        Map<Config.Resource, Integer> resourcesActual = player.getResources();

        for(Config.Resource resource : Config.Resource.values()) {
            if(resourcesActual.containsKey(resource)) {
                int expectedCount = resourcesExpected.get(resource);
                int actualCount = resourcesActual.get(resource);
                assertEquals(expectedCount, actualCount, "equals actual count of resources with expected");
            }
        }
    }
}
