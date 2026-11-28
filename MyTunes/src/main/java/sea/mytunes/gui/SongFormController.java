package sea.mytunes.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.stage.FileChooser;
import sea.mytunes.be.Song;
import javafx.scene.media.MediaPlayer;

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
            fillAudioDetails(selectedFile);
        }
    }

    private void fillAudioDetails(File file) {
        String path = file.getAbsolutePath();

        try
        {
            Media media = new Media(file.toURI().toString());
            MediaPlayer reader = new MediaPlayer(media);

            // Release this temporary player if the file cannot be read.
            reader.setOnError(reader::dispose);

            // Wait until JavaFX has read the file details.
            reader.setOnReady(() -> {
                try
                {
                    // Ignore the result if the user has chosen a different file.
                    if (!path.equals(txtFile.getText()))
                    {
                        return;
                    }

                    String fileName = file.getName().replaceFirst("\\.[^.]+$", "");

                    txtTitle.setText(readTag(media, "title", fileName));
                    txtArtist.setText(readTag(media, "artist", txtArtist.getText()));
                    txtCategory.setText(readTag(media, "genre", txtCategory.getText()));

                    // Convert the duration to the format used by the form.
                    double duration = reader.getTotalDuration().toSeconds();
                    if (Double.isFinite(duration) && duration > 0)
                    {
                        long seconds = (long) duration;
                        txtTime.setText(String.format("%d:%02d", seconds / 60, seconds % 60));
                    }
                }
                finally
                {
                    reader.dispose();
                }
            });
        }
        catch (javafx.scene.media.MediaException e)
        {
            new Alert(Alert.AlertType.WARNING,
                    "Could not read the audio details. You can enter them manually.")
                    .showAndWait();
        }
    }

    private String readTag(Media media, String key, String fallback)
    {
        // Use the fallback when this information is missing from the file.
        Object value = media.getMetadata().get(key);
        if (value instanceof String text && !text.isBlank())
        {
            return text;
        }

        return fallback;
        
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
