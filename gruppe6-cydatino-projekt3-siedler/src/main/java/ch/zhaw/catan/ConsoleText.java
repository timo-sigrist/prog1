package ch.zhaw.catan;

/**
 * This Enum contains all text fragements for printing on the console
 *
 * @author baumgnoa, bergecyr, brundar, sigritim
 * @version 07.12.2021
 */
public enum ConsoleText {
    WELCOMEMESSAGE("Welcome to The Settlers of CATAN"),
    AMOUNTOFPLAYERS("Please enter the number players:"),
    STEALSUCCESSFUL("Stealing card from player was successful"),
    TRADESUCCESSUL("Trade with bank was successful"),
    TRADEFAILEDOUTOFRESOURCE("The bank is out of your requested resource, please trade a different one"),
    TRADEFAILEDNOTENOUGHRESOURCE("You dont have enough of the selected resource"),
    CHOOSXCOORDINATES("x-coordinates:"),
    CHOOSYCOORDINATES("y-coordinates:"),
    CHOOSEOPTION("What would you like to do?"),
    CHOOSERESOURCEOFFER("Which resource would you like to offer?"),
    CHOOSERESOURCEWANT("Which resource would you like to request?"),
    CHOOSEPLAYERTOSTEALFROM("Which player do you want to steal from?"),
    PLAYERHASNOCARDS("This player has no cards in his hand"),
    PLAYERTEXT("Player"),
    WHERETOBUILD("Where do you want to build your"),
    WHERETOPLACETHIEF("Where do you want to place the thief?"),
    SETTLEMENTTEXT("SETTLEMENT"),
    CITYTEXT("CITY"),
    ROADTEXTFROM("ROAD (from)"),
    ROADTEXTTO("ROAD (to)"),
    TEXTFORDICEROLL("You rolled a"),
    TEXTRESOURCESOFPLAYER("You have the following resources"),
    TEXTFORCURRENTTURN("It is your turn"),
    TEXTRESOURCESDISTRIBUTED("The following resources get distributed:"),
    WINNERTEXT("You won the game! Congratulations!"),
    QUITORNOT("Do you want to quit the game?"),
    SELECTRESOURCE("Please select resource"),
    RESOURCEFORTHIEF("What resource do you want to give to thief?"),
    ERRORNOTENOUGHRESOURCES("You don't have enough resources"),
    ERRORNOTALLOWEDTOBUILD("You can not build any structure in this stage of the game"),
    ERRORNOTNEXTTOTWOROADS("Settlement need to placed next to two roads!"),
    ERRORFIELDOCCUPIED("Coordinate is occupied!"),
    ERROROTHERFACTIONCLOSEBY("Coordinate is to close to another settlement!"),
    ERRORCANTBUILDINWATTER("Settlement can't be placed in the water! "),
    ERRORNOTACORNER("Coordinates does not match any corner!"),
    ERRORROADNEXTTOCONSTRUCTION("Road must be built adjacent to a Settlement, City or an existing road!"),
    ERRORROADINWATER("Road leads into the water!"),
    ERRORNOTANEDGE("Coordinates do not match any edge!"),
    ERRORNOTAFIELD("Coordinates do not match any field!"),
    ERRORTHEREISNOSETTLEMENT("At this place is no settlement!"),
    ERRORSETTLEMENTISNOTYOURS("The settlement at this place is not yours!"),
    ERRORTHEREISALREADYACITY("At this place is already a city!"),
    ERRORCANTPLACETHIEFINWATER("The thief can not swim!"),
    ERRORNEXTTOFIRSTSETTLEMENT("Road must be build by the first settlement"),
    ERRORNEXTTOSECONDSETTLEMENT("Road must be build by the second settlement"),
    ERRORSTRUCTURELIMITREACHED("Structure-limit is reached!");

    private String text;

    /**
     * Constructor of Enum, sets the Texts of the enum
     *
     * @param text Text to set
     */
    ConsoleText(String text) {
        this.text = text;
    }

    /**
     * This Method overrides the toString Methode to output the text of the enum
     *
     * @return Returns the text of the enum
     */
    @Override
    public String toString() {
        return text;
    }

}
