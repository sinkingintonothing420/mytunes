package sea.mytunes.be;

public class Song
{
    private final String title;
    private final String artist;
    private final String category;
    private final String time;
    private final String filePath;

    // Store the values received from the song form.
    public Song(String title, String artist, String category,
                String time, String filePath)
    {
        this.title = title;
        this.artist = artist;
        this.category = category;
        this.time = time;
        this.filePath = filePath;
    }

    public String getTitle()
    {
        return title;
    }

    public String getArtist()
    {
        return artist;
    }

    public String getCategory()
    {
        return category;
    }

    public String getTime()
    {
        return time;
    }

    public String getFilePath() {
        return filePath;
    }
}