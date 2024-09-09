package ch.zhaw.catan;

import ch.zhaw.catan.Config.Land;
import ch.zhaw.hexboard.HexBoard;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class implements the board of the siedler game, which is built out of 37 hexagon fields
 *
 * @author baumgnoa, bergecyr, brundar, sigritim
 * @version 10.12.2021
 */
public class SiedlerBoard extends HexBoard<Land, String, String, String> {
    private Point thiefField;

    /**
     * Standard constructor of the board
     */
    public SiedlerBoard() {
        buildSiedlerBoard();
        thiefField = Config.INITIAL_THIEF_POSITION;
    }

    /**
     * Returns the fields associated with the specified dice value.
     *
     * @param dice the dice value
     * @return the fields associated with the dice value
     */
    public List<Point> getFieldsForDiceValue(int dice) {
        List<Point> diceFieldPoints = new ArrayList<>();
        for (Map.Entry<Point, Integer> point : Config.getStandardDiceNumberPlacement().entrySet()) {
            if (point.getValue() == dice) {
                diceFieldPoints.add(point.getKey());
            }
        }
        return diceFieldPoints;
    }

    /**
     * Returns the {@link Land}s adjacent to the specified corner.
     *
     * @param corner the corner
     * @return the list with the adjacent {@link Land}s
     */
    public List<Land> getLandsForCorner(Point corner) {
        return getFields(corner);
    }

    /**
     * Set the thief on given field
     *
     * @param thiefField
     */
    public void setThiefField(Point thiefField) {
        this.thiefField = thiefField;
    }

    /**
     * get the current field of the thief
     *
     * @return field where thief is
     */
    public Point getThiefField() {
        return thiefField;
    }

    /**
     * Builds the SiedlerBoard to play the game on
     */
    private void buildSiedlerBoard() {
        Map<Point, Land> landPlacement = Config.getStandardLandPlacement();
        for (Point point : landPlacement.keySet()) {
            addField(point, landPlacement.get(point));
        }
    }

    /**
     * Checks if it's possible to build a Building at given coordinate
     * Checks: if place is empty, valid coordinates, when initial -> if own road is adjacent and if its in the water
     *
     * @param coordinate    coordinate to check
     * @param playerFaction faction of player who wants to build
     * @param initial       if it's an initial settlement
     * @return check result
     */
    boolean isValidBuildPointForSettlement(Point coordinate, Config.Faction playerFaction, boolean initial) {
        if (hasCorner(coordinate)) {
            if (!isCornerSurroundedByWater(coordinate)) {
                if (getNeighboursOfCorner(coordinate).isEmpty()) {
                    if (getCorner(coordinate) == null) {
                        if (initial) {
                            return true;
                        }
                        if (hasAdjacentRoad(coordinate, playerFaction)) {
                            return true;
                        }
                        TextIOWrapper.printError(ConsoleText.ERRORNOTNEXTTOTWOROADS.toString());
                        return false;
                    }
                    TextIOWrapper.printError(ConsoleText.ERRORFIELDOCCUPIED.toString());
                    return false;
                }
                TextIOWrapper.printError(ConsoleText.ERROROTHERFACTIONCLOSEBY.toString());
                return false;
            }
            TextIOWrapper.printError(ConsoleText.ERRORCANTBUILDINWATTER.toString());
            return false;
        }
        TextIOWrapper.printError(ConsoleText.ERRORNOTACORNER.toString());
        return false;
    }

    /**
     * Checks if it's possible to build a Building at given coordinate
     * Checks: if place is empty, valid coordinates, when initial -> if own road is adjacent and if its in the water
     *
     * @param coordinate    coordinate to check
     * @param playerFaction faction of player who wants to build
     * @return check result
     */
    boolean isValidBuildPointForCity(Point coordinate, Config.Faction playerFaction) {
        boolean buildValid = false;
        if (hasCorner(coordinate)) {
            String corner = getCorner(coordinate);
            if (corner == null) {
                TextIOWrapper.printError(ConsoleText.ERRORTHEREISNOSETTLEMENT.toString());
            } else if (isStringUpperCase(corner)) {
                TextIOWrapper.printError(ConsoleText.ERRORTHEREISALREADYACITY.toString());
            } else {
                Config.Faction cornerFaction = SiedlerGame.getFactionFromName(corner);
                if (cornerFaction == playerFaction) {
                    buildValid = true;
                } else {
                    TextIOWrapper.printError(ConsoleText.ERRORSETTLEMENTISNOTYOURS.toString());
                }
            }
        } else {
            TextIOWrapper.printError(ConsoleText.ERRORNOTANEDGE.toString());
        }
        return buildValid;
    }

    /**
     * Checks whether a string is upper case
     *
     * @param string input to check
     * @return returns true or false
     */
    private boolean isStringUpperCase(String string) {
        char[] charArray = string.toCharArray();
        for (char c : charArray) {
            if (!Character.isUpperCase(c)) return false;
        }

        return true;
    }

    /**
     * Checks if it's possible to place the thief at a certain Point
     *
     * @param thiefField field to place the field
     * @return check result
     */
    boolean isValidPlacementPointForThief(Point thiefField) {
        boolean placementValid = false;
        if (hasField(thiefField)) {
            Land land = getField(thiefField);
            if (land.equals(Land.WATER)) {
                TextIOWrapper.printError(ConsoleText.ERRORCANTPLACETHIEFINWATER.toString());
            } else {
                placementValid = true;
            }
        } else {
            TextIOWrapper.printError(ConsoleText.ERRORNOTAFIELD.toString());
        }
        return placementValid;
    }

