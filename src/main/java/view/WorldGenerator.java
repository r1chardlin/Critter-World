package view;

import cms.util.maybe.NoMaybeValue;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;
import model.ReadOnlyCritter;
import model.ReadOnlyWorld;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.Objects;

public class WorldGenerator
{
    private StackPane world;
    private ReadOnlyWorld readWorld;
    private Hashtable<String, Color> speciesColor = new Hashtable<>();
    private int numRows;
    private int numColumns;

    private final Image foodImage = new Image(Objects.requireNonNull(getClass().getResource("razzBerry.png")).toString(),
            23, 23, true, false);

    private final Image rockImage = new Image(Objects.requireNonNull(getClass().getResource("rock.png")).toString(),
            30, 30, true, false);
    private final double HEIGHT = 43.3013;

    private final double BOTTOM_LEFT_X_START = 17.5;
    private final double BOTTOM_LEFT_Y_START = 70.3013;
    private final double BOTTOM_RIGHT_X_START = 42.5;
    private final double BOTTOM_RIGHT_Y_START = 70.3013;
    private final double CENTER_RIGHT_X_START = 55;
    private final double CENTER_RIGHT_Y_START = 48.65065;
    private final double TOP_RIGHT_X_START = 42.5;
    private final double TOP_RIGHT_Y_START = 27;
    private final double TOP_LEFT_X_START = 17.5;
    private final double TOP_LEFT_Y_START = 27;
    private final double CENTER_LEFT_X_START = 5;
    private final double CENTER_LEFT_Y_START = 48.65065;

    private final double HEX_CENTER_X = (CENTER_LEFT_X_START + CENTER_RIGHT_X_START) / 2;
    private final double HEX_CENTER_Y = (CENTER_LEFT_Y_START + CENTER_RIGHT_Y_START) / 2;


    public ZoomPane loadWorld(int numRows, int numColumns, ReadOnlyWorld readWorld) throws NoMaybeValue
    {
        this.numRows = numRows;
        this.numColumns = numColumns;
        this.readWorld = readWorld;

        BorderPane hexes = getHexGroup();
        BorderPane activePane = getActivePane();

        world = new StackPane(hexes, activePane);

        return new ZoomPane(world, Math.max(this.numRows, this.numColumns));
    }

    public BorderPane getHexGroup()
    {
        BorderPane hexes = new BorderPane();

        double blX = BOTTOM_LEFT_X_START;
        double brX = BOTTOM_RIGHT_X_START;
        double crX = CENTER_RIGHT_X_START;
        double trX = TOP_RIGHT_X_START;
        double tlX = TOP_LEFT_X_START;
        double clX = CENTER_LEFT_X_START;

        for (int i = 0; i < numColumns; i++) {
            if (i > 0) {
                blX += 37.5;
                brX += 37.5;
                crX += 37.5;
                trX += 37.5;
                tlX += 37.5;
                clX += 37.5;
            }

            double blY;
            double brY;
            double crY;
            double trY;
            double tlY;
            double clY;

            if (numRows % 2 == 0)
            {
                blY = i % 2 == 0 ? BOTTOM_LEFT_Y_START : BOTTOM_LEFT_Y_START - HEIGHT / 2;
                brY = i % 2 == 0 ? BOTTOM_RIGHT_Y_START : BOTTOM_RIGHT_Y_START - HEIGHT / 2;
                crY = i % 2 == 0 ? CENTER_RIGHT_Y_START : CENTER_RIGHT_Y_START - HEIGHT / 2;
                trY = i % 2 == 0 ? TOP_RIGHT_Y_START : TOP_RIGHT_Y_START - HEIGHT / 2;
                tlY = i % 2 == 0 ? TOP_LEFT_Y_START : TOP_LEFT_Y_START - HEIGHT / 2;
                clY = i % 2 == 0 ? CENTER_LEFT_Y_START : CENTER_LEFT_Y_START - HEIGHT / 2;
            }
            else
            {
                blY = i % 2 == 0 ? BOTTOM_LEFT_Y_START : BOTTOM_LEFT_Y_START + HEIGHT / 2;
                brY = i % 2 == 0 ? BOTTOM_RIGHT_Y_START : BOTTOM_RIGHT_Y_START + HEIGHT / 2;
                crY = i % 2 == 0 ? CENTER_RIGHT_Y_START : CENTER_RIGHT_Y_START + HEIGHT / 2;
                trY = i % 2 == 0 ? TOP_RIGHT_Y_START : TOP_RIGHT_Y_START + HEIGHT / 2;
                tlY = i % 2 == 0 ? TOP_LEFT_Y_START : TOP_LEFT_Y_START + HEIGHT / 2;
                clY = i % 2 == 0 ? CENTER_LEFT_Y_START : CENTER_LEFT_Y_START + HEIGHT / 2;
            }

            int limit = i % 2 == 0 ? (int) Math.ceil(numRows / 2.0) : numRows / 2;

            for (int j = 0; j < limit; j++)
            {
                Polygon polygon = new Polygon(
                        blX, blY,              // bottom left
                        brX, brY,              // bottom right
                        crX, crY,              // center right
                        trX, trY,              // top right
                        tlX, tlY,              // top left
                        clX, clY);             // center left

                polygon.setStroke(Color.BLACK);
                polygon.setStrokeWidth(2);
                polygon.setFill(Color.TRANSPARENT);

                hexes.getChildren().add(polygon);

                blY += HEIGHT;
                brY += HEIGHT;
                crY += HEIGHT;
                trY += HEIGHT;
                tlY += HEIGHT;
                clY += HEIGHT;
            }
        }

        return hexes;
    }

