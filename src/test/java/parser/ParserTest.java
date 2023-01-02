package parser;

import ast.*;
import cms.util.maybe.Maybe;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import exceptions.SyntaxError;
import org.junit.jupiter.api.Test;
import parse.Parser;
import parse.ParserFactory;

import static org.junit.jupiter.api.Assertions.*;

/** This class contains tests for the Critter parser. */
public class ParserTest
{
    /** Checks that a valid critter program is not {@code null} when parsed. */
    @Test
    public void testProgramIsNotNone()
    {
        InputStream in = ClassLoader.getSystemResourceAsStream("files/draw_critter_5.txt");
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();

        try
        {
            Program prog = parser.parse(r);
            System.out.println(prog);
        }
        catch(SyntaxError e)
        {
            System.out.println(e);
            fail("A valid program should not have syntax errors");
        }
    }

    @Test
    public void testRemove()
    {
        Mutation mut = MutationFactory.getRemove();
        InputStream in = ClassLoader.getSystemResourceAsStream("files/draw_critter_2.txt");
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        try
        {
            Program prog = parser.parse(r);
            Node target = prog.nodeAt((int) (Math.random() * prog.size()));
            System.out.println();
            System.out.println("--TEST REMOVE--");
            System.out.println();
            System.out.println(target.toString());
            System.out.println(target.getClass());
            System.out.println();
            System.out.println(mut.canApply(target));
            System.out.println();
            System.out.println("PARENT: " + ((AbstractNode) target).getParent());
            System.out.println(((AbstractNode) target).getParent().getClass());
            System.out.println();
            mut.apply(prog, target);
            System.out.println(prog);
        }
        catch(SyntaxError e)
        {
            fail("A valid program should not have syntax errors");
        }
    }

    @Test
    public void testSwap()
    {
        Mutation mut = MutationFactory.getSwap();
        InputStream in = ClassLoader.getSystemResourceAsStream("files/draw_critter_2.txt");
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        try
        {
            Program prog = parser.parse(r);
            Node target = prog.nodeAt((int) (Math.random() * prog.size()));
            System.out.println();
            System.out.println("--TEST SWAP--");
            System.out.println();
            System.out.println(target.toString());
            System.out.println(target.getClass());
            System.out.println();
            System.out.println(mut.canApply(target));
            System.out.println();
            System.out.println("CHILDREN: " + target.getChildren());
            System.out.println();
            mut.apply(prog, target);
            System.out.println(prog);
        }
        catch(SyntaxError e)
        {
            fail("A valid program should not have syntax errors");
        }
    }

    @Test
    public void testReplace()
    {
        Mutation mut = MutationFactory.getReplace();
        InputStream in = ClassLoader.getSystemResourceAsStream("files/draw_critter_2.txt");
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        try
        {
            Program prog = parser.parse(r);
            Node target = prog.nodeAt((int) (Math.random() * prog.size()));
            System.out.println();
            System.out.println("--TEST REPLACE--");
            System.out.println();
            System.out.println(target.toString());
            System.out.println(target.getClass());
            System.out.println();
            System.out.println(mut.canApply(target));
            System.out.println();
            System.out.println("PARENT: " + ((AbstractNode) target).getParent());
            System.out.println(((AbstractNode) target).getParent().getClass());
            System.out.println();
            mut.apply(prog, target);
            System.out.println(prog);
        }
        catch(SyntaxError e)
        {
            fail("A valid program should not have syntax errors");
        }
    }

    @Test
    public void testTransform()
    {
        Mutation mut = MutationFactory.getTransform();
        InputStream in = ClassLoader.getSystemResourceAsStream("files/draw_critter_2.txt");
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        try
        {
            Program prog = parser.parse(r);
            Node target = prog.nodeAt((int) (Math.random() * prog.size()));
            System.out.println();
            System.out.println("--TEST TRANSFORM--");
            System.out.println();
            System.out.println(target.toString());
            System.out.println(target.getClass());
            System.out.println();
            System.out.println(mut.canApply(target));
            System.out.println();
            System.out.println(((AbstractNode) target).getParent());
            System.out.println(((AbstractNode) target).getParent().getClass());
            System.out.println();
            mut.apply(prog, target);
            System.out.println(prog);
        }
        catch(SyntaxError e)
        {
            fail("A valid program should not have syntax errors");
        }
    }

