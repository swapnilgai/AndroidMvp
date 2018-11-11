package com.stashinvest.stashchallenge.ui.fragment.popup;

import com.stashinvest.stashchallenge.api.GettyImageService;
import com.stashinvest.stashchallenge.api.model.ImageResponse;
import com.stashinvest.stashchallenge.api.model.MetadataResponse;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class PopUpDialogPresenter implements PopUpDialogFragmentContract.DialogPresenter {

  private GettyImageService gettyImageService;

  private PopUpDialogFragmentContract.DialogView dialogView;

  private CompositeDisposable disposable = new CompositeDisposable();

  public PopUpDialogPresenter(GettyImageService gettyImageService,
      PopUpDialogFragmentContract.DialogView dialogView) {
    this.gettyImageService = gettyImageService;
    this.dialogView = dialogView;
  }

  @Override public void getImagesMetaData(@NonNull String id) {

    dialogView.setMetadataLoading(true);

    disposable.add(gettyImageService.getImageMetadata(id)
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::onMetadataResponse, this::onMetadataError));
  }

  @Override public void getSimilarImages(@NonNull String id) {

    dialogView.setSimilarImagesLoading(true);

    disposable.add(gettyImageService.getSimilarImages(id)
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::onSimilarImagesResponse, this::onSimilarImagesError));
  }

  public void clear() {
    disposable.clear();
  }

  private void onMetadataResponse(MetadataResponse metadataResponse) {
    dialogView.setMetadataLoading(false);
    dialogView.setMetadataResponse(metadataResponse);
  }

  private void onMetadataError(Throwable throwable) {
    dialogView.setMetadataLoading(false);

    dialogView.setMetadataError();
  }

  private void onSimilarImagesResponse(ImageResponse imageResponse) {
    dialogView.setSimilarImagesLoading(false);

    dialogView.setSimilarImagesResponse(imageResponse);
  }

  private void onSimilarImagesError(Throwable throwable) {
    dialogView.setSimilarImagesLoading(false);

    dialogView.setSimilarImagesError();
  }
}
