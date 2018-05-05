package work.juanhernandez.postaround.data;

/**
 * Created by juan.hernandez on 5/4/18.
 * DataManager
 */

public class DataManager {
    DataManagerListener dataManagerListener;

    public DataManager(DataManagerListener dataManagerListener) {
        this.dataManagerListener = dataManagerListener;
    }
}
