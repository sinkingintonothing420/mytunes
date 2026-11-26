package sea.mytunes.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sea.mytunes.be.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class MyTunesController
{

    @FXML private TableView<Song> tableSongs;
    @FXML private TextField txtSearch;
    @FXML private Button btnFilter;
    @FXML private TableColumn<Song, String> colTitle;
    @FXML private TableColumn<Song, String> colArtist;
    @FXML private TableColumn<Song, String> colCategory;
    @FXML private TableColumn<Song, String> colTime;

    // Keep every song here, even when the filter hides some rows.
    private final ArrayList<Song> allSongs = new ArrayList<>();

    // An empty search means the table shows all songs.
    private String activeSearch = "";

    @FXML
    private void initialize()
    {
        // Tell each column which song value to display.
        colTitle.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getTitle()));
        colArtist.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getArtist()));
        colCategory.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getCategory()));
        colTime.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getTime()));

        // Compare durations by their total number of seconds.
        colTime.setComparator((firstTime, secondTime) ->
                Long.compare(toSeconds(firstTime), toSeconds(secondTime)));
    }

    private long toSeconds(String time) {
        // Split a duration like "2:30" into minutes and seconds.
        String[] parts = time.split(":");
        long minutes = Long.parseLong(parts[0]);
        long seconds = Long.parseLong(parts[1]);

        return minutes * 60 + seconds;
    }

    @FXML
    private void clickFilter()
    {
        if (activeSearch.isEmpty())
        {
            String search = txtSearch.getText().trim().toLowerCase(Locale.ROOT);
            if (search.isEmpty())
            {
                return;
            }

            activeSearch = search;
            btnFilter.setText("Clear");
        }
        else
        {
            activeSearch = "";
            txtSearch.clear();
            btnFilter.setText("Filter");
        }

        refreshSongTable();
    }

    private void refreshSongTable()
    {
        // Rebuild the visible rows from the full list.
        tableSongs.getItems().clear();

        for (Song song : allSongs)
        {
            String title = song.getTitle().toLowerCase(Locale.ROOT);
            String artist = song.getArtist().toLowerCase(Locale.ROOT);

            if (activeSearch.isEmpty()
                    || title.contains(activeSearch)
                    || artist.contains(activeSearch))
            {
                tableSongs.getItems().add(song);
            }
        }

        // Keep the sort chosen through the table headers.
        tableSongs.sort();
    }

    @FXML
    private void clickNewSong()
    {
        // A new song has no existing song to edit.
        openSongForm(null);
    }

    @FXML
    private void clickEditSong()
    {
        Song selectedSong = tableSongs.getSelectionModel().getSelectedItem();

        if (selectedSong == null)
        {
            new Alert(Alert.AlertType.WARNING, "Please select a song to edit.")
                    .showAndWait();
            return;
        }

        openSongForm(selectedSong);
    }

    private void openSongForm(Song originalSong)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/sea/mytunes/SongForm.fxml"));

            Stage formStage = new Stage();
            formStage.setScene(new Scene(loader.load()));
            formStage.initOwner(tableSongs.getScene().getWindow());
            formStage.initModality(Modality.WINDOW_MODAL);

            SongFormController formController = loader.getController();

            if (originalSong == null)
            {
                formStage.setTitle("New Song");
            }
            else
            {
                formStage.setTitle("Edit Song");
                formController.fillForm(originalSong);
            }

            // Wait until the user saves or closes the form.
            formStage.showAndWait();

            Song savedSong = formController.getSavedSong();
            if (savedSong == null)
            {
                return;
            }

            // Update the full list before refreshing the visible rows.
            if (originalSong == null)
            {
                allSongs.add(savedSong);
            }
            else
            {
                int originalIndex = allSongs.indexOf(originalSong);
                allSongs.set(originalIndex, savedSong);
            }

            refreshSongTable();
        }
        catch (IOException e)
        {
            new Alert(Alert.AlertType.ERROR, "Could not open the song form.")
                    .showAndWait();
        }
    }

    @FXML
    private void clickDeleteSong()
    {
        Song selectedSong = tableSongs.getSelectionModel().getSelectedItem();

        if (selectedSong == null)
        {
            new Alert(Alert.AlertType.WARNING, "Please select a song to delete.")
                    .showAndWait();
            return;
        }

        Alert confirmation = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Delete \"" + selectedSong.getTitle() + "\"?",
                ButtonType.OK,
                ButtonType.CANCEL
        );
        confirmation.setTitle("Delete Song");
        confirmation.setHeaderText(null);
        confirmation.initOwner(tableSongs.getScene().getWindow());

        // Remove the song only after the user confirms.
        if (confirmation.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK)
        {
            allSongs.remove(selectedSong);
            refreshSongTable();
        }
    }
}