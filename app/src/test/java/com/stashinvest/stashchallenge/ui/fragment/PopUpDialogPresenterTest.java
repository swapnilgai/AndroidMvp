package com.stashinvest.stashchallenge.ui.fragment;

import android.support.annotation.NonNull;
import com.stashinvest.stashchallenge.api.GettyImageService;
import com.stashinvest.stashchallenge.api.model.ImageResponse;
import com.stashinvest.stashchallenge.api.model.MetadataResponse;
import com.stashinvest.stashchallenge.ui.fragment.popup.PopUpDialogFragmentContract;
import com.stashinvest.stashchallenge.ui.fragment.popup.PopUpDialogPresenter;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.TimeUnit;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PopUpDialogPresenterTest {

  private static MetadataResponse metadataResponse;
  private static ImageResponse imageResponse;
  private static String ID;
  private static Throwable throwable;
  @Mock GettyImageService gettyImageService;
  @Mock PopUpDialogFragmentContract.DialogView dialogView;
  PopUpDialogPresenter popUpDialogPresenter;

  @Before public void setUp() {
    // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
    // inject the mocks in the test the initMocks method needs to be called.
    MockitoAnnotations.initMocks(this);

    // Get a reference to the class under test
    popUpDialogPresenter = new PopUpDialogPresenter(gettyImageService, dialogView);

    Scheduler immediate = new Scheduler() {
      @Override
      public Disposable scheduleDirect(@NonNull Runnable run, long delay, @NonNull TimeUnit unit) {
        // this prevents StackOverflowErrors when scheduling with a delay
        return super.scheduleDirect(run, 0, unit);
      }

      @Override public Worker createWorker() {
        return new ExecutorScheduler.ExecutorWorker(Runnable::run);
      }
    };

    RxJavaPlugins.setInitIoSchedulerHandler(scheduler -> immediate);
    RxJavaPlugins.setInitComputationSchedulerHandler(scheduler -> immediate);
    RxJavaPlugins.setInitNewThreadSchedulerHandler(scheduler -> immediate);
    RxJavaPlugins.setInitSingleSchedulerHandler(scheduler -> immediate);
    RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> immediate);

    metadataResponse = new MetadataResponse();

    imageResponse = new ImageResponse();

    ID = "ID";

    throwable = new Throwable();
  }

  @Test public void getImagesMetaData_on_success() {

    when(gettyImageService.getImageMetadata(ID)).thenReturn(Single.just(metadataResponse));

    popUpDialogPresenter.getImagesMetaData(ID);

    verify(dialogView).setMetadataLoading(true);

    verify(dialogView).setMetadataLoading(false);

    verify(dialogView).setMetadataResponse(metadataResponse);
  }

  @Test public void getSimilarImages_on_success() {

    when(gettyImageService.getSimilarImages(ID)).thenReturn(Single.just(imageResponse));

    popUpDialogPresenter.getSimilarImages(ID);

    verify(dialogView).setSimilarImagesLoading(true);

    verify(dialogView).setSimilarImagesLoading(false);

    verify(dialogView).setSimilarImagesResponse(imageResponse);
  }

  @Test public void getSimilarImages_on_error() {

    when(gettyImageService.getSimilarImages(ID)).thenReturn(Single.error(throwable));

    popUpDialogPresenter.getSimilarImages(ID);

    verify(dialogView).setSimilarImagesLoading(true);

    verify(dialogView).setSimilarImagesLoading(false);

    verify(dialogView).setSimilarImagesError();
  }

  @Test public void getImagesMetaData_on_error() {

    when(gettyImageService.getImageMetadata(ID)).thenReturn(Single.error(throwable));

    popUpDialogPresenter.getImagesMetaData(ID);

    verify(dialogView).setMetadataLoading(true);

    verify(dialogView).setMetadataLoading(false);

    verify(dialogView).setMetadataError();
  }
}