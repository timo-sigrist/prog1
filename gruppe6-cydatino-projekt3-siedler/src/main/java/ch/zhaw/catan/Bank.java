package ch.zhaw.catan;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This Class represents the Bank of the Game
 *
 * @author baumgnoa, bergecyr, brundar, sigritim
 * @version 10.12.2021
 */
public class Bank {
    private Map<Config.Resource, Integer> resources = new HashMap<>();

    /**
     * Default constructor for Bank. Sets the bank's ressources according to the game parameters
     */
    public Bank() {
        resources.put(Config.Resource.LUMBER, Config.INITIAL_RESOURCE_CARDS_BANK.get(Config.Resource.LUMBER));
        resources.put(Config.Resource.GRAIN, Config.INITIAL_RESOURCE_CARDS_BANK.get(Config.Resource.GRAIN));
        resources.put(Config.Resource.BRICK, Config.INITIAL_RESOURCE_CARDS_BANK.get(Config.Resource.BRICK));
        resources.put(Config.Resource.WOOL, Config.INITIAL_RESOURCE_CARDS_BANK.get(Config.Resource.WOOL));
        resources.put(Config.Resource.ORE, Config.INITIAL_RESOURCE_CARDS_BANK.get(Config.Resource.ORE));
    }

    /**
     * Remove initial resources from bank for adjacent lands
     *
     * @param lands adjacent lands
     * @return map of initial resources and their count
     */
    public Map<Config.Resource, Integer> getInitialResourcesForLands(List<Config.Land> lands) {
        Map<Config.Resource, Integer> initialResources = new HashMap<>();
        for (Config.Land land : lands) {
            Config.Resource resource = land.getResource();
            // Here we want to exclude water and desert
            if (resource != null) {
                int totalCards = resources.get(resource);

                totalCards--;
                resources.put(resource, totalCards);
                initialResources.merge(resource, 1, Integer::sum);
            }
        }
        return initialResources;
    }

    /**
     * This Methode implements the trading with
     *
     * @param offer The ressources the player offers
     * @param want  The ressources the player wants
     * @return returns true if successfull
     */
    public boolean tradeWithBank(Config.Resource offer, Config.Resource want) {
        boolean successful = false;
        if (resources.get(want) > 0) {
            resources.put(offer, resources.get(offer) + SiedlerGame.FOUR_TO_ONE_TRADE_OFFER);
            resources.put(want, resources.get(want) - SiedlerGame.FOUR_TO_ONE_TRADE_WANT);
            successful = true;
        }
        return successful;
    }
}
