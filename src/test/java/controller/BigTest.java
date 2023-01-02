package controller;

import org.junit.jupiter.api.Test;

public class BigTest
{
    @Test
    public void testBig()
    {
        Controller controller = ControllerFactory.getConsoleController();
        controller.loadWorld("src\\test\\resources\\A5_A6_files\\big_world.txt", true, false);
        controller.printWorld(System.out);
        System.out.println();
        controller.advanceTime(100);
        System.out.println();
        controller.printWorld(System.out);
    }
}
