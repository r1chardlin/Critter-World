package controller;

import controller.Controller;
import controller.ControllerFactory;
import org.junit.jupiter.api.Test;

public class ControllerTest
{
    @Test
    public void testLoadWorld()
    {
        Controller controller = ControllerFactory.getConsoleController();
        controller.loadWorld("src\\test\\resources\\A5_A6_files\\test_world.txt", true, false);
        controller.printWorld(System.out);
    }

    @Test
    public void testNewWorld()
    {
        Controller controller = ControllerFactory.getConsoleController();
        controller.newWorld();
        controller.loadCritters("src\\test\\resources\\A5_A6_files\\test_critter.txt", 10);
        controller.printWorld(System.out);
    }

    @Test
    public void testAdvanceTimeStep()
    {
        Controller controller = ControllerFactory.getConsoleController();
        controller.loadWorld("src\\test\\resources\\A5_A6_files\\test_world.txt", true, false);
//        controller.printWorld(System.out);
//        System.out.println();
        controller.advanceTime(5);
        controller.printWorld(System.out);
    }

    @Test
    public void testSpiralCritter()
    {
        Controller controller = ControllerFactory.getConsoleController();
//        controller.newWorld();
//        controller.loadCritters("src\\test\\resources\\A5files\\spiral_critter.txt", 1);
        controller.loadWorld("src\\test\\resources\\A5_A6_files\\spiral_world.txt", false, false);
        controller.printWorld(System.out);
        System.out.println();
        controller.advanceTime(300);
        System.out.println();
        controller.printWorld(System.out);
    }

    @Test
    public void testEatCritter(){
        Controller controller = ControllerFactory.getConsoleController();
        controller.loadWorld("src\\test\\resources\\A5_A6_files\\eat_world.txt", true, false);
        controller.printWorld(System.out);
        System.out.println();
        controller.advanceTime(100);
        System.out.println();
        controller.printWorld(System.out);
    }

    @Test
    public void testBudCritter(){
        Controller controller = ControllerFactory.getConsoleController();
        controller.loadWorld("src\\test\\resources\\A5_A6_files\\bud_world.txt", false, false);
        controller.printWorld(System.out);
        System.out.println();
        controller.advanceTime(10);
        System.out.println();
        controller.printWorld(System.out);
    }

    @Test
    public void testRunCritter(){
        Controller controller = ControllerFactory.getConsoleController();
        controller.loadWorld("src\\test\\resources\\A5_A6_files\\big_world.txt", false, false);
        controller.printWorld(System.out);
        System.out.println();
        controller.advanceTime(10);
        System.out.println();
        controller.printWorld(System.out);
    }

    @Test
    public void testMateCritter(){
        Controller controller = ControllerFactory.getConsoleController();
        controller.loadWorld("src\\test\\resources\\A5_A6_files\\big_world.txt", false, false);
        controller.printWorld(System.out);
        System.out.println();
        controller.advanceTime(10);
        System.out.println();
        controller.printWorld(System.out);
    }

    @Test
    public void testViewWorld(){
        Controller controller = ControllerFactory.getConsoleController();
        controller.loadWorld("src\\test\\resources\\A5_A6_files\\view_world.txt", true, false);
        controller.printWorld(System.out);
        System.out.println();
        controller.advanceTime(20);
        System.out.println();
        controller.printWorld(System.out);
    }

    @Test
    public void testSmeller()
    {
        Controller controller = ControllerFactory.getConsoleController();
        controller.loadWorld("src\\test\\resources\\A5_A6_files\\smell_world.txt", false, false);
        controller.printWorld(System.out);
        System.out.println();
        controller.advanceTime(10);
        System.out.println();
        controller.printWorld(System.out);
    }
    @Test
    public void testBigRun()
    {
        Controller controller = ControllerFactory.getConsoleController();
        controller.newWorld();
        controller.loadCritters("src\\test\\resources\\A5_A6_files\\run_critter.txt", 200);
        controller.printWorld(System.out);
        System.out.println();
        controller.advanceTime(10);
        System.out.println();
        controller.printWorld(System.out);
    }
}
