package com.stashinvest.stashchallenge.injection;

import com.stashinvest.stashchallenge.App;
import com.stashinvest.stashchallenge.ui.activity.MainActivity;
import com.stashinvest.stashchallenge.ui.fragment.main.MainFragment;
import com.stashinvest.stashchallenge.ui.fragment.popup.PopUpDialogFragment;
import dagger.Component;
import javax.inject.Singleton;

@Singleton @Component(modules = { AppModule.class, NetworkModule.class, FragmentModule.class })
public interface AppComponent {
  void inject(App app);

  void inject(MainActivity activity);

  void inject(MainFragment fragment);

  void inject(PopUpDialogFragment fragment);
}