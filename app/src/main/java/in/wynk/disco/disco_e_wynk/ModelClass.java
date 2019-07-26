package in.wynk.disco.disco_e_wynk;

public class ModelClass {

    private int songId;
    private int imageResource;
    private String title;
    private String body;

    public ModelClass(int songId, int imageResource, String title, String body){
        this.songId = songId;
        this.imageResource = imageResource;
        this.title = title;
        this.body = body;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
