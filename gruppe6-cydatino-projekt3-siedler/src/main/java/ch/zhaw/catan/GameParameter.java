package ch.zhaw.catan;

/**
 * This class defines the game parameters
 *
 * @author baumgnoa, bergecyr, brundar, sigritim
 * @version 07.12.2021
 */
public class GameParameter {
    //Set game parameters
    static final int WINPOINTS = 10;
    static final int MINFIELD = 0;
    static final int MAXFIELD_X = 14;
    static final int MAXFIELD_Y = 22;

    // Available colors
    public enum Color {
        WHITECOLOR("rgba(255,255,255,1)"),
        REDCOLOR("rgba(255,0,0,1)"),
        GREENCOLOR("rgba(0,255,0,1)"),
        BLUECOLOR("rgba(0,0,255,1)"),
        YELLOWCOLOR("rgba(255,255,0,1)");

        private String name;

        Color(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }


}
