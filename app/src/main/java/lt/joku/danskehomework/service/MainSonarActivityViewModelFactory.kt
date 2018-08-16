package lt.joku.danskehomework.service

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import lt.joku.danskehomework.viewmodel.MainActivityViewModel

class MainActivityViewModelFactory: ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainActivityViewModel() as T
    }

}