package com.stashinvest.stashchallenge.ui.fragment.popup;

import com.stashinvest.stashchallenge.api.model.ImageResponse;
import com.stashinvest.stashchallenge.api.model.MetadataResponse;

public interface PopUpDialogFragmentContract {

  interface DialogView {
    void setMetadataResponse(MetadataResponse metadataResponse);

    void setMetadataError();

    void setMetadataLoading(boolean active);

    void setSimilarImagesResponse(ImageResponse imageResponse);

    void setSimilarImagesError();

    void setSimilarImagesLoading(boolean active);
  }

  interface DialogPresenter {
    void getImagesMetaData(String id);

    void getSimilarImages(String id);
  }
}
