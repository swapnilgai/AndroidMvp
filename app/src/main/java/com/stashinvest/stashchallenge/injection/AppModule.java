package com.stashinvest.stashchallenge.injection;

import android.app.Application;
import android.content.Context;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module public class AppModule {
  Application application;

  public AppModule(Application application) {
    this.application = application;
  }

  @Provides @Singleton @ForApplication Context provideContext() {
    return application;
  }
}