    /**
     * Checks if the target-coordinate is only surrounded by water
     *
     * @param corner coordinate to check
     * @return if coordinate is only surrounded by water
     */
    boolean isCornerSurroundedByWater(Point corner) {
        List<Land> adjacentFields = getFields(corner);
        for (Land field : adjacentFields) {
            if (!field.equals(Land.WATER)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if there is an adjacent road next to the target-coordinate which is build by the given player
     *
     * @param coordinate    target-coordinate
     * @param playerFaction faction of a player
     * @return check result
     */
    boolean hasAdjacentRoad(Point coordinate, Config.Faction playerFaction) {
        List<String> neighbourRoads = getAdjacentEdges(coordinate);
        for (String road : neighbourRoads) {
            if (road.equals(playerFaction.toString())) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method checks if in Phase II the target-point is the second settlement.
     * The first settlement need to have a road -> point is settlement but doesn't have a road yet
     *
     * @param roadStart roadstart-corner
     * @param roadEnd   roadend-corner
     * @return check boolean
     */
    boolean roadIsAdjacentToSecondSettlement(Point roadStart, Point roadEnd, Config.Faction playerFaction) {
        if ((getCorner(roadStart) != null && getCorner(roadStart).equals(playerFaction.toString())) || (getCorner(roadEnd) != null && getCorner(roadEnd).equals(playerFaction.toString()))) {
            List<String> adjacentStartPointRoads = getAdjacentEdges(roadStart);
            List<String> adjacentEndPointRoads = getAdjacentEdges(roadEnd);
            boolean ownRoadAdjacentToStart = false;
            boolean ownRoadAdjacentToEnd = false;
            for (String road : adjacentStartPointRoads) {
                if (road.equals(playerFaction.toString())) {
                    ownRoadAdjacentToStart = true;
                    break;
                }
            }
            if (!ownRoadAdjacentToStart) {
                for (String road : adjacentEndPointRoads) {
                    if (road.equals(playerFaction.toString())) {
                        ownRoadAdjacentToEnd = true;
                        break;
                    }
                }
            }
            if (!ownRoadAdjacentToStart && !ownRoadAdjacentToEnd) {
                return true;
            }
        }
        TextIOWrapper.printError(ConsoleText.ERRORNEXTTOSECONDSETTLEMENT.toString());
        return false;
    }

    /**
     * Checks if it's possible to build a Road between given coordinates
     * Checks: if place is empty,valid coordinates,  players settlement or road at start- or endpoint
     * and if its build over the water
     *
     * @param roadStart start-coordinate
     * @param roadEnd   end-coordinate
     * @param player    player
     * @return check result
     */
    boolean isValidBuildPointForRoad(Point roadStart, Point roadEnd, Player player, boolean initial) {
        if (hasEdge(roadStart, roadEnd)) {
            if (!isCornerSurroundedByWater(roadStart) && !isCornerSurroundedByWater(roadEnd)) {
                boolean isEdgeEmpty = getEdge(roadStart, roadEnd) == null || getEdge(roadStart, roadEnd).equals("");
                boolean ownBuildingAdjacent = (getCorner(roadStart) != null && getCorner(roadStart).equals(player.getFaction().toString().toLowerCase()))
                        || (getCorner(roadEnd) != null && getCorner(roadEnd).equals(player.getFaction().toString()));
                if (initial) {
                    if (player.getRoadCount() > 0) {
                        return roadIsAdjacentToSecondSettlement(roadStart, roadEnd, player.getFaction());
                    } else if (ownBuildingAdjacent) {
                        return true;
                    } else {
                        TextIOWrapper.printError(ConsoleText.ERRORNEXTTOFIRSTSETTLEMENT.toString());
                        return false;
                    }
                }
                List<String> neighboursEdgesOfStart = getAdjacentEdges(roadStart);
                List<String> neighboursEdgesOfEnd = getAdjacentEdges(roadEnd);
                boolean ownRoadAdjacentStart = false;
                boolean ownRoadAdjacentEnd = false;
                for (String possibleRoadSpot : neighboursEdgesOfStart) {
                    if (possibleRoadSpot != null && possibleRoadSpot.equals(player.getFaction().toString())) {
                        ownRoadAdjacentStart = true;
                        break;
                    }
                }
                if (!ownRoadAdjacentStart) {
                    for (String possibleRoadSpot : neighboursEdgesOfEnd) {
                        if (possibleRoadSpot != null && possibleRoadSpot.equals(player.getFaction().toString())) {
                            ownRoadAdjacentEnd = true;
                            break;
                        }
                    }
                }
                if (isEdgeEmpty && (ownBuildingAdjacent || ownRoadAdjacentStart || ownRoadAdjacentEnd)) {
                    return true;
                }
                TextIOWrapper.printError(ConsoleText.ERRORROADNEXTTOCONSTRUCTION.toString());
                return false;
            }
            TextIOWrapper.printError(ConsoleText.ERRORROADINWATER.toString());
            return false;
        }
        TextIOWrapper.printError(ConsoleText.ERRORNOTANEDGE.toString());
        return false;
    }
}
