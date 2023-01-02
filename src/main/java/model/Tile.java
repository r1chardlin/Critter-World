package model;

public class Tile {

    private boolean isRock;
    private boolean isFood;
    private boolean isCritter;

    private int numFood = 0;
    private Critter critter;

    Tile()
    {
        isRock = true;
        this.isFood = false;
        this.isCritter = false;
    }

    Tile(int numFood)
    {
        this.isFood = true;
        this.isRock = false;
        this.isCritter = false;
        this.numFood = numFood;
    }

    Tile(Critter critter)
    {
        this.isCritter = true;
        this.isFood = false;
        this.isRock = false;
        this.critter = critter;
    }

    Tile(Boolean bool)
    {
        this.isCritter = false;
        this.isFood = false;
        this.isRock = false;
    }

    public boolean getIsRock()
    {
        return isRock;
    }

    public boolean getIsFood()
    {
        return numFood > 0 ? true : false;
    }

    public boolean getIsCritter()
    {
        return isCritter;
    }

    public int getNumFood()
    {
        return numFood;
    }

    public Critter getCritter()
    {
        return critter;
    }


}
