package model;

import ast.Program;

import java.io.PrintStream;

public abstract class ControlOnlyWorld
{
    public abstract int getNumRows();

    public abstract int getNumColumns();

    public abstract Tile[][] tileView();

    public abstract boolean addCritter(String species, int[] mem, Program ast);

    public abstract boolean addCritter(String species, int[] mem, Program ast, int row, int column, int dir);

    public abstract boolean addCritter(String species, int[] mem, Program ast, int row, int column, int dir, boolean loaded);

    public abstract boolean addRock(int row, int column);

    public abstract boolean addFood(int row, int column, int amount);

    public abstract void advanceTimeStep();

    public abstract void printWorld(PrintStream out);

    public abstract void forcedMutate();

    public abstract void addManna();

    public abstract void setManna(boolean enableManna);
    public abstract void setForcedMutation(boolean enableForcedMutation);
}
