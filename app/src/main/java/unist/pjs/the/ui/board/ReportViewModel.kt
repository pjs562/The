package unist.pjs.the.ui.board

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import unist.pjs.the.BaseViewModel
import unist.pjs.the.data.Confirm
import unist.pjs.the.data.MatchDao
import unist.pjs.the.repository.BoardService
import unist.pjs.the.repository.LikeService
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val service: BoardService
) : BaseViewModel() {
    private val _confirm: MutableLiveData<Confirm> = MutableLiveData<Confirm>()
    val confirm: LiveData<Confirm> = _confirm

    fun postReport(name: String, boardId: String, content: String, type: String) =
        viewModelScope.launch(exceptionHandler) {
            _confirm.value = service.postBoardReport(name, boardId, content, type)
        }
}