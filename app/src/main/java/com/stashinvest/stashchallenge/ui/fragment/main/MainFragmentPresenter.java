package com.stashinvest.stashchallenge.ui.fragment.main;

import com.stashinvest.stashchallenge.api.GettyImageService;
import com.stashinvest.stashchallenge.api.model.ImageResponse;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainFragmentPresenter implements MainFragmentContract.MainPresenter {

  private GettyImageService gettyImageService;

  private MainFragmentContract.MainView mainView;

  private CompositeDisposable disposable = new CompositeDisposable();

  MainFragmentPresenter(GettyImageService gettyImageService,
      MainFragmentContract.MainView mainView) {
    this.gettyImageService = gettyImageService;
    this.mainView = mainView;
  }

  @Override public void search(String searchItem) {
    mainView.setLoading(true);
    disposable.add(gettyImageService.searchImages(searchItem)
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::onResponse, this::displayError));
  }

  public void clear() {
    disposable.clear();
  }

  private void onResponse(ImageResponse response) {
    mainView.setLoading(false);

    if (response != null) {
      mainView.setSearchSucceed(response.getImages());
    } else {
      displayError(new Throwable("Error"));
    }
  }

  private void displayError(Throwable throwable) {
    mainView.setLoading(false);
    mainView.setSearchFailed();
  }
}