    @Test
    public void testInsert()
    {
        Mutation mut = MutationFactory.getInsert();
        InputStream in = ClassLoader.getSystemResourceAsStream("files/draw_critter_2.txt");
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        try
        {
            Program prog = parser.parse(r);
            Node target = prog.nodeAt((int) (Math.random() * prog.size()));
            System.out.println();
            System.out.println("--TEST INSERT--");
            System.out.println();
            System.out.println(target.toString());
            System.out.println(target.getClass());
            System.out.println();
            System.out.println(mut.canApply(target));
            System.out.println();
            System.out.println(((AbstractNode) target).getParent());
            System.out.println(((AbstractNode) target).getParent().getClass());
            System.out.println();
            mut.apply(prog, target);
            System.out.println(prog);
        }
        catch(SyntaxError e)
        {
            fail("A valid program should not have syntax errors");
        }
    }

    @Test
    public void testDuplicate()
    {
        Mutation mut = MutationFactory.getDuplicate();
        InputStream in = ClassLoader.getSystemResourceAsStream("files/draw_critter_2.txt");
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        try
        {
            Program prog = parser.parse(r);
            Node target = prog.nodeAt((int) (Math.random() * prog.size()));
            while (!(mut.canApply(target)))
            {
                target = prog.nodeAt((int) (Math.random() * prog.size()));
            }
//            Node target = prog.nodeAt(8);
            System.out.println();
            System.out.println("--TEST DUPLICATE--");
            System.out.println();
            System.out.println(target.toString());
            System.out.println(target.getClass());
            System.out.println();
            System.out.println(mut.canApply(target));
            System.out.println();
            if (!(target instanceof Program))
            {
                System.out.println("PARENT: " + ((AbstractNode) target).getParent());
                System.out.println(((AbstractNode) target).getParent().getClass());
            }
            System.out.println();
            mut.apply(prog, target);
            System.out.println(prog);
        }
        catch(SyntaxError e)
        {
            fail("A valid program should not have syntax errors");
        }
    }

    @Test
    public void testClassInv()
    {
        Mutation mut = MutationFactory.getDuplicate();
        InputStream in = ClassLoader.getSystemResourceAsStream("files/draw_critter_2.txt");
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        try
        {
            Program prog = parser.parse(r);
            assertEquals(true, prog.classInv());
        }
        catch(SyntaxError e)
        {
            fail("A valid program should not have syntax errors");
        }
    }

    @Test
    public void testBigRandom() throws SyntaxError
    {
        InputStream in = ClassLoader.getSystemResourceAsStream("files/draw_critter_2.txt");
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program prog = parser.parse(r);
//        Mutation mut = MutationFactory.getSwap();
        Node target = prog.nodeAt((int) (Math.random() * prog.size()));
        for (int i = 0; i < 1000; i++)
        {
            target = prog.nodeAt((int) (Math.random() * prog.size()));
            System.out.println(i);
            Mutation mut;
            int mutationPicker = (int) (Math.random() * 6);
            if (mutationPicker == 0)
            {
                mut = MutationFactory.getRemove();
            }
            else if (mutationPicker == 1)
            {
                mut = MutationFactory.getSwap();
            }
            else if (mutationPicker == 2)
            {
                mut = MutationFactory.getReplace();
            }
            else if (mutationPicker == 3)
            {
                mut = MutationFactory.getTransform();
            }
            else if (mutationPicker == 4)
            {
                mut = MutationFactory.getInsert();
            }
            else
            {
                mut = MutationFactory.getDuplicate();
            }

            int count = 0;
            while (!(mut.canApply(target)) && count < 50)
            {
                target = prog.nodeAt((int) (Math.random() * prog.size()));
                count++;
            }
            try
            {
                mut.apply(prog, target);
//                System.out.println(mut.canApply(target));
            }
            catch (IndexOutOfBoundsException | StackOverflowError | NullPointerException e)
            {
                System.out.println(e);
                System.out.println();
                System.out.println(prog);
                System.out.println();
                System.out.println(mut.getClass());
                System.out.println();
                System.out.println(target);
                System.out.println();
                throw e;
            }
        }
        System.out.println();
        System.out.println(prog);
        System.out.println();
        System.out.println(target);
        System.out.println();
        System.out.println(target.getClass());
    }
}

