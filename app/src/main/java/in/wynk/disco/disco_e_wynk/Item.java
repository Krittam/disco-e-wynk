package in.wynk.disco.disco_e_wynk;

import java.util.List;

public class Item {
    private String title;
    private String id;
    private List<Song> items;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Song> getItems() {
        return items;
    }

    public void setItems(List<Song> items) {
        this.items = items;
    }
}
