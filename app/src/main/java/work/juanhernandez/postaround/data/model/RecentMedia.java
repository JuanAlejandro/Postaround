package work.juanhernandez.postaround.data.model;

/**
 * Created by juan.hernandez on 5/4/18.
 * RecentMedia
 */

public class RecentMedia {
    public static final String IMAGE_TYPE = "image";
    public static final String VIDEO_TYPE = "video";

    String id;

    User user;

    MediasIG images;

    MediasIG videos;

    Caption caption;

    String type;

    LocationIG location;

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
        return caption;
    }

    public String getType() {
        return type;
    }

    public LocationIG getLocation() {
        return location;
    }
}
