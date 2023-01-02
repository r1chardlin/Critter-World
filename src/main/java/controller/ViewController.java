package controller;

import ast.Program;
import exceptions.SyntaxError;
import model.*;
import parse.Parser;
import parse.ParserFactory;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.File;
import java.io.Reader;
import java.io.FileReader;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ViewController implements Controller
{
    private ControlOnlyWorld controlWorld;
    private ReadOnlyWorld readOnlyWorld;
    private boolean enableManna;
    private boolean enableForcedMutation;

    @Override
    public int getNumRows()
    {
        return controlWorld.getNumRows();
    }

    @Override
    public int getNumColumns()
    {
        return controlWorld.getNumColumns();
    }

    @Override
    public int getSteps()
    {
        return readOnlyWorld.getSteps();
    }

    @Override
    public int getNumberOfAliveCritters()
    {
        return readOnlyWorld.getNumberOfAliveCritters();
    }

    @Override
    public void setManna(boolean enableManna)
    {
        this.enableManna = enableManna;
        controlWorld.setManna(enableManna);
    }

    @Override
    public void setForcedMutation(boolean enableForcedMutation)
    {
        this.enableForcedMutation = enableForcedMutation;
        controlWorld.setForcedMutation(enableForcedMutation);
    }

    @Override
    public ReadOnlyWorld getReadOnlyWorld()
    {
        return readOnlyWorld;
    }

    @Override
    public void newWorld()
    {
        World world = new World();
        controlWorld = world;
        readOnlyWorld = world;
    }

    @Override
    public boolean loadWorld(String filename, boolean enableManna, boolean enableForcedMutation)
    {
        this.enableManna = enableManna;
        this.enableForcedMutation = enableForcedMutation;

        File worldFile = new File(filename);
        try
        {
            Scanner sc = new Scanner(worldFile);
            sc.nextLine();
            sc.next();
            int width = sc.nextInt();
            int height = sc.nextInt();
            World world = new World(width, height, enableManna, enableForcedMutation);
            ControlOnlyWorld tempControlWorld = world;
            while (sc.hasNext())
            {
                String object = sc.next();
                if (object.startsWith("//"))
                {
                    sc.nextLine();
                }
                else if (object.equals("rock"))
                {
                    int column = sc.nextInt();
                    int row = sc.nextInt();
                    if ((column + row) % 2 == 0)
                    {
                        tempControlWorld.addRock(row, column);
                    }
                }
                else if (object.equals("food"))
                {
                    int column = sc.nextInt();
                    int row = sc.nextInt();
                    int amount = sc.nextInt();
                    if ((column + row) % 2 == 0)
                    {
                        tempControlWorld.addFood(row, column, amount);
                    }
                }
                else if (object.equals("critter"))
                {
                    String critterFile = sc.next();
                    int column = sc.nextInt();
                    int row = sc.nextInt();
                    int direction = sc.nextInt();
                    if ((row + column) % 2 == 0)
                    {
                        Object[] critterInfo = parseCritterFile(worldFile.getParent() + "\\" + critterFile);
                        if (critterInfo != null)
                        {
                            tempControlWorld.addCritter((String) critterInfo[0], (int[]) critterInfo[1], (Program) critterInfo[2],
                                    row, column, direction, true);
                        }
                    }
                }
            }
            controlWorld = world;
            readOnlyWorld = world;
            return true;
        }
        catch (FileNotFoundException | InputMismatchException e)
        {
//            System.out.println("please input a valid world file");
        }
        return false;
    }

    @Override
    public boolean addCritter(String filename, int row, int column, int dir)
    {
        Object[] critterInfo = parseCritterFile(filename);
        if (critterInfo != null)
        {
            return controlWorld.addCritter((String) critterInfo[0], (int[]) critterInfo[1], (Program) critterInfo[2],
                    row, column, dir, true);
        }
        return false;
    }

    @Override
    public boolean loadCritters(String filename, int n)
    {
        Object[] critterInfo = parseCritterFile(filename);
        if (critterInfo != null)
        {
            for (int i = 0; i < n; i++)
            {
                controlWorld.addCritter((String) critterInfo[0], (int[]) critterInfo[1], (Program) critterInfo[2]);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean advanceTime(int n)
    {
        if (controlWorld == null || n < 0)
        {
            return false;
        }
        for (int i = 0; i < n; i++)
        {
            controlWorld.advanceTimeStep();
            if(enableForcedMutation) controlWorld.forcedMutate();
            if(enableManna) controlWorld.addManna();
        }
        return true;
    }

    @Override
    public void printWorld(PrintStream out)
    {
        controlWorld.printWorld(out);
    }

    public Object[] parseCritterFile(String filename)
    {
        try
        {
            Reader r = new FileReader(filename);
            String species = "";
            Program ast;
            int[] mem = new int[Constants.MIN_MEMORY];
            int line = 1;
            String text = "";
            while (line <= 7)
            {
                text = "";
                int i;
                while ((char) (i = r.read()) != '\n')
                {
                    text += (char) i;
                }
                if (text.endsWith("\r"))
                {
                    text = text.substring(0, text.length() - 1);
                }
                if (text.startsWith("//") || text.equals(""))
                {
                    continue;
                }
                else if (text.startsWith("species: ") && line == 1)
                {
                    species = text.substring(9);
                }
                else if (text.startsWith("memsize: ") && line == 2)
                {
                    int memsize = Integer.parseInt(text.substring(9));
                    mem = memsize > Constants.MIN_MEMORY ? new int[memsize] : mem;
                    mem[0] = memsize;
                    mem[5] = 1;
                }
                else if (text.startsWith("defense: ") && line == 3)
                {
                    int num = Integer.parseInt(text.substring(9));
                    mem[1] = num;
                }
                else if (text.startsWith("offense: ") && line == 4)
                {
                    int num = Integer.parseInt(text.substring(9));
                    mem[2] = num;
                }
                else if (text.startsWith("size: ") && line == 5)
                {
                    int num = Integer.parseInt(text.substring(6));
                    mem[3] = num;
                }
                else if (text.startsWith("energy: ") && line == 6)
                {
                    int num = Integer.parseInt(text.substring(8));
                    mem[4] = num;
                }
                else if (text.startsWith("posture: ") && line == 7)
                {
                    int num = Integer.parseInt(text.substring(9));
                    mem[6] = num;
                }
                else
                {
                    throw new IOException();
                }
                line++;
            }
            Parser p = ParserFactory.getParser();
            ast = p.parse(r);
            return new Object[]{species, mem, ast};
        }
        catch (IOException e)
        {
//            System.out.println("please input a valid critter file");
        }
        catch (SyntaxError e)
        {
//            System.out.println("a valid program should not have syntax errors");
        }
        return null;
    }
}
