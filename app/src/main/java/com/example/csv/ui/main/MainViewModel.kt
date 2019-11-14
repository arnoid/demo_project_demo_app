package com.example.csv.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.csv.logic.data.Row
import com.example.csv.logic.usecase.LoadAndSaveRecordsUseCase
import com.example.csv.logic.usecase.ReadHeadersUseCase
import com.example.csv.logic.usecase.ReadRowsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel : ViewModel() {

    @Inject
    lateinit var loadAndSaveRecordsUseCase: LoadAndSaveRecordsUseCase
    @Inject
    lateinit var readRowsUseCase: ReadRowsUseCase
    @Inject
    lateinit var readHeadersUseCase: ReadHeadersUseCase

    private val viewStateLiveData = MutableLiveData<ViewState>()
    private val rowsLiveData = MutableLiveData<List<Row>>()
    private val headersLiveData = MutableLiveData<Row>()

    fun viewStateLiveData(): LiveData<ViewState> {
        return viewStateLiveData
    }

    fun postViewState(viewState: ViewState) {
        viewStateLiveData.postValue(viewState)
    }

    fun rowsLiveData(): LiveData<List<Row>> {
        return rowsLiveData
    }

    fun postRows(newRows: List<Row>) {
        rowsLiveData.postValue(newRows)
    }

    fun headersLiveData(): LiveData<Row> {
        return headersLiveData
    }

    fun postHeaders(newHeaders: Row) {
        headersLiveData.postValue(newHeaders)
    }

    fun retrieveRows() {
        viewModelScope.launch(Dispatchers.IO) {
            postViewState(ViewState.Loading)

            try {
                loadAndSaveRecordsUseCase.execute()
                postHeaders(readHeadersUseCase.execute())
                postRows(readRowsUseCase.execute())

                postViewState(ViewState.Ready)
            } catch (e: Exception) {
                postViewState(ViewState.Error(e))
            }
        }
    }
}

sealed class ViewState {
    object Loading : ViewState()
    object Ready : ViewState()
    class Error(val exception: Exception) : ViewState()
}
