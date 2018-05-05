package work.juanhernandez.postaround.ui.base;

/**
 * Created by juan.hernandez on 5/4/18.
 * MvpPresenter
 */

public interface MvpPresenter<V extends MvpView> {
    void onAttach(V mvpView);
}
