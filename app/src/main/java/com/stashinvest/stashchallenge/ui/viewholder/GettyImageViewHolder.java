package com.stashinvest.stashchallenge.ui.viewholder;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.squareup.picasso.Picasso;
import com.stashinvest.stashchallenge.R;
import com.stashinvest.stashchallenge.api.model.ImageResult;
import com.stashinvest.stashchallenge.util.LongPressGestureDetector;

public class GettyImageViewHolder extends RecyclerView.ViewHolder {
  @BindView(R.id.image_view) ImageView imageView;

  public GettyImageViewHolder(View itemView) {
    super(itemView);
    ButterKnife.bind(this, itemView);
  }

  public void bind(ImageResult imageResult, LongPressGestureDetector.Listener listener) {
    GestureDetectorCompat gestureDetector =
        new GestureDetectorCompat(itemView.getContext(), new LongPressGestureDetector(listener));

    itemView.setOnTouchListener((v, event) -> {
      gestureDetector.onTouchEvent(event);
      return true;
    });

    Picasso.with(itemView.getContext()).load(imageResult.getThumbUri()).into(imageView);
  }
}
