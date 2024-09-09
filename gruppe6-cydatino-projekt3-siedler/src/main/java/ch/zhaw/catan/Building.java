package ch.zhaw.catan;

/**
 * This abstract class represents all type of buildings
 *
 * @author baumgnoa, bergecyr, brundar, sigritim
 * @version 10.12.2021
 */
public abstract class Building {

    private Config.Structure structure;
    private String name;

    /**
     * Method to get structures
     *
     * @return structure
     */
    public Config.Structure getStructure() {
        return structure;
    }

    /**
     * Method to set structures
     *
     * @param structure type of building
     */
    public void setStructure(Config.Structure structure) {
        this.structure = structure;
    }

    /**
     * Method to get name from player
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Method to set name from player
     *
     * @param name name of player
     */
    public void setName(String name) {
        this.name = name;
    }

}
