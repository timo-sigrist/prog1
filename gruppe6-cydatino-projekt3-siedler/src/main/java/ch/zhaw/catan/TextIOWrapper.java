package ch.zhaw.catan;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This Class generates a Wrapper for the TextIO
 *
 * @author baumgnoa, bergecyr, brundar, sigritim
 * @version 07.12.2021
 */
public class TextIOWrapper {
    static TextIO textIO = TextIoFactory.getTextIO();
    static TextTerminal<?> textTerminal = textIO.getTextTerminal();

    static {
        textTerminal.getProperties().setPromptColor(GameParameter.Color.WHITECOLOR.toString());
    }

    private TextIOWrapper() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Methode requests the amount of players for the game
     * return Integer between MINPLAYER and MAXPLAYER
     */
    public static int getNumbersOfPlayers() {
        return textIO.newIntInputReader()
                .withMinVal(Config.MIN_NUMBER_OF_PLAYERS)
                .withMaxVal(Config.Faction.values().length)
                .read(ConsoleText.AMOUNTOFPLAYERS.toString());
    }

    /**
     * Methode requests the position to build a road, settlement or city from the player
     *
     * @param construction  The Type of construction the current player wants to build
     * @param currentPlayer The name of the current player
     * @return Point where player wants to build
     */
    public static Point getConstructionPosition(ConsoleText construction, Player currentPlayer) {
        return getPosition(ConsoleText.WHERETOBUILD + " " + construction.toString() + "?", currentPlayer);
    }

    /**
     * Methode requests the position to place the thief
     *
     * @param currentPlayer The name of the current player
     * @return Point where player wants to place the thief
     */
    public static Point getThiefPosition(Player currentPlayer) {
        return getPosition(ConsoleText.WHERETOPLACETHIEF.toString(), currentPlayer);
    }

    /**
     * Methode requests the position to build or place something
     *
     * @param text          The input text
     * @param currentPlayer The name of the current player
     * @return Point where player wants to build or place something
     */
    private static Point getPosition(String text, Player currentPlayer) {
        concatenatePlayerText(currentPlayer);
        textTerminal.println(text);

        int x = textIO.newIntInputReader()
                .withMinVal(GameParameter.MINFIELD)
                .withMaxVal(GameParameter.MAXFIELD_X)
                .read(ConsoleText.CHOOSXCOORDINATES.toString());

        int y = textIO.newIntInputReader()
                .withMinVal(GameParameter.MINFIELD)
                .withMaxVal(GameParameter.MAXFIELD_Y)
                .read(ConsoleText.CHOOSYCOORDINATES.toString());

        return new Point(x, y);
    }

    /**
     * Methode requests the resource to trade with
     *
     * @param want Resource you want to request
     * @return Resource which a player wants to trade
     */
    public static Config.Resource getResource(boolean want) {
        Config.Resource resource;
        if (want) {
            resource = textIO.newEnumInputReader(Config.Resource.class)
                    .read(ConsoleText.CHOOSERESOURCEWANT.toString());
        } else {
            resource = textIO.newEnumInputReader(Config.Resource.class)
                    .read(ConsoleText.CHOOSERESOURCEOFFER.toString());
        }
        return resource;
    }

    /**
     * Prints out a selection for the possible moveoptions
     *
     * @param currentPlayer The name of the current player
     * @return move options (Enum)
     */
    public static MoveOption getOption(Player currentPlayer) {
        concatenatePlayerText(currentPlayer);
        return textIO.newEnumInputReader(MoveOption.class).read(ConsoleText.CHOOSEOPTION.toString());
    }

    /**
     * Methode prints out the welcome message in the beginning of the Game
     */
    public static void printWelcomeMessage() {
        textTerminal.println(ConsoleText.WELCOMEMESSAGE.toString());
    }

    /**
     * Methode prints out a simple line on console
     */
    public static void printLine(String text) {
        textTerminal.println(text);
    }

