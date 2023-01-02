package model;

import ast.Program;
import cms.util.maybe.Maybe;

public class Critter implements ReadOnlyCritter
{
    private String species;
    private Program ast;
    private int[] mem;
    private int row;
    private int column;
    private int direction;
    private String lastRuleString;
    private boolean mating;
    private boolean justCreated;

    public Critter(String species, Program ast, int[] mem, int row, int column, int direction)
    {
        this.species = species;
        this.ast = ast;
        this.mem = mem;
        this.row = row;
        this.column = column;
        this.direction = direction % 6 >= 0 ? direction % 6 : direction % 6 + 6; // idk if we should the mod 6 should be included
        this.justCreated = true;
    }

    public Program getProgram()
    {
        return ast;
    }

//    public int[] getMem()
//    {
//        return mem;
//    }

    public boolean isMating()
    {
        return mating;
    }

    public boolean isJustCreated() {
        return justCreated;
    }

    public void setJustCreated(boolean set)
    {
        justCreated = set;
    }

    public void setLastRuleString(String newRule)
    {
        lastRuleString = newRule;
    }


    public void setMating(boolean mating)
    {
        this.mating = mating;
    }

    public int getRow()
    {
        return row;
    }

    public int getColumn()
    {
        return column;
    }

    public void setPosition(int r, int c)
    {
        row = r;
        column = c;
    }

    public void setDirection(int newDir){
        direction = newDir;
    }

    public int energyCapacity(){
        return mem[3] *  Constants.ENERGY_PER_SIZE;
    }

    public int complexity(){
        return ast.getChildren().size() * Constants.RULE_COST + (mem[1] + mem[2]) * Constants.ABILITY_COST;
    }

    @Override
    public int getDirection()
    {
        return direction;
    }

    public int getMemValue(int index)
    {
        return index >= 0 && index < mem.length ? mem[index] : 0;
    }

    public void setMem(int index, int newNum)
    {
        if ((index >= 3 && index <= 5 && newNum >= 1) || (index == 6 && newNum >= 0 && newNum <= 99) || (index >= 7 && index < mem.length))
        {
            mem[index] = newNum;
        }
    }

    @Override
    public String getSpecies()
    {
        return species;
    }

    @Override
    public int[] getMemory()
    {
        return mem.clone();
    }

    @Override
    public String getProgramString()
    {
        return ast.toString();
    }

    @Override
    public Maybe<String> getLastRuleString()
    {
        return lastRuleString != null ? Maybe.from(lastRuleString) : Maybe.none();
    }

    @Override
    public String toString()
    {
        String ret = "";
        for(int i = 0; i<mem.length; i++)
        {
            ret += (mem[i] + ", ");
        }
        ret += (row + ", " + column + ", " + direction);
        return ret;

    }
}
