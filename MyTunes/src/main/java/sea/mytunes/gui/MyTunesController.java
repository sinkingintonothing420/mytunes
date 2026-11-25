package sea.mytunes.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MyTunesController {

    @FXML
    private TableView<?> tableSongs;

    @FXML
    private TableColumn<?, ?> colTitle;

    @FXML
    private TableColumn<?, ?> colArtist;

    @FXML
    private TableColumn<?, ?> colCategory;

    @FXML
    private TableColumn<?, ?> colTime;

    @FXML
    private void initialize()
    {
        System.out.println(colTitle.getText());
    }

    /**
     * Opens the New Song form when the user clicks New in the All songs section.
     */

    @FXML
    private void clickNewSong()
    {
        try
        {
            // Find the FXML file created in Scene Builder.
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/sea/mytunes/SongForm.fxml")
            );

            // Load the form into a new window.
            Stage formStage = new Stage();
            formStage.setTitle("New Song");
            formStage.setScene(new Scene(loader.load()));

            // Use the song table to find the main MyTunes window.
            formStage.initOwner(tableSongs.getScene().getWindow());

            // Keep the main window inactive until the form is closed.
            formStage.initModality(Modality.WINDOW_MODAL);
            formStage.show();

        } catch(IOException e)
        {
            // Show an error to the user if the form cannot be loaded.
            new Alert(Alert.AlertType.ERROR, "Could not open the song form.").showAndWait();
        }

    }
}