    public BorderPane getActivePane() throws NoMaybeValue
    {
        BorderPane activePane = new BorderPane();

        double centerX = HEX_CENTER_X;

        for (int c = 0; c < numColumns; c++)
        {
            centerX = c > 0 ? centerX + 37.5 : centerX;
            double centerY;
            if (numRows % 2 == 0)
            {
                centerY = c % 2 == 0 ? HEX_CENTER_Y : HEX_CENTER_Y - HEIGHT / 2;
            }
            else
            {
                centerY = c % 2 == 0 ? HEX_CENTER_Y : HEX_CENTER_Y + HEIGHT / 2;
            }

            int start;
            if (numRows % 2 == 0)
            {
                start = c % 2 == 1 ? 0 : 1;
            }
            else
            {
                start = c % 2 == 0 ? 0 : 1;
            }
            for (int r = start; r < numRows; r += 2)
            {
                int info = readWorld.getTerrainInfo(c, numRows - 1 - r);
                // is critter
                if (info > 0)
                {
                    ReadOnlyCritter critter = readWorld.getReadOnlyCritter(c, numRows - 1 - r).get();
                    int dir = critter.getDirection();
                    String species = critter.getSpecies();
                    Color color = speciesColor.get(species);
                    if (color == null)
                    {
                        color = Color.color(Math.random(), Math.random(), Math.random());
                        speciesColor.put(species, color);
                    }

                    int size = critter.getMemory()[3];
                    size = Math.min(size, 5);
                    double bodyRadius = Math.sqrt(size * 40);
                    double headRadius = bodyRadius * (2.0 / 5.0);

                    Circle body = new Circle(bodyRadius, color);
                    body.setLayoutX(centerX);
                    body.setLayoutY(centerY + 3);

                    Circle head = new Circle(headRadius, Color.BLACK);
                    head.setLayoutX(centerX);
                    head.setLayoutY(centerY - bodyRadius - headRadius + 6);

                    Line leg1 = new Line();
                    leg1.setStrokeWidth(1.5);
                    double startX1 = Math.cos((3 * Math.PI) / 4) * bodyRadius;
                    double startY1 = Math.sin((3 * Math.PI) / 4) * bodyRadius;
                    leg1.setStartX(centerX + startX1);
                    leg1.setEndX(centerX + startX1 * 1.2);
                    leg1.setStartY(centerY + 3 - startY1);
                    leg1.setEndY(centerY + 3 - startY1 * 1.2);

                    Line leg2 = new Line();
                    leg2.setStrokeWidth(1.5);
                    leg2.setStartX(centerX - bodyRadius);
                    leg2.setEndX(centerX - bodyRadius * 1.2);
                    leg2.setStartY(centerY + 3);
                    leg2.setEndY(centerY + 3);

                    Line leg3 = new Line();
                    leg3.setStrokeWidth(1.5);
                    double startX3 = Math.cos((5 * Math.PI) / 4) * bodyRadius;
                    double startY3 = Math.sin((5 * Math.PI) / 4) * bodyRadius;
                    leg3.setStartX(centerX + startX3);
                    leg3.setEndX(centerX + startX3 * 1.2);
                    leg3.setStartY(centerY + 3 - startY3);
                    leg3.setEndY(centerY + 3 - startY3 * 1.2);

                    Line leg4 = new Line();
                    leg4.setStrokeWidth(1.5);
                    double startX4 = Math.cos(Math.PI / 4) * bodyRadius;
                    double startY4 = Math.sin(Math.PI / 4) * bodyRadius;
                    leg4.setStartX(centerX + startX4);
                    leg4.setEndX(centerX + startX4 * 1.2);
                    leg4.setStartY(centerY + 3 - startY4);
                    leg4.setEndY(centerY + 3 - startY4 * 1.2);

                    Line leg5 = new Line();
                    leg5.setStrokeWidth(1.5);
                    leg5.setStartX(centerX + bodyRadius);
                    leg5.setEndX(centerX + bodyRadius * 1.2);
                    leg5.setStartY(centerY + 3);
                    leg5.setEndY(centerY + 3);

                    Line leg6 = new Line();
                    leg6.setStrokeWidth(1.5);
                    double startX6 = Math.cos((7 * Math.PI) / 4) * bodyRadius;
                    double startY6 = Math.sin((7 * Math.PI) / 4) * bodyRadius;
                    leg6.setStartX(centerX + startX6);
                    leg6.setEndX(centerX + startX6 * 1.2);
                    leg6.setStartY(centerY + 3 - startY6);
                    leg6.setEndY(centerY + 3 - startY6 * 1.2);

                    Group critterGroup = new Group();
                    critterGroup.getChildren().addAll(head, body, leg1, leg2, leg3, leg4, leg5, leg6);


                    Rotate rotate = new Rotate();

                    rotate.setPivotX(centerX);
                    rotate.setPivotY(centerY);

                    if (dir == 1)
                    {
                        rotate.setAngle(60);
                    }
                    else if (dir == 2)
                    {
                        rotate.setAngle(120);
                    }
                    else if (dir == 3)
                    {
                        rotate.setAngle(180);
                    }
                    else if (dir == 4)
                    {
                        rotate.setAngle(240);
                    }
                    else if (dir == 5)
                    {
                        rotate.setAngle(300);
                    }
                    critterGroup.getTransforms().add(rotate);

                    critterGroup.setOnMouseClicked(e ->
                    {
                        int[] mem = critter.getMemory();
                        String speciesStr = "Species: " + species + "\n\n";
                        String columnStr = "Column: " + critter.getColumn() + "\n";
                        String rowStr = "Row: " + (readWorld.getNumRows() - 1 - critter.getRow()) + "\n\n";
                        String memStr = "Mem Array: " + Arrays.toString(critter.getMemory()) + "\n";
                        String memInfoStr = "memsize: " + mem[0] + "\ndefense: " + mem[1] + "\noffense: " +
                                mem[2] + "\nsize: " + mem[3] + "\nenergy: " + mem[4] + "\npass number: " +
                                mem[5] + "\nposture: " + mem[6] + "\n\n";
                        String programStr = "Program String: \n" + critter.getProgramString() + "\n\n";
                        String lastRuleStr;
                        try
                        {
                            lastRuleStr = "Last Executed Rule: \n" + critter.getLastRuleString().get();
                        }
                        catch (NoMaybeValue ex)
                        {
                            lastRuleStr = "";
                        }
                        Alert critterInfo = new Alert(Alert.AlertType.INFORMATION);
                        critterInfo.setContentText(speciesStr + columnStr + rowStr + memStr + memInfoStr + programStr + lastRuleStr);
                        critterInfo.show();
                    });

                    activePane.getChildren().add(critterGroup);
                }
                // is rock
                else if (info == -1)
                {
                    ImageView rock = new ImageView(rockImage);
                    double imageWidth = rockImage.getWidth();
                    double imageHeight = rockImage.getHeight();
                    rock.setLayoutX(centerX - imageWidth / 2);
                    rock.setLayoutY(centerY - imageHeight / 2);
                    activePane.getChildren().add(rock);
                }
                // is food
                else if (info < -1)
                {
                    ImageView food = new ImageView(foodImage);
                    double imageWidth = foodImage.getWidth();
                    double imageHeight = foodImage.getHeight();
                    food.setLayoutX(centerX - imageWidth / 2);
                    food.setLayoutY(centerY - imageHeight / 2);
                    activePane.getChildren().add(food);
                }
                centerY += HEIGHT;
            }
        }
        return activePane;
    }

    public BorderPane updateActivePane(ReadOnlyWorld newReadWorld) throws NoMaybeValue
    {
        readWorld = newReadWorld;
        BorderPane activePane = getActivePane();
        world.getChildren().remove(1);
        world.getChildren().add(activePane);
        return activePane;
    }
}
