package work.juanhernandez.postaround.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by juan.hernandez on 5/4/18.
 * ImagesIG
 */

public class ImagesIG {
    @SerializedName("standard_resolution")
    ImageIG standardResolution;

    public ImageIG getStandardResolution() {
        return standardResolution;
    }
}
