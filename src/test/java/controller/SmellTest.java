package controller;

import org.junit.jupiter.api.Test;

public class SmellTest
{
    @Test
    public void testSmell()
    {
        Controller controller = ControllerFactory.getConsoleController();
        controller.loadWorld("src\\test\\resources\\A5_A6_files\\smell_world.txt", false, false);
        controller.printWorld(System.out);
        System.out.println();
        controller.advanceTime(10);
        System.out.println();
        controller.printWorld(System.out);
    }
}
