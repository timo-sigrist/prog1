package ch.zhaw.catan;

import ch.zhaw.catan.Config.Faction;
import ch.zhaw.catan.Config.Resource;

import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * This class performs all actions related to modifying the game state.
 *
 * @author baumgnoa, bergecyr, brundar, sigritim
 * @version 07.12.2021
 */
public class SiedlerGame {
    static final int FOUR_TO_ONE_TRADE_OFFER = 4;
    static final int FOUR_TO_ONE_TRADE_WANT = 1;
    private List<Player> players = new ArrayList<>();
    private SiedlerBoard board;
    private Player currentPlayer;
    private Bank bank;
    private int winPoints;
    private Random random = new Random();

    /**
     * Constructs a SiedlerGame game state object.
     *
     * @param winPoints       the number of points required to win the game
     * @param numberOfPlayers the number of players
     * @throws IllegalArgumentException if winPoints is lower than
     *                                  three or players is not between two and four
     */
    public SiedlerGame(int winPoints, int numberOfPlayers) {
        players.addAll(Player.initializePlayers(numberOfPlayers));
        if (!players.isEmpty()) {
            currentPlayer = players.get(0);
        }
        this.board = new SiedlerBoard();
        this.winPoints = winPoints;
        this.bank = new Bank();
    }

    /**
     * Returns searched faction
     */
    static Faction getFactionFromName(String name) {
        Faction searchedFaction = null;
        for (Faction faction : Faction.values()) {
            if (faction.toString().equals(name.toLowerCase())) {
                searchedFaction = faction;
            }
        }
        return searchedFaction;
    }

    /**
     * Switches to the next player in the defined sequence of players.
     */
    public void switchToNextPlayer() {
        try {
            currentPlayer = players.get(players.indexOf(currentPlayer) + 1);
        } catch (IndexOutOfBoundsException e) {
            currentPlayer = players.get(0);
        }
    }

    /**
     * Switches to the previous player in the defined sequence of players.
     */
    public void switchToPreviousPlayer() {
        try {
            currentPlayer = players.get(players.indexOf(currentPlayer) - 1);
        } catch (IndexOutOfBoundsException e) {
            currentPlayer = players.get(players.size() - 1);
        }
    }

    /**
     * Returns the {@link Faction}s of the active players.
     *
     * <p>The order of the player's factions in the list must
     * correspond to the oder in which they play.
     * Hence, the player that sets the first settlement must be
     * at position 0 in the list etc.
     *
     * <strong>Important note:</strong> The list must contain the
     * factions of active players only.</p>
     *
     * @return the list with player's factions
     */
    public List<Faction> getPlayerFactions() {
        List<Faction> factions = new ArrayList<>();
        for (Player player : players) {
            factions.add(player.getFaction());
        }
        return factions;
    }

    /**
     * Returns the game board.
     *
     * @return the game board
     */
    public SiedlerBoard getBoard() {
        return board;
    }

    /**
     * Returns the {@link Faction} of the current player.
     *
     * @return the faction of the current player
     */
    public Faction getCurrentPlayerFaction() {
        return currentPlayer.getFaction();
    }

    /**
     * Returns how many resource cards of the specified type
     * the current player owns.
     *
     * @param resource the resource type
     * @return the number of resource cards of this type
     */
    public int getCurrentPlayerResourceStock(Resource resource) {
        int amountOfGivenResource = 0;
        if (currentPlayer.getResources().get(resource) != null) {
            amountOfGivenResource = currentPlayer.getResources().get(resource);
        }
        return amountOfGivenResource;
    }

    /**
     * Places a settlement in the founder's phase (phase II) of the game.
     *
     * <p>The placement does not cost any resource cards. If payout is
     * set to true, for each adjacent resource-producing field, a resource card of the
     * type of the resource produced by the field is taken from the bank (if available) and added to
     * the players' stock of resource cards.</p>
     *
     * @param position the position of the settlement
     * @param payout   if true, the player gets one resource card per adjacent resource-producing field
     * @return true, if the placement was successful
     */
    public boolean placeInitialSettlement(Point position, boolean payout) {
        boolean successful = false;
        Settlement settlement = new Settlement(Config.Structure.SETTLEMENT, getCurrentPlayerFaction().toString());
        if (board.isValidBuildPointForSettlement(position, getCurrentPlayerFaction(), true) && !isBuildinglimitReached(settlement)) {
            board.setCorner(position, settlement.getName());
            successful = true;
            currentPlayer.addPoints(1);
            currentPlayer.addStructure(settlement);
            if (payout) {
                distributeInitialResources(position);
            }
        }

        return successful;
    }

