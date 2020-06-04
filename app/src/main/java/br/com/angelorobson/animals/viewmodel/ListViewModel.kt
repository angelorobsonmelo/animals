package br.com.angelorobson.animals.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import br.com.angelorobson.animals.di.AppModule
import br.com.angelorobson.animals.di.CONTEXT_APP
import br.com.angelorobson.animals.di.DaggerViewModelComponent
import br.com.angelorobson.animals.di.TypeOfContext
import br.com.angelorobson.animals.model.Animal
import br.com.angelorobson.animals.model.AnimalApiService
import br.com.angelorobson.animals.model.ApiKey
import br.com.angelorobson.animals.util.SharePreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ListViewModel(application: Application) : AndroidViewModel(application) {

    constructor(application: Application, test: Boolean = true) : this(application) {
        injected = true
    }

    val animals by lazy { MutableLiveData<List<Animal>>() }
    val loadError by lazy { MutableLiveData<Boolean>() }
    val loading by lazy { MutableLiveData<Boolean>() }

    private val disposable = CompositeDisposable()

    @Inject
    lateinit var apiService: AnimalApiService

    @Inject
    @field:TypeOfContext(CONTEXT_APP)
    lateinit var prefs: SharePreferencesHelper

    private var invalidApiKey = false
    private var injected = false

    fun inject() {
        if (injected.not()) {
            DaggerViewModelComponent.builder()
                .appModule(AppModule(getApplication()))
                .build()
                .inject(this)
        }
    }

    fun refresh() {
        inject()
        loading.value = true
        invalidApiKey = false
        val key = prefs.getApiKey()
        if (key.isNullOrEmpty()) {
            getKey()
            return
        }

        getAnimals(key)
    }

    fun hardRefresh() {
        inject()
        loading.value = true
        getKey()
    }

    private fun getKey() {
        disposable.add(
            apiService.getApiKey()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    loading.value = true
                }
                .doAfterTerminate {
                    loading.value = false
                }
                .subscribeWith(object : DisposableSingleObserver<ApiKey>() {
                    override fun onSuccess(key: ApiKey) {
                        if (key.key.isNullOrEmpty()) {
                            loadError.value = true
                            return
                        }

                        prefs.saveApiKey(key.key)
                        getAnimals(key.key)
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        loadError.value = true
                    }

                })
        )
    }

    private fun getAnimals(key: String) {
        disposable.add(
            apiService.getAnimals(key)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    loading.value = true
                }
                .doAfterTerminate {
                    loading.value = false
                }
                .subscribeWith(object : DisposableSingleObserver<List<Animal>>() {
                    override fun onSuccess(list: List<Animal>) {
                        loadError.value = false
                        animals.value = list
                    }

                    override fun onError(e: Throwable) {
                        if (invalidApiKey.not()) {
                            invalidApiKey = true
                            getKey()
                            return
                        }

                        e.printStackTrace()
                        loadError.value = true
                        animals.value = null

                    }
                }
                )
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}