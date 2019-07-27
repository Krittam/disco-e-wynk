package in.wynk.disco.disco_e_wynk;

public class ModelClass {

    private String songId;
    private String imageResource;
    private String title;
    private String body;

    public ModelClass(String songId, String imageResource, String title, String body) {
        this.songId = songId;
        this.imageResource = imageResource;
        this.title = title;
        this.body = body;
    }

    public String getSongId() {
        return songId;
    }

    public String getImageResource() {
        return imageResource;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
}
