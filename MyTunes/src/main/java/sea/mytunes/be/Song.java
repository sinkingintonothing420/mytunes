package sea.mytunes.be;

public class Song
{
    private int id;
    private String title;
    private String artist;
    private String category;
    private String time;
    private String filePath;

    // Store the values received from the song form (adding new song to db)
    public Song(String title, String artist, String category, String time, String filePath)
    {
        this.title = title;
        this.artist = artist;
        this.category = category;
        this.time = time;
        this.filePath = filePath;
    }

    public int getId(){
        return id;
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

    //refers to a song that already exists in the database, thats why id is present here because it already exists
    public Song(int id, String title, String artist, String category, String time, String filePath){
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.category = category;
        this.time = time;
        this.filePath = filePath;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setTitle(String title){

        this.title = title;
    }

    public void setArtist(String artist){

        this.artist = artist;
    }

    public void setCategory(String category){

        this.category = category;
    }

    public void setTime(String time){

        this.time = time;
    }

    public void setFilePath(String filePath){

        this.filePath = filePath;
    }


}