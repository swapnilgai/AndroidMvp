package com.stashinvest.stashchallenge.ui.fragment.main;

import com.stashinvest.stashchallenge.api.model.ImageResult;
import java.util.List;

public interface MainFragmentContract {

  interface MainView {

    void setSearchSucceed(List<ImageResult> imageResultList);

    void setSearchFailed();

    void setLoading(Boolean active);
  }

  interface MainPresenter {
    void search(String searchItem);
  }
}
