package ch.zhaw.catan;

import java.awt.*;
import java.util.List;
import java.util.Random;

/**
 * This Class runs through the three phases of the game
 * Phase 1: Starting game and creating the initial Board
 * Phase 2: Place the first two Settlements and Roads for every player
 * Phase 3: Runs the actual Game
 *
 * @author baumgnoa, bergecyr, brundar, sigritim
 * @version 07.12.2021
 */

public class GameManager {
    private SiedlerGame game;
    private SiedlerBoardTextView view;
    private Random random;

    /**
     * Enum represents the Phases in which the player is allowed to build cunstructions
     */
    private enum Phase {
        TWO, THREE
    }

    /**
     * Constructor of GameManager, starts the individual phases of the game
     */
    public GameManager() {
        runPhaseOne();
        runPhaseTwo();
        runPhaseThree();
    }

    /**
     * Methode that runs Phase 1 of Game (starting game and creating the initial Board)
     */
    private void runPhaseOne() {
        TextIOWrapper.printWelcomeMessage();
        int winningPoints = GameParameter.WINPOINTS;
        int numbersOfPlayers = TextIOWrapper.getNumbersOfPlayers();

        game = new SiedlerGame(winningPoints, numbersOfPlayers);
        view = new SiedlerBoardTextView(game.getBoard());

        random = new Random();
    }