    /**
     * Methode prints out an error in red on console
     */
    public static void printError(String error) {
        textTerminal.getProperties().setPromptColor(GameParameter.Color.REDCOLOR.toString());
        textTerminal.println(error);
        textTerminal.getProperties().setPromptColor(GameParameter.Color.WHITECOLOR.toString());
    }


    /**
     * Prints out text with rolled dice value
     *
     * @param diceValue Value of dice
     */
    public static void printDiceRollText(int diceValue) {
        textTerminal.println(ConsoleText.TEXTFORDICEROLL + " " + diceValue);
    }

    /**
     * Prints out the available resources of the current player
     *
     * @param resources Ressources as Map
     */
    public static void printResources(Map<Config.Resource, Integer> resources) {
        textTerminal.println(ConsoleText.TEXTRESOURCESOFPLAYER.toString());
        for (Map.Entry<Config.Resource, Integer> resource : resources.entrySet()) {
            textTerminal.println(resource.getKey() + ": " + resource.getValue());
        }
    }

    /**
     * Print out whose turn it is
     *
     * @param currentPlayer Name of the current player
     */
    public static void printWhoseTurnItIs(Player currentPlayer) {
        concatenatePlayerText(currentPlayer);
        textTerminal.println(ConsoleText.TEXTFORCURRENTTURN.toString());
    }

    /**
     * Prints out the winner of the game
     *
     * @param currentPlayer Player name as String
     */
    public static void printWinner(Player currentPlayer) {
        concatenatePlayerText(currentPlayer);
        textTerminal.println(ConsoleText.WINNERTEXT.toString());
    }

    /**
     * Helper Methode prints the concatinated player name with the player text
     *
     * @param player Name of the player
     */
    private static void concatenatePlayerText(Player player) {
        GameParameter.Color playerColor = GameParameter.Color.WHITECOLOR;
        switch (player.getFaction()) {
            case RED:
                playerColor = GameParameter.Color.REDCOLOR;
                break;
            case BLUE:
                playerColor = GameParameter.Color.BLUECOLOR;
                break;
            case GREEN:
                playerColor = GameParameter.Color.GREENCOLOR;
                break;
            case YELLOW:
                playerColor = GameParameter.Color.YELLOWCOLOR;
                break;
        }

        textTerminal.getProperties().setPromptColor(playerColor.toString());
        textTerminal.print(ConsoleText.PLAYERTEXT + " " + player.getFaction().name() + ": ");
        textTerminal.getProperties().setPromptColor(GameParameter.Color.WHITECOLOR.toString());
    }

    /**
     * Methode requests player to select a card to give to the thief
     *
     * @param player              Name of the player
     * @param factionsToStealFrom faction of the player
     * @return Faction (Enum)
     */
    public static Config.Faction selectFactionToStealFrom(Player player, ArrayList<String> factionsToStealFrom) {
        concatenatePlayerText(player);
        return Config.Faction.valueOf(textIO.newStringInputReader()
                .withNumberedPossibleValues(factionsToStealFrom).read(ConsoleText.CHOOSEPLAYERTOSTEALFROM.toString()));
    }

    /**
     * Methode returns the resources that get distributed after dice was rolled
     *
     * @param resourcesToDistribute The resources that get distributed
     */
    public static void printDistributedResourcesForDiceThrow(Map<Config.Faction, List<Config.Resource>> resourcesToDistribute) {
        if (resourcesToDistribute.size() > 0) {
            textTerminal.println(ConsoleText.TEXTRESOURCESDISTRIBUTED.toString());
            for (Map.Entry<Config.Faction, List<Config.Resource>> entry : resourcesToDistribute.entrySet()) {
                textTerminal.print(entry.getKey().name() + ": ");
                for (Config.Resource resource : entry.getValue()) {
                    textTerminal.print(resource + " ");
                }
                textTerminal.println();
            }
        }
    }

    /**
     * Methode returns boolean if the player wants to quit
     *
     * @return boolean true/false if user wants to quit
     */
    public static boolean doYouWantToQuit() {
        return textIO.newBooleanInputReader().read(ConsoleText.QUITORNOT.toString());
    }
}
