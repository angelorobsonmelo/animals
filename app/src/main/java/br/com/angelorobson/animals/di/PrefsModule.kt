package br.com.angelorobson.animals.di

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import br.com.angelorobson.animals.util.SharePreferencesHelper
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
open class PrefsModule {

    @Provides
    @Singleton
    @TypeOfContext(CONTEXT_APP)
    open fun provideSharedPreferences(app: Application): SharePreferencesHelper {
        return SharePreferencesHelper(app)
    }

    @Provides
    @Singleton
    @TypeOfContext(CONTEXT_ACTIVITY)
    fun provideActivitySharedPreferences(activity: AppCompatActivity): SharePreferencesHelper {
        return SharePreferencesHelper(activity)
    }

}

const val CONTEXT_APP = "application context"
const val CONTEXT_ACTIVITY = "activity context"

@Qualifier
annotation class TypeOfContext(val type: String)