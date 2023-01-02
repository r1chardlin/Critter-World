package view;

import controller.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TileSelecter implements Initializable
{
    private Stage stage = new Stage();
    private Controller controller;
    private File critterFile;
    private Main main;

    @FXML
    Button critterOK;
    @FXML
    Button critterCancel;
    @FXML
    TextField colText;
    @FXML
    TextField rowText;
    @FXML
    TextField dirText;

    public TileSelecter(Controller controller, File critterFile, Main main) throws IOException
    {
        this.controller = controller;
        this.critterFile = critterFile;
        this.main = main;
    }

    public TileSelecter(Main main, Controller controller)
    {
        this.main = main;
        this.controller = controller;
    }

    public void setCritterFile(File critterFile)
    {
        this.critterFile = critterFile;
    }

    public void start() throws IOException
    {
        URL r = getClass().getResource("addCritter.fxml");
        FXMLLoader loader = new FXMLLoader(r);
        loader.setController(this);
        VBox parent = loader.load();
        Scene scene = new Scene(parent, 220, 226);
        stage.setTitle("Tile Selecter");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        critterOK.setOnAction(event ->
        {
            int row;
            int column;
            int dir;
            try
            {
                row = Integer.parseInt(rowText.getText());
                column = Integer.parseInt(colText.getText());
                dir = Integer.parseInt(dirText.getText());
                if (row < 0 || row > controller.getNumRows() || column < 0 || column > controller.getNumColumns()
                        || (row + column) % 2 != 0) throw new NumberFormatException();
                controller.addCritter(critterFile.getAbsolutePath(), row, column, dir);
            }
            catch (NumberFormatException exception)
            {
                controller.loadCritters(critterFile.getAbsolutePath(), 1);
            }
            main.updateActivePaneGUI();
            stage.hide();
        });

        critterCancel.setOnAction(event ->
        {
            stage.hide();
        });
    }
}
