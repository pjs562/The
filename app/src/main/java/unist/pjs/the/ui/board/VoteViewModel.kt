package unist.pjs.the.ui.board

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import unist.pjs.the.BaseViewModel
import unist.pjs.the.data.Confirm
import unist.pjs.the.repository.BoardService
import javax.inject.Inject

@HiltViewModel
class VoteViewModel @Inject constructor(
    val service: BoardService
): BaseViewModel() {
    private val _confirm: MutableLiveData<Confirm> = MutableLiveData<Confirm>()
    val confirm: LiveData<Confirm> = _confirm

    fun postPoll(id: String, content: String, type: String) = viewModelScope.launch(exceptionHandler) {
        _confirm.value = service.postPoll(id, content, type)
        Log.e("TEST", "confirm: ${_confirm.value}")
    }
}