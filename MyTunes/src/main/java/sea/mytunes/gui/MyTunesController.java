package sea.mytunes.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sea.mytunes.be.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    @FXML private TableView<String> tablePlaylists;
    @FXML private TableColumn<String, String> colPlaylistName;
    @FXML private TableColumn<String, String> colPlaylistSongs;
    @FXML private TableColumn<String, String> colPlaylistTime;
    // This list will display the titles in the selected playlist.
    @FXML private ListView<String> listPlaylistSongs;

    // Keep every song here, even when the filter hides some rows.
    private final ArrayList<Song> allSongs = new ArrayList<>();

    // Keep the songs added to the test playlist.
    private final List<Song> testPlaylistSongs = new ArrayList<>();


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

        // Show the name stored in each row.
        colPlaylistName.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue()));

        // Display the song count and total duration in the playlist table.
        colPlaylistSongs.setCellValueFactory(cell ->
                new SimpleStringProperty(String.valueOf(testPlaylistSongs.size())));

        colPlaylistTime.setCellValueFactory(cell ->
                new SimpleStringProperty(getPlaylistTime(testPlaylistSongs)));
    }

    private long toSeconds(String time) {
        // Split a duration like "2:30" into minutes and seconds.
        String[] parts = time.split(":");
        long minutes = Long.parseLong(parts[0]);
        long seconds = Long.parseLong(parts[1]);

        return minutes * 60 + seconds;
    }

    private String getPlaylistTime(List<Song> songs) //for time playlist collumn
    {
        long totalSeconds = 0;

        // Add the duration of each song in seconds.
        for (Song song : songs)
        {
            totalSeconds += toSeconds(song.getTime());
        }

        // Convert the total back to minutes and seconds.
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        return String.format(Locale.ROOT, "%d:%02d", minutes, seconds);
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
    private void clickNewPlaylist()
    {
        // A new playlist has no row to edit.
        openPlaylistForm(-1);
    }

    @FXML
    private void clickEditPlaylist()
    {
        int selectedIndex = tablePlaylists.getSelectionModel().getSelectedIndex();

        // JavaFX returns -1 when no row is selected.
        if (selectedIndex == -1)
        {
            new Alert(Alert.AlertType.WARNING, "Please select a playlist to edit.")
                    .showAndWait();
            return;
        }

        openPlaylistForm(selectedIndex);
    }

    @FXML
    private void clickDeletePlaylist()
    {
        int selectedIndex = tablePlaylists.getSelectionModel().getSelectedIndex();

        // JavaFX returns -1 when no row is selected.
        if (selectedIndex == -1)
        {
            new Alert(Alert.AlertType.WARNING, "Please select a playlist to delete.")
                    .showAndWait();
            return;
        }

        String playlistName = tablePlaylists.getItems().get(selectedIndex);

        Alert confirmation = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Delete \"" + playlistName + "\"?",
                ButtonType.OK,
                ButtonType.CANCEL);
        confirmation.setTitle("Delete Playlist");
        confirmation.setHeaderText(null);
        confirmation.initOwner(tablePlaylists.getScene().getWindow());

        // Remove the selected row only after the user confirms.
        if (confirmation.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK)
        {
            tablePlaylists.getItems().remove(selectedIndex);
        }
    }

    private void openPlaylistForm(int selectedIndex)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/sea/mytunes/PlaylistForm.fxml"));

            Stage window = new Stage();
            window.setScene(new Scene(loader.load()));
            window.initOwner(tablePlaylists.getScene().getWindow());
            window.initModality(Modality.WINDOW_MODAL);

            PlaylistFormController formController = loader.getController();

            // When editing, fill in the current name.
            if (selectedIndex >= 0)
            {
                window.setTitle("Edit Playlist");
                formController.fillForm(tablePlaylists.getItems().get(selectedIndex));
            }
            else
            {
                window.setTitle("New Playlist");
            }

            // Wait for the form to close before reading the name.
            window.showAndWait();

            String playlistName = formController.getSavedName();
            if (playlistName == null)
            {
                return;
            }

            if (selectedIndex >= 0)
            {
                // Replace the name in the selected row.
                tablePlaylists.getItems().set(selectedIndex, playlistName);
            }
            else
            {
                // Add a row for the new playlist.
                tablePlaylists.getItems().add(playlistName);
            }
        }
        catch (IOException e)
        {
            new Alert(Alert.AlertType.ERROR, "Could not open the playlist form.")
                    .showAndWait();
        }
    }

    @FXML
    private void clickAddSongToPlaylist()
    {
        // Read the selected playlist name from the left table.
        String playlistName = tablePlaylists.getSelectionModel().getSelectedItem();

        // Read the selected song from the right table.
        Song song = tableSongs.getSelectionModel().getSelectedItem();

        if (playlistName == null || song == null)
        {
            new Alert(Alert.AlertType.WARNING, "Select a playlist and a song.")
                    .showAndWait();
            return;
        }

        // Check that the button receives both selections.
        //System.out.println("Playlist: " + playlistName + ", song: " + song.getTitle()); //temporary

        // Add the selected song and update the middle list.
        testPlaylistSongs.add(song);
        showPlaylistSongs(testPlaylistSongs);

        // Recalculate the values shown in Songs and Time.
        tablePlaylists.refresh();
    }

    private void showPlaylistSongs(List<Song> songs)
    {
        // Clear the titles currently shown in the middle list.
        listPlaylistSongs.getItems().clear();

        // Show the title of each song in the playlist.
        for (Song song : songs)
        {
            listPlaylistSongs.getItems().add(song.getTitle());
        }
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