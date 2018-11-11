package com.stashinvest.stashchallenge.ui.fragment.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.stashinvest.stashchallenge.App;
import com.stashinvest.stashchallenge.R;
import com.stashinvest.stashchallenge.api.GettyImageService;
import com.stashinvest.stashchallenge.api.model.ImageResult;
import com.stashinvest.stashchallenge.ui.adapter.ViewModelAdapter;
import com.stashinvest.stashchallenge.ui.factory.GettyImageFactory;
import com.stashinvest.stashchallenge.ui.fragment.popup.PopUpDialogFragment;
import com.stashinvest.stashchallenge.ui.viewmodel.BaseViewModel;
import com.stashinvest.stashchallenge.util.SpaceItemDecoration;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import static android.view.View.GONE;

public class MainFragment extends Fragment implements MainFragmentContract.MainView {
  private final String TAG = "POP_UP_DIALOG";
  @Inject ViewModelAdapter adapter;
  @Inject GettyImageService gettyImageService;
  @Inject GettyImageFactory gettyImageFactory;
  @Inject PopUpDialogFragment dialogFragment;
  @BindView(R.id.search_phrase) EditText searchView;
  @BindView(R.id.recycler_view) RecyclerView recyclerView;
  @BindView(R.id.loading) View loading;
  @BindView(R.id.error) View error;
  @BindDimen(R.dimen.image_space) int space;
  Unbinder unbinder;
  private MainFragmentPresenter mainFragmentPresenter;

  public static MainFragment newInstance() {
    return new MainFragment();
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    App.getInstance().getAppComponent().inject(this);

    mainFragmentPresenter = new MainFragmentPresenter(gettyImageService, this);
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_main, container, false);
    unbinder = ButterKnife.bind(this, view);

    searchView.setOnEditorActionListener((v, actionId, event) -> {
      if (actionId == EditorInfo.IME_ACTION_SEARCH) {
        loading.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        mainFragmentPresenter.search(searchView.getText().toString());
        return true;
      }
      return false;
    });

    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
    recyclerView.setAdapter(adapter);
    recyclerView.addItemDecoration(new SpaceItemDecoration(space, space, space, space));
    return view;
  }

  public void onImageLongPress(String id, String uri) {
    Bundle args = new Bundle();
    args.putString(getString(R.string.id), id);
    args.putString(getString(R.string.url), uri);
    dialogFragment.setArguments(args);
    dialogFragment.show(getFragmentManager(), TAG);
  }

  @Override public void setSearchSucceed(List<ImageResult> imageResultList) {
    updateImages(imageResultList);
    error.setVisibility(View.GONE);
    recyclerView.setVisibility(View.VISIBLE);
  }

  @Override public void setSearchFailed() {
    error.setVisibility(View.VISIBLE);
    recyclerView.setVisibility(GONE);
  }

  @Override public void setLoading(Boolean active) {
    if (active) {
      loading.setVisibility(View.VISIBLE);
    } else {
      loading.setVisibility(View.GONE);
    }
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
    mainFragmentPresenter.clear();
  }

  private void updateImages(List<ImageResult> images) {
    List<BaseViewModel> viewModels = new ArrayList<>();
    int i = 0;
    for (ImageResult imageResult : images) {
      viewModels.add(
          gettyImageFactory.createGettyImageViewModel(i++, imageResult, this::onImageLongPress));
    }
    adapter.setViewModels(viewModels);
  }
}
