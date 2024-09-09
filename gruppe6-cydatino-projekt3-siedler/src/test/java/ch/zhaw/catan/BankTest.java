package ch.zhaw.catan;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class BankTest {

    private Bank bank;

    @BeforeEach
    public void initializeTests() {
        bank = new Bank();
    }

    @Test
    public void testGetInitialResourcesForLands() {
        List<Config.Land> lands = new ArrayList<>();
        lands.add(Config.Land.FOREST);
        lands.add(Config.Land.DESERT);
        lands.add(Config.Land.WATER);

        Map<Config.Resource, Integer> actualInitialResources = bank.getInitialResourcesForLands(lands);
        Map<Config.Resource, Integer> expectedInitialResources = new HashMap<>();
        expectedInitialResources.put(Config.Resource.LUMBER, 1);

        assertEquals(expectedInitialResources, actualInitialResources, "are the right initial resources in");
    }

    @Test
    public void testTradeWithBank() {
        for (int i=0; i < Config.INITIAL_RESOURCE_CARDS_BANK.get(Config.Resource.LUMBER); i++) {
            assertTrue(this.bank.tradeWithBank(Config.Resource.LUMBER, Config.Resource.WOOL));
        }
        assertFalse(this.bank.tradeWithBank(Config.Resource.LUMBER, Config.Resource.WOOL));
    }

}