    /**
     * Initial resource distribution, which gets resources from the bank and distributes them to the players
     *
     * @param position corner where the initial building was
     */
    private void distributeInitialResources(Point position) {
        List<Config.Land> lands = board.getLandsForCorner(position);
        Map<Config.Resource, Integer> initialResources = bank.getInitialResourcesForLands(lands);

        for (Map.Entry<Resource, Integer> initialResource : initialResources.entrySet()) {
            currentPlayer.addResources(initialResource.getKey(), initialResource.getValue());
        }
    }

    /**
     * Places a road in the founder's phase (phase II) of the game.
     * The placement does not cost any resource cards.
     *
     * @param roadStart position of the start of the road
     * @param roadEnd   position of the end of the road
     * @return true, if the placement was successful
     */
    public boolean placeInitialRoad(Point roadStart, Point roadEnd) {
        if (board.isValidBuildPointForRoad(roadStart, roadEnd, currentPlayer, true) && !isRoadlimitReached()) {
            board.setEdge(roadStart, roadEnd, getCurrentPlayerFaction().toString());
            currentPlayer.addRoad();
            return true;
        }
        return false;
    }

    /**
     * This method takes care of actions depending on the dice throw result.
     * <p>
     * A key action is the payout of the resource cards to the players
     * according to the payout rules of the game. This includes the
     * "negative payout" in case a 7 is thrown and a player has more than
     * {@link Config#MAX_CARDS_IN_HAND_NO_DROP} resource cards.
     * <p>
     * If a player does not get resource cards, the list for this players'
     * {@link Faction} is <b>an empty list (not null)</b>!.
     *
     * <p>
     * The payout rules of the game take into account factors such as, the number
     * of resource cards currently available in the bank, settlement types
     * (settlement or city), and the number of players that should get resource
     * cards of a certain type (relevant if there are not enough left in the bank).
     * </p>
     *
     * @param dicethrow the resource cards that have been distributed to the players
     * @return the resource cards added to the stock of the different players
     */
    public Map<Faction, List<Resource>> throwDice(int dicethrow) {
        //  We return an empty list of resources if a player does not get any resource cards and not null
        Map<Faction, List<Resource>> resourceToStock = prepareEmptyResourceStockForEachPlayer();

        //  We go through each field of the given dicethrow
        List<Point> fieldPoints = board.getFieldsForDiceValue(dicethrow);
        for (Point fieldPoint : fieldPoints) {
            if (!fieldPoint.equals(board.getThiefField())) {
                Resource resource = board.getField(fieldPoint).getResource();
                List<String> corners = board.getCornersOfField(fieldPoint);
                //  For each corner of given field we check if there is a settlement (or city) and put the resource.
                for (String corner : corners) {
                    if (corner != null) {
                        Faction faction = getFactionFromName(corner);
                        List<Resource> currentResource = resourceToStock.get(faction);
                        currentResource.add(resource);
                        //  If corner has a city, we just add a second resource to given faction
                        if (Character.isUpperCase(corner.charAt(0)) && Character.isUpperCase(corner.charAt(1))) {
                            currentResource.add(resource);
                        }
                        resourceToStock.put(faction, currentResource);
                    }
                }
            }
        }

        payoutResourcesForDiceThrow(resourceToStock);

        return resourceToStock;
    }

    /**
     * Helper Methode that creates an empty resource stock for each player
     *
     * @return resourceStock An empty resource stock
     */
    private Map<Faction, List<Resource>> prepareEmptyResourceStockForEachPlayer() {
        Map<Faction, List<Resource>> resourceToStock = new HashMap<>();
        for (Player player : players) {
            resourceToStock.put(player.getFaction(), new ArrayList<>());
        }
        return resourceToStock;
    }

    /**
     * Builds a settlement at the specified position on the board.
     *
     * <p>The settlement can be built if:
     * <ul>
     * <li> the player possesses the required resource cards</li>
     * <li> a settlement to place on the board</li>
     * <li> the specified position meets the build rules for settlements</li>
     * </ul>
     *
     * @param position the position of the settlement
     * @return true, if the placement was successful
     */
    public boolean buildSettlement(Point position) {
        boolean built = false;
        Settlement settlement = new Settlement(Config.Structure.SETTLEMENT, getCurrentPlayerFaction().toString());
        if (board.isValidBuildPointForSettlement(position, getCurrentPlayerFaction(), false) && !isBuildinglimitReached(settlement) && currentPlayer.pay(settlement.getStructure())) {
            board.setCorner(position, settlement.getName());
            built = true;
            currentPlayer.addPoints(1);
            currentPlayer.addStructure(settlement);
        }
        return built;
    }

