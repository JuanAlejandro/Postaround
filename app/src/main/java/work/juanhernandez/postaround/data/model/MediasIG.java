package work.juanhernandez.postaround.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by juan.hernandez on 5/4/18.
 * MediasIG
 */

public class MediasIG {
    @SerializedName("standard_resolution")
    private
    MediaIG standardResolution;

    public MediaIG getStandardResolution() {
        return standardResolution;
    }
}
