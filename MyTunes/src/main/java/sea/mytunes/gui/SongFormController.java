package sea.mytunes.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;

public class SongFormController
{

    @FXML
    private void clickCancel(ActionEvent event)
    {
        Button cancelButton = (Button) event.getSource();

        // Hide the window that contains the Cancel button.
        cancelButton.getScene().getWindow().hide();
    }

    @FXML
    private TextField txtTitle;

    @FXML
    private TextField txtArtist;

    @FXML
    private TextField txtCategory;

    @FXML
    private TextField txtTime;

    @FXML
    private TextField txtFile;

    @FXML
    private void clickChooseFile()
    {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose a song file");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Audio files", "*.mp3", "*.wav"));

        // Open the file chooser from the song form window.
        File selectedFile = chooser.showOpenDialog(txtFile.getScene().getWindow());

        // Update the path only if the user chose a file.
        if (selectedFile != null)
        {
            txtFile.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    private void clickSave()
    {
        // Print the form values to check that they are read correctly.
        System.out.println("Title: " + txtTitle.getText());
        System.out.println("Artist: " + txtArtist.getText());
        System.out.println("Category: " + txtCategory.getText());
        System.out.println("Time: " + txtTime.getText());
        System.out.println("File: " + txtFile.getText());
    }
}