    /**
     * Builds a city at the specified position on the board.
     *
     * <p>The city can be built if:
     * <ul>
     * <li> the player possesses the required resource cards</li>
     * <li> a city to place on the board</li>
     * <li> the specified position meets the build rules for cities</li>
     * </ul>
     *
     * @param position the position of the city
     * @return true, if the placement was successful
     */
    public boolean buildCity(Point position) {
        boolean built = false;
        City city = new City(Config.Structure.CITY, getCurrentPlayerFaction().toString());
        if (board.isValidBuildPointForCity(position, getCurrentPlayerFaction()) && !isBuildinglimitReached(city) && currentPlayer.pay(city.getStructure())) {
            board.setCorner(position, getCurrentPlayerFaction().toString().toUpperCase());
            built = true;
            currentPlayer.addPoints(1);
            currentPlayer.addStructure(city);
            currentPlayer.removeStructure(new Settlement(Config.Structure.SETTLEMENT, getCurrentPlayerFaction().toString()));
        }
        return built;
    }

    /**
     * Builds a road at the specified position on the board.
     *
     * <p>The road can be built if:
     * <ul>
     * <li> the player possesses the required resource cards</li>
     * <li> a road to place on the board</li>
     * <li> the specified position meets the build rules for roads</li>
     * </ul>
     *
     * @param roadStart the position of the start of the road
     * @param roadEnd   the position of the end of the road
     * @return true, if the placement was successful
     */
    public boolean buildRoad(Point roadStart, Point roadEnd) {
        boolean built = false;
        if (board.isValidBuildPointForRoad(roadStart, roadEnd, currentPlayer, false) && !isRoadlimitReached()) {
            if (currentPlayer.pay(Config.Structure.ROAD)) {
                board.setEdge(roadStart, roadEnd, getCurrentPlayerFaction().toString());
                built = true;
                currentPlayer.addRoad();
            } else {
                TextIOWrapper.printError(ConsoleText.ERRORNOTALLOWEDTOBUILD.toString());
            }
        }
        return built;
    }

    /**
     * Check if the limit of given building is reached
     *
     * @param building type of building
     * @return boolean
     */
    boolean isBuildinglimitReached(Building building) {
        if (currentPlayer.getStructures().get(building.getClass()) != null && currentPlayer.getStructures().get(building.getClass()) >= building.getStructure().getStockPerPlayer()) {
            TextIOWrapper.printError(ConsoleText.ERRORSTRUCTURELIMITREACHED.toString());
            return true;
        }
        return false;
    }

    /**
     * Check if the limit of roads is reached
     *
     * @return boolean
     */
    boolean isRoadlimitReached() {
        if (currentPlayer.getRoadCount() >= Config.Structure.ROAD.getStockPerPlayer()) {
            TextIOWrapper.printError(ConsoleText.ERRORSTRUCTURELIMITREACHED.toString());
            return true;
        }
        return false;
    }

    /**
     * Trades in {@link #FOUR_TO_ONE_TRADE_OFFER} resource cards of the
     * offered type for {@link #FOUR_TO_ONE_TRADE_WANT} resource cards of the wanted type.
     * <p>
     * The trade only works when bank and player possess the resource cards
     * for the trade before the trade is executed.
     *
     * @param offer offered type
     * @param want  wanted type
     * @return true, if the trade was successful
     */
    public boolean tradeWithBankFourToOne(Resource offer, Resource want) {
        boolean successful = false;
        if (currentPlayer.getResources().get(offer) >= SiedlerGame.FOUR_TO_ONE_TRADE_OFFER) {
            if (bank.tradeWithBank(offer, want)) {
                currentPlayer.removeResources(offer, FOUR_TO_ONE_TRADE_OFFER);
                currentPlayer.addResources(want, FOUR_TO_ONE_TRADE_WANT);
                successful = true;
                TextIOWrapper.printLine(ConsoleText.TRADESUCCESSUL.toString());
            } else {
                TextIOWrapper.printLine(ConsoleText.TRADEFAILEDOUTOFRESOURCE.toString());
            }
        } else {
            TextIOWrapper.printLine(ConsoleText.TRADEFAILEDNOTENOUGHRESOURCE.toString());
        }
        return successful;
    }

    /**
     * Returns the winner of the game, if any.
     *
     * @return the winner of the game or null, if there is no winner (yet)
     */
    public Faction getWinner() {
        Faction winner = null;
        for (Player current : players) {
            if (current.getPoints() >= winPoints) {
                winner = current.getFaction();
                TextIOWrapper.printWinner(current);
            }
        }
        return winner;
    }

