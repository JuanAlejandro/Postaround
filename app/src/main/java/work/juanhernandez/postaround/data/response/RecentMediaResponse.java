package work.juanhernandez.postaround.data.response;

import java.util.List;

import work.juanhernandez.postaround.data.model.RecentMedia;


/**
 * Created by juan.hernandez on 5/4/18.
 * RecentMediaResponse
 */

public class RecentMediaResponse {
    List<RecentMedia> data;

    public RecentMediaResponse(List<RecentMedia> data) {
        this.data = data;
    }

    public List<RecentMedia> getData() {
        return data;
    }
}