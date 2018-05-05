package work.juanhernandez.postaround.ui.base;


/**
 * Created by juan.hernandez on 5/4/18.
 * BasePresenter
 */

public class BasePresenter<V extends MvpView> implements MvpPresenter<V>{
    private V mvpView;

    @Override
    public void onAttach(V mvpView) {
        this.mvpView = mvpView;
    }

    public V getMvpView() {
        return mvpView;
    }
}