    /**
     * Places the thief on the specified field and steals a random resource card (if
     * the player has such cards) from a random player with a settlement at that
     * field (if there is a settlement) and adds it to the resource cards of the
     * current player.
     *
     * @param field the field on which to place the thief
     * @return false, if the specified field is not a field or the thief cannot be
     * placed there (e.g., on water)
     */
    public boolean placeThiefAndStealCard(Point field) {
        thiefChecksAndStealsCardsOfPlayers();
        boolean placed = false;
        if (board.isValidPlacementPointForThief(field)) {
            board.setThiefField(field);
            placed = true;

            ArrayList<String> factionsToStealFrom = new ArrayList<>();
            for (String corner : board.getCornersOfField(field)) {
                Faction faction = getFactionFromName(corner);
                if (faction != null && faction != currentPlayer.getFaction() && !factionsToStealFrom.contains(faction.name())) {
                    factionsToStealFrom.add(faction.name());
                }
            }
            playerStealsCard(factionsToStealFrom);
        }

        return placed;
    }

    /**
     * Thief checks how many cards the players have and steals the half, if one has more or equal 7 cards     *
     */
    void thiefChecksAndStealsCardsOfPlayers() {
        for (Player player : players) {
            if (player.getCardsCount() >= 7) {
                int amountOfCardsToRemove = player.getCardsCount() / 2;
                for (int i = 0; i < amountOfCardsToRemove; i++) {
                    player.removeResources(getRandomAvailableResourceFromPlayer(player), 1);
                }
            }
        }
    }

    /**
     * Player Steals a card
     *
     * @param factionsToStealFrom from which factions should be stolen from
     */
    void playerStealsCard(ArrayList<String> factionsToStealFrom) {
        Faction selectedFactionToStealFrom = null;
        Player playerToStealFrom = null;
        // if thief is placed in nirwana we dont steal from any player
        if (!factionsToStealFrom.isEmpty()) {
            while (selectedFactionToStealFrom == null) {
                // if there is only one player adjacent to field where thief is placed we steal from this player
                if (factionsToStealFrom.size() == 1) {
                    selectedFactionToStealFrom = Faction.valueOf(factionsToStealFrom.get(0));
                } else {
                    // otherwise we let the current player decide where to steal a card from
                    selectedFactionToStealFrom = TextIOWrapper.selectFactionToStealFrom(currentPlayer, factionsToStealFrom);
                }
            }
            for (Player player : players) {
                if (player.getFaction().equals(selectedFactionToStealFrom)) {
                    playerToStealFrom = player;
                }
            }
        }
        if (playerToStealFrom != null) {
            if (playerToStealFrom.getCardsCount() > 0) {
                Resource chosenResource = getRandomAvailableResourceFromPlayer(playerToStealFrom);
                playerToStealFrom.removeResources(chosenResource, 1);
                currentPlayer.addResources(chosenResource, 1);
                TextIOWrapper.printLine(ConsoleText.STEALSUCCESSFUL.toString());
            } else {
                TextIOWrapper.printError(ConsoleText.PLAYERHASNOCARDS.toString());
            }
        }
    }

    /**
     * Get a random available resource from a player
     *
     * @param playerToStealFrom player to steal the resource from
     * @return a random available resource
     */
    Resource getRandomAvailableResourceFromPlayer(Player playerToStealFrom) {
        List<Resource> availableResourcesOfPlayer = new ArrayList<>();
        if (playerToStealFrom.getCardsCount() > 0) {
            Map<Config.Resource, Integer> allResourcesOfPlayer = playerToStealFrom.getResources();
            for (Map.Entry<Config.Resource, Integer> resource : allResourcesOfPlayer.entrySet()) {
                if (resource.getValue() > 0) {
                    availableResourcesOfPlayer.add(resource.getKey());
                }
            }
            return availableResourcesOfPlayer.get(random.nextInt(availableResourcesOfPlayer.size()));
        } else {
            TextIOWrapper.printError(ConsoleText.PLAYERHASNOCARDS.toString());
        }
        return null;
    }

    /**
     * Get a list of all the players
     *
     * @return List with players
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Get the current player
     *
     * @return Returns the current player
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Methode pays out resources after the dice was rolled
     *
     * @param resourcesToDistribute Map with the player and the resources they get
     */
    void payoutResourcesForDiceThrow(Map<Faction, List<Resource>> resourcesToDistribute) {
        for (Player player : players) {
            Faction factionOfPlayer = player.getFaction();
            if (resourcesToDistribute.containsKey(factionOfPlayer)) {
                List<Resource> resources = resourcesToDistribute.get(factionOfPlayer);
                for (Resource resource : resources) {
                    player.addResources(resource, 1);
                }
            }
        }
        TextIOWrapper.printDistributedResourcesForDiceThrow(resourcesToDistribute);
    }

}
