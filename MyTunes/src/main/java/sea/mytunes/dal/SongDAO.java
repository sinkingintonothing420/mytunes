package sea.mytunes.dal;

import sea.mytunes.be.Song;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SongDAO {
    String sql = "SELECT * FROM Song";
    String add = "INSERT INTO Song (title, artist, category, time) VALUES (?,?,?,?)";
    String update = "UPDATE Song SET title = ?, artist = ?, category = ?, time = ? WHERE song_id = ?";
    String del = "DELETE FROM Song WHERE song_id = ?";


    public List<Song> getAllSongs() throws SQLException{
        DatabaseConnect db = new DatabaseConnect();
        List<Song> songs = new ArrayList<>();

        try(Connection con = db.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {
                while (rs.next()){
                    Song song = new Song(
                            rs.getString("title"),
                            rs.getString("artist"),
                            rs.getString("category"),
                            rs.getString("time"),
                            null); // need to fix

                        songs.add(song);

                }

            }
        return songs;
    }
        public void addSong(Song song) throws SQLException {
            DatabaseConnect db = new DatabaseConnect();

            try (Connection con = db.getConnection();
                 PreparedStatement stmt = con.prepareStatement(add)) {

                stmt.setString(1, song.getTitle());
                stmt.setString(2, song.getArtist());
                stmt.setString(3, song.getCategory());
                stmt.setString(4, song.getTime());
                stmt.executeUpdate();
            }
        }

            public void updateSong ( int songId, Song song) throws SQLException {
                DatabaseConnect db = new DatabaseConnect();

                try (Connection con = db.getConnection();
                     PreparedStatement stmt = con.prepareStatement(update)) {

                    stmt.setString(1, song.getTitle());
                    stmt.setString(2, song.getArtist());
                    stmt.setString(3, song.getCategory());
                    stmt.setString(4, song.getTime());
                    stmt.setInt(5, songId);
                    stmt.executeUpdate();

                }
            }

            public void deleteSong ( int songId) throws SQLException{
                DatabaseConnect db = new DatabaseConnect();

                try (Connection con = db.getConnection();
                     PreparedStatement stmt = con.prepareStatement(del)) {
                    stmt.setInt(1, songId);
                    stmt.executeUpdate();

                }


            }
        }