package com.stashinvest.stashchallenge.api;

import com.stashinvest.stashchallenge.api.model.ImageResponse;
import com.stashinvest.stashchallenge.api.model.MetadataResponse;
import io.reactivex.Single;
import javax.inject.Inject;

public class GettyImageService {
  public static final String FIELDS = "id,title,thumb";
  public static final String SORT_ORDER = "best";

  @Inject GettyImagesApi api;

  @Inject public GettyImageService() {
  }

  public Single<ImageResponse> searchImages(String phrase) {
    return api.searchImages(phrase, FIELDS, SORT_ORDER);
  }

  public Single<MetadataResponse> getImageMetadata(String id) {
    return api.getImageMetadata(id);
  }

  public Single<ImageResponse> getSimilarImages(String id) {
    return api.getSimilarImages(id);
  }
}
