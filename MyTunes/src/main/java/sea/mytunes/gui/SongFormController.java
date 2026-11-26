package sea.mytunes.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import sea.mytunes.be.Song;

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

    private Song savedSong;

    public Song getSavedSong()
    {
        return savedSong;
    }

    // Fill the form with the values of the selected song.
    public void fillForm(Song song)
    {
        txtTitle.setText(song.getTitle());
        txtArtist.setText(song.getArtist());
        txtCategory.setText(song.getCategory());
        txtTime.setText(song.getTime());
        txtFile.setText(song.getFilePath());
    }

    @FXML
    private void clickSave()
    {

        // Check the form before creating the song.
        if (txtTitle.getText().isBlank()
                || txtArtist.getText().isBlank()
                || txtCategory.getText().isBlank()
                || txtFile.getText().isBlank())
        {
            new Alert(Alert.AlertType.WARNING,
                    "Please enter a title, artist and category, and choose an audio file.")
                    .showAndWait();
            return;
        }

        if (!txtTime.getText().trim().matches("[0-9]+:[0-5][0-9]"))
        {
            new Alert(Alert.AlertType.WARNING,
                    "Enter the duration as minutes:seconds, for example 3:15.")
                    .showAndWait();
            return;
        }

        // Create a song from the values in the form.
        savedSong = new Song(
                txtTitle.getText(),
                txtArtist.getText(),
                txtCategory.getText(),
                txtTime.getText(),
                txtFile.getText()
        );

        // Close the form after creating the song.
        txtTitle.getScene().getWindow().hide();
    }
}
