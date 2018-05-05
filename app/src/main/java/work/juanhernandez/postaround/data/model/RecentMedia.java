package work.juanhernandez.postaround.data.model;

/**
 * Created by juan.hernandez on 5/4/18.
 * RecentMedia
 */

public class RecentMedia {
    String id;

    User user;

    ImagesIG images;

    VideosIG videos;

    Caption caption;

    String type;

    LocationIG location;

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public ImagesIG getImages() {
        return images;
    }

    public VideosIG getVideos() {
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
