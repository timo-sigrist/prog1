package ch.zhaw.catan;

/**
 * This enum represents the possible move option in Phase III of the game
 *
 * @author baumgnoa, bergecyr, brundar, sigritim
 * @version 07.12.2021
 */
public enum MoveOption {
    SHOW("SHOW BOARD"),
    BUILDROAD("BUILD ROAD"),
    BUILDSETTLEMENT("BUILD SETTLEMENT"),
    BUILDCITY("BUILD CITY"),
    SHOWRESSOURCES("SHOW RESSOURCES"),
    TRADEWITHBANK("TRADE WITH BANK"),
    ENDTURN("END TURN"),
    QUIT("QUIT");

    private String text;

    /**
     * Constructor of Enum, sets the Texts of the enum
     *
     * @param text Text to set
     */
    MoveOption(String text) {
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
