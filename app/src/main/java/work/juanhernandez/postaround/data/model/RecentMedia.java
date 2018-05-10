package work.juanhernandez.postaround.data.model;

/**
 * Created by juan.hernandez on 5/4/18.
 * RecentMedia
 */

public class RecentMedia {
    private String id;

    private User user;

    private MediasIG images;

    private MediasIG videos;

    private Caption caption;

    private String type;

    private LocationIG location;

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public MediasIG getImages() {
        return images;
    }

    public MediasIG getVideos() {
        return videos;
    }

    public Caption getCaption() {
        return caption != null ? caption : new Caption();
    }

    public String getType() {
        return type;
    }

    public LocationIG getLocation() {
        return location;
    }
}
