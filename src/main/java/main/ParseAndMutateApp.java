package main;

import ast.Mutation;
import ast.MutationFactory;
import ast.Node;
import ast.Program;
import exceptions.SyntaxError;
import parse.Parser;
import parse.ParserFactory;
import java.io.Reader;
import java.io.FileReader;
import java.io.FileNotFoundException;

public class ParseAndMutateApp
{
    public static void main(String[] args)
    {
        int n = 0;
        String file = null;
        try
        {
            if (args.length == 1)
            {
                file = args[0];
//                System.out.println(file);
            }
            else if (args.length == 3 && args[0].equals("--mutate"))
            {
                n = Integer.parseInt(args[1]);
                if (n < 0) throw new IllegalArgumentException();
                file = args[2];
            }
            else
            {
                throw new IllegalArgumentException();
            }
            try
            {
                Reader r = new FileReader(file);
                Parser parser = ParserFactory.getParser();
                Program prog = parser.parse(r);
                if (n > 0)
                {
                    Mutation mut;
                    for (int i = 0; i < n; i++)
                    {
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
                        Node target = prog.nodeAt((int) (Math.random() * prog.size()));
                        int count = 0;
                        while (!(mut.canApply(target)) && count < 50)
                        {
                            target = prog.nodeAt((int) (Math.random() * prog.size()));
                            count++;
                        }
                        mut.apply(prog, target);
                        System.out.println(mut.getClass());
                        System.out.println(target);
                    }
                }
                System.out.println(prog);
            }
            catch(SyntaxError e)
            {
                System.out.println("A valid program should not have syntax errors");
            }
            catch (FileNotFoundException e)
            {
                System.out.println("Please enter a valid file");
                System.out.println(e);
            }
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("Usage:\n  <input_file>\n  " +
                               "--mutate <n> <input_file>");
        }
    }
}
