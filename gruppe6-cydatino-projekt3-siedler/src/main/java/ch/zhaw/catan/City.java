package ch.zhaw.catan;

/**
 * This class represents the building-type city
 *
 * @author baumgnoa, bergecyr, brundar, sigritim
 * @version 10.12.2021
 */
public class City extends Building {

    /**
     * Default constructor of city
     *
     * @param structure type of structure
     * @param name      name of player
     */
    public City(Config.Structure structure, String name) {
        this.setStructure(structure);
        this.setName(name);
    }

    /**
     * Method to get name from player
     *
     * @return name of player
     */
    @Override
    public String getName() {
        return super.getName().toUpperCase();
    }
}