    /**
     * Methode that runs Phase 2 of Game (place the first two Settlements and Roads for every player)
     */
    private void runPhaseTwo() {
        List<Player> players = game.getPlayers();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < players.size(); j++) {

                //Show board
                TextIOWrapper.printLine(view.toString());
                //Build Settlement
                while (!buildSettlementForPlayer(Phase.TWO, i == 1)) ;

                //Show board
                TextIOWrapper.printLine(view.toString());
                //Build Settlement
                while (!buildRoadForPlayer(Phase.TWO)) ;

                //Switch to next player until player - 1
                if (j < players.size() - 1) {
                    //First Round of Phase II
                    if (i == 0) {
                        game.switchToNextPlayer();
                    }
                    //Second Round of Phase II
                    if (i == 1) {
                        game.switchToPreviousPlayer();
                    }
                }
            }
        }
    }

    /**
     * Methode that runs Phase 3 of Game (Runs the actual Game)
     */
    private void runPhaseThree() {
        boolean quit;

        //Start new Turn (fence post)
        startNewTurn();

        do {
            //Ask Player for Option
            MoveOption selectedMoveOption = TextIOWrapper.getOption(game.getCurrentPlayer());

            // Catch exit command
            quit = chooseMoveOption(selectedMoveOption);
            if (quit) {
                quit = TextIOWrapper.doYouWantToQuit();
            }

        } while (!quit && game.getWinner() == null);
    }

    /**
     * Methode executes the selected move option in phase III
     *
     * @param moveOption to choose an option
     */
    boolean chooseMoveOption(MoveOption moveOption) {
        switch (moveOption) {
            case SHOW:
                TextIOWrapper.printLine(view.toString());
                break;
            case BUILDROAD:
                buildRoadForPlayer(Phase.THREE);
                break;
            case BUILDSETTLEMENT:
                buildSettlementForPlayer(Phase.THREE, false);
                break;
            case BUILDCITY:
                buildCityForPlayer();
                break;
            case SHOWRESSOURCES:
                TextIOWrapper.printResources(game.getCurrentPlayer().getResources());
                break;
            case TRADEWITHBANK:
                game.tradeWithBankFourToOne(TextIOWrapper.getResource(false), TextIOWrapper.getResource(true));
                break;
            case ENDTURN:
                game.switchToNextPlayer();
                startNewTurn();
                break;
            case QUIT:
                return true;
            default:
                throw new IllegalStateException("Unexpected value: " + moveOption);
        }
        return false;
    }

    /**
     * Methode helps to build roads in phase II and III
     *
     * @param phase Enum of the current phase of the game
     * @return boolean Gives true, if building of road was successfull
     */
    boolean buildRoadForPlayer(Phase phase) {
        boolean built = false;
        Player currentPlayer = game.getCurrentPlayer();
        Point cornerStreetFrom;
        Point cornerStreetTo;

        if (phase == Phase.TWO) {
            cornerStreetFrom = TextIOWrapper.getConstructionPosition(ConsoleText.ROADTEXTFROM, currentPlayer);
            cornerStreetTo = TextIOWrapper.getConstructionPosition(ConsoleText.ROADTEXTTO, currentPlayer);
            built = game.placeInitialRoad(cornerStreetFrom, cornerStreetTo);
        } else if (phase == Phase.THREE) {
            if (currentPlayer.isPaymentPossible(Config.Structure.ROAD)) {
                cornerStreetFrom = TextIOWrapper.getConstructionPosition(ConsoleText.ROADTEXTFROM, currentPlayer);
                cornerStreetTo = TextIOWrapper.getConstructionPosition(ConsoleText.ROADTEXTTO, currentPlayer);
                built = game.buildRoad(cornerStreetFrom, cornerStreetTo);
            }
        } else {
            TextIOWrapper.printError(ConsoleText.ERRORNOTALLOWEDTOBUILD.toString());
        }
        return built;
    }

    /**
     * Methode helps to build settlements in phase II and III
     *
     * @param phase  Enum of the current phase of the game
     * @param payout Boolean if there should be a payout
     * @return boolean Gives true, if building of settlement was successfull
     */
    boolean buildSettlementForPlayer(Phase phase, Boolean payout) {
        boolean built = false;
        Player currentPlayer = game.getCurrentPlayer();
        Point cornerForSettlement;

        if (phase == Phase.TWO) {
            cornerForSettlement = TextIOWrapper.getConstructionPosition(ConsoleText.SETTLEMENTTEXT, currentPlayer);
            built = game.placeInitialSettlement(cornerForSettlement, payout);
        } else if (phase == Phase.THREE) {
            if (currentPlayer.isPaymentPossible(Config.Structure.SETTLEMENT)) {
                cornerForSettlement = TextIOWrapper.getConstructionPosition(ConsoleText.SETTLEMENTTEXT, currentPlayer);
                built = game.buildSettlement(cornerForSettlement);
            }
        } else {
            TextIOWrapper.printError(ConsoleText.ERRORNOTALLOWEDTOBUILD.toString());
        }

        return built;
    }

    /**
     * Methode helps to build cities
     *
     * @return boolean Gives true, if building of city was successfull
     */
    boolean buildCityForPlayer() {
        boolean built = false;
        Player currentPlayer = game.getCurrentPlayer();
        Point cornerForCity;

        if (currentPlayer.isPaymentPossible(Config.Structure.CITY)) {
            cornerForCity = TextIOWrapper.getConstructionPosition(ConsoleText.CITYTEXT, currentPlayer);
            built = game.buildCity(cornerForCity);
        }
        return built;
    }

    /**
     * Methode creates a random Number between 1 and 12
     *
     * @return random integer from 1 to 12
     */
    int generateRandomDiceValue() {
        int diceValue = random.nextInt(6) + 1 + random.nextInt(6) + 1;
        TextIOWrapper.printDiceRollText(diceValue);
        return diceValue;
    }

    /**
     * Methode starts next turn, rolls the dice and Output whose turn it is
     */
    private void startNewTurn() {
        //Show board
        TextIOWrapper.printLine(view.toString());
        //Print Whose turn it is
        TextIOWrapper.printWhoseTurnItIs(game.getCurrentPlayer());
        //Auto roll the dice for next player
        int diceValue = generateRandomDiceValue();
        if (diceValue == 7) {
            while (!placeThiefForPlayer()) ;
            view.updateThiefPlacement();
        } else {
            game.throwDice(diceValue);
        }
    }

    /**
     * Methode helps to build cities
     *
     * @return boolean Gives true, if building of city was successfull
     */
    boolean placeThiefForPlayer() {
        boolean placed;
        Player currentPlayer = game.getCurrentPlayer();
        Point fieldForThief = TextIOWrapper.getThiefPosition(currentPlayer);

        placed = game.placeThiefAndStealCard(fieldForThief);
        return placed;
    }

}
