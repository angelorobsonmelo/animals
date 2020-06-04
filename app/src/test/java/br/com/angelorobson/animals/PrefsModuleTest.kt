package br.com.angelorobson.animals

import android.app.Application
import br.com.angelorobson.animals.di.PrefsModule
import br.com.angelorobson.animals.util.SharePreferencesHelper

class PrefsModuleTest(val mockPrefs: SharePreferencesHelper): PrefsModule() {

    override fun provideSharedPreferences(app: Application): SharePreferencesHelper {
        return mockPrefs
    }
}