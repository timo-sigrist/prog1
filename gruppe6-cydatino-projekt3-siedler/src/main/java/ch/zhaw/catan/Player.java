package ch.zhaw.catan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a single player with its resources
 *
 * @author baumgnoa, bergecyr, brundar, sigritim
 * @version 07.12.2021
 */
public class Player {
    private Map<Config.Resource, Integer> resources;
    private Map<Class, Integer> structures;
    private int roadCount;
    private Config.Faction faction;
    private int points;

    /**
     * Default Constructor: Constructs a new Player object
     */
    public Player() {
        resources = new HashMap<>();
        structures = new HashMap<>();

        resources.put(Config.Resource.BRICK, 0);
        resources.put(Config.Resource.GRAIN, 0);
        resources.put(Config.Resource.LUMBER, 0);
        resources.put(Config.Resource.ORE, 0);
        resources.put(Config.Resource.WOOL, 0);
    }

    /**
     * Initializes the list of players
     *
     * @param numberOfPlayers player count
     * @return player list
     */
    public static List<Player> initializePlayers(int numberOfPlayers) {
        List<Player> players = new ArrayList<>();

        for (int i = 1; i < numberOfPlayers + 1; i++) {
            Player player = new Player();
            if (i == 1) {
                player.setFaction(Config.Faction.RED);
            }
            if (i == 2) {
                player.setFaction(Config.Faction.BLUE);
            }
            if (i == 3) {
                player.setFaction(Config.Faction.GREEN);
            }
            if (i == 4) {
                player.setFaction(Config.Faction.YELLOW);
            }
            players.add(player);
        }
        return players;
    }

    /**
     * Add n points to the player
     *
     * @param addedPoints n points
     */
    public void addPoints(int addedPoints) {
        this.points += addedPoints;
    }

    /**
     * Add structures with their count
     * if a structure already exists, the value will increment
     *
     * @param building structure type
     */
    public void addStructure(Building building) {
        this.structures.put(building.getClass(), this.structures.get(building.getClass()) != null ? this.structures.get(building.getClass()) + 1 : 1);
    }

    /**
     * Count the roadcount up
     */
    public void addRoad() {
        roadCount++;
    }

    /**
     * Returns the number of roads placed
     *
     * @return roadcount
     */
    public int getRoadCount() {
        return roadCount;
    }

    /**
     * Remove structure with their count
     *
     * @param building building
     */
    public void removeStructure(Building building) {
        this.structures.put(building.getClass(), this.structures.get(building.getClass()) - 1);
    }

    public Config.Faction getFaction() {
        return faction;
    }

    /**
     * Sets the color of the current player
     */
    public void setFaction(Config.Faction faction) {
        this.faction = faction;
    }

    /**
     * Methode returns the points of the current player
     *
     * @return points of the current player
     */
    public int getPoints() {
        return points;
    }

    /**
     * Returns the cards of the current player
     *
     * @return cardsCount Cards of current player
     */
    public int getCardsCount() {
        int cardsCount = 0;
        for (Integer resourceAmount : resources.values()) {
            cardsCount += resourceAmount;
        }
        return cardsCount;
    }

    /**
     * Returns a Map with the resources of the current player
     *
     * @return Returns a Map with resources of the player
     */
    public Map<Config.Resource, Integer> getResources() {
        return resources;
    }

    /**
     * Sets the resources for the current player
     *
     * @param resources a Map with resources of the player
     */
    public void setResources(Map<Config.Resource, Integer> resources) {
        this.resources = resources;
    }

    /**
     * Method to get structures
     *
     * @return structure map (with count per structure)
     */
    public Map<Class, Integer> getStructures() {
        return structures;
    }

    /**
     * Pay structures
     *
     * @param structure which should be paid
     * @return shows if payment was successful
     */
    public boolean pay(Config.Structure structure) {
        Map<Config.Resource, Integer> resourceCosts = getResourceCosts(structure);
        boolean paymentPossible = isPaymentPossible(structure);

        if (paymentPossible) {
            for (Map.Entry<Config.Resource, Integer> resourceCost : resourceCosts.entrySet()) {
                Integer localCount = resources.get(resourceCost.getKey());
                localCount = localCount - resourceCost.getValue();
                resources.put(resourceCost.getKey(), localCount);
            }
        }
        return paymentPossible;
    }

    /**
     * Interpret structure costs to list with structures and costs
     *
     * @param structure structure type
     * @return costs per resource
     */
    private Map<Config.Resource, Integer> getResourceCosts(Config.Structure structure) {
        Map<Config.Resource, Integer> resourceCosts = new HashMap<>();
        for (Config.Resource resource : structure.getCosts()) {
            Integer costs = 0;
            if (resourceCosts.containsKey(resource)) costs = resourceCosts.get(resource);
            costs++;
            resourceCosts.put(resource, costs);
        }
        return resourceCosts;
    }

    /**
     * Methode checks whether payment is possible for the provided structure
     *
     * @param structure costs per resource
     * @return True if payment is possible, false otherwise
     */
    public boolean isPaymentPossible(Config.Structure structure) {
        boolean paymentPossible = true;
        Map<Config.Resource, Integer> resourceCosts = getResourceCosts(structure);

        for (Map.Entry<Config.Resource, Integer> resourceCost : resourceCosts.entrySet()) {
            if (resources.containsKey(resourceCost.getKey())) {
                Integer localCount = resources.get(resourceCost.getKey());
                if (localCount < resourceCost.getValue()) {
                    paymentPossible = false;
                }
            } else {
                paymentPossible = false;
            }
        }

        if (!paymentPossible) {
            TextIOWrapper.printError(ConsoleText.ERRORNOTENOUGHRESOURCES.toString());
        }
        return paymentPossible;
    }

    /**
     * Adding resources for the current player
     *
     * @param resource Type of resource
     * @param amount   Amount of resources to add
     */
    public void addResources(Config.Resource resource, int amount) {
        int newVal = 0;
        if (resources.containsKey(resource)) {
            newVal = resources.get(resource);
        }
        resources.put(resource, newVal + amount);
    }

    /**
     * Remove resources for the current player
     *
     * @param resource Type of resource
     * @param amount   Amount of resources to add
     */
    public boolean removeResources(Config.Resource resource, int amount) {
        int newVal = 0;
        if (resources.containsKey(resource)) {
            newVal = resources.get(resource);
        }
        if (newVal >= amount) {
            resources.put(resource, newVal - amount);
            return true;
        } else {
            return false;
        }
    }
}
