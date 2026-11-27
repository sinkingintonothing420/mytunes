package sea.mytunes.bll;

import sea.mytunes.be.Song;
import sea.mytunes.dal.SongDAO;

import java.util.List;

public class SongManager {

    private SongDAO songDAO;

    public List<Song> getAllSongs() throws Exception {
        return songDAO.getAllSongs();
    }

    //added a trim so the user cannot just add empty spaces as a title etc.
    public void addSong(String title, String artist, String category, String time, String filePath) throws Exception {
        if (title == null || title.trim().isEmpty()) {
            throw new Exception("Song title cannot be empty");
        }

        if (artist == null || artist.trim().isEmpty()) {
            throw new Exception("Artist cannot be empty");
        }

        if(category == null || category.trim().isEmpty()){
            throw new Exception("Category cannot be empty");
        }

        if (filePath == null || filePath.trim().isEmpty()) {
            throw new Exception("Select a file");
        }

        if (time == null) {
            throw new Exception("Song must be longer than 0 seconds");
        }


        Song newSong = new Song(title.trim(), artist.trim(), category.trim(), time.trim(), filePath);
        songDAO.addSong(newSong);
    }

    public void editSong(Song song, String title, String artist, String category, String time, String filePath) throws Exception {
        song.setTitle(title.trim());
        song.setArtist(artist.trim());
        song.setCategory(category.trim());
        song.setTime(time);
        song.setFilePath(filePath.trim());

        songDAO.updateSong(song.getId(), song);
    }

    public void removeSongFromPlaylist(Playlist playlist, Song song) throws Exception {
        //Remove from the playlist
        playlist.getSongs().remove(song);

        // DAL removes connection between playlist and specific song
        playlistDAO.removeSongFromPlaylist(playlist.getId(), song.getId());
    }
}


