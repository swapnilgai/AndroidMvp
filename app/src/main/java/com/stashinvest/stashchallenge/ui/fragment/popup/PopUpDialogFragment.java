package com.stashinvest.stashchallenge.ui.fragment.popup;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.squareup.picasso.Picasso;
import com.stashinvest.stashchallenge.App;
import com.stashinvest.stashchallenge.R;
import com.stashinvest.stashchallenge.api.GettyImageService;
import com.stashinvest.stashchallenge.api.model.ImageResponse;
import com.stashinvest.stashchallenge.api.model.MetadataResponse;
import javax.inject.Inject;

public class PopUpDialogFragment extends DialogFragment
    implements PopUpDialogFragmentContract.DialogView {

  @Inject GettyImageService gettyImageService;

  @BindView(R.id.similar_image_view1) ImageView similarImageView1;

  @BindView(R.id.similar_image_view2) ImageView similarImageView2;

  @BindView(R.id.similar_image_view3) ImageView similarImageView3;

  @BindView(R.id.similar_image_item_linear_layout) LinearLayout similarImageItemLinearLayout;

  @BindView(R.id.similar_image_item_error) View similarImageItemError;

  @BindView(R.id.similar_image_item_loading) View similarImageItemLoading;

  @BindView(R.id.image_view) ImageView mainImage;

  @BindView(R.id.title_view) TextView titleTextView;

  @BindView(R.id.loading) View loadingView;

  @BindView(R.id.error) View errorView;

  private PopUpDialogPresenter dialogPresenter;

  private Unbinder unbinder;

  private String id;
  private String uri;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_dialog_popup, container, false);
    unbinder = ButterKnife.bind(this, view);

    return view;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    App.getInstance().getAppComponent().inject(this);
    id = getArguments().getString(getString(R.string.id));
    uri = getArguments().getString(getString(R.string.url));
    dialogPresenter = new PopUpDialogPresenter(gettyImageService, this);
  }

  @Override public void setMetadataResponse(MetadataResponse metadataResponse) {
    titleTextView.setText(String.format(getString(R.string.metadata_title_format), metadataResponse.getMetadata().get(0).getTitle(),
                            metadataResponse.getMetadata().get(0).getArtist()));
  }

  @Override public void setMetadataError() {
    titleTextView.setVisibility(View.GONE);
    errorView.setVisibility(View.VISIBLE);
    loadingView.setVisibility(View.GONE);
  }

  @Override public void setMetadataLoading(boolean active) {
    if (active) {
      titleTextView.setVisibility(View.GONE);
      errorView.setVisibility(View.GONE);
      loadingView.setVisibility(View.VISIBLE);
    } else {
      titleTextView.setVisibility(View.VISIBLE);
      errorView.setVisibility(View.GONE);
      loadingView.setVisibility(View.GONE);
    }
  }

  @Override public void setSimilarImagesResponse(@NonNull ImageResponse imageResponse) {

    if (imageResponse.getImages().size() > 0 && imageResponse.getImages().get(0) != null) {
      renderImage(similarImageView1,
          imageResponse.getImages().get(0).getDisplaySizes().get(0).getUri());
    } else {
      similarImageView1.setVisibility(View.GONE);
    }

    if (imageResponse.getImages().size() > 1 && imageResponse.getImages().get(1) != null) {
      renderImage(similarImageView2,
          imageResponse.getImages().get(1).getDisplaySizes().get(0).getUri());
    } else {
      similarImageView2.setVisibility(View.GONE);
    }

    if (imageResponse.getImages().size() > 2 && imageResponse.getImages().get(2) != null) {
      renderImage(similarImageView3,
          imageResponse.getImages().get(2).getDisplaySizes().get(0).getUri());
    } else {
      similarImageView3.setVisibility(View.GONE);
    }

    similarImageItemLinearLayout.setVisibility(View.VISIBLE);
  }

  @Override public void setSimilarImagesError() {
    similarImageItemError.setVisibility(View.VISIBLE);
    similarImageItemLinearLayout.setVisibility(View.GONE);
  }

  @Override public void setSimilarImagesLoading(boolean active) {
    if (active) {
      similarImageItemLinearLayout.setVisibility(View.GONE);
      similarImageItemError.setVisibility(View.GONE);
      similarImageItemLoading.setVisibility(View.VISIBLE);
    } else {
      similarImageItemLoading.setVisibility(View.GONE);
    }
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
    dialogPresenter.clear();
  }

  @Override public void onResume() {
    super.onResume();
    if (uri != null) renderImage(mainImage, uri);
    dialogPresenter.getImagesMetaData(id);
    dialogPresenter.getSimilarImages(id);
  }

  private void renderImage(ImageView imageView, @NonNull String Uri) {
    Picasso.with(imageView.getContext()).load(Uri).into(imageView);
  }
}
