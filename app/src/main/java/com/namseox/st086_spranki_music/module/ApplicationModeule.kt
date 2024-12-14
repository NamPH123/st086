package com.namseox.st086_spranki_music.module

import android.content.Context
import com.namseox.st086_spranki_music.utils.SharedPreferenceUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModeule {
    @Singleton
    @Provides
    fun providerSharedPreference(@ApplicationContext appContext: Context): SharedPreferenceUtils {
        return SharedPreferenceUtils.getInstance(appContext)
    }
//
//    @Singleton
//    @Provides
//    fun providerRepository(@ApplicationContext context: Context): VideoRepository {
//        return VideoRepository(context)
//    }

//    @Singleton
//    @Provides
//    fun providerApi(@ApplicationContext context: Context) : ApiHelper {
//        return ApiHelper(context)
//    }
//
//    @Singleton
//    @Provides
//    fun providerApiRepository( apiHelper: ApiHelper): ApiRepository {
//        return ApiRepository(apiHelper)
//    }
}