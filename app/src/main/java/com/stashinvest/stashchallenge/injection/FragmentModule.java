package com.stashinvest.stashchallenge.injection;

import com.stashinvest.stashchallenge.ui.fragment.popup.PopUpDialogFragment;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module public class FragmentModule {
  @Provides @Singleton PopUpDialogFragment getDialogFragment() {
    return new PopUpDialogFragment();
  }
}
