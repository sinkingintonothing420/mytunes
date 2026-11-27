package sea.mytunes.bll;

import sea.mytunes.be.Song;

public class PlaylistManager {

    // user is deleting the song from the app
    public void deleteSongFromApplication(Song song) throws Exception {
        // removes song from all playlists
        playlistDAO.removeSongFromAllPlaylists(song.getId());

        //deletes song from database
        songDAO.deleteSong(song.getId());
    }
}
