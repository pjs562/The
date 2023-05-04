package unist.pjs.the.ui.board

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import unist.pjs.the.BaseViewModel
import unist.pjs.the.data.Bulletin
import unist.pjs.the.data.BulletinDao
import unist.pjs.the.extends.Preferences
import unist.pjs.the.repository.BoardService
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val bulletinDao: BulletinDao,
    private val service: BoardService
): BaseViewModel() {
    private val _type: MutableLiveData<String> = MutableLiveData<String>()
    val type: LiveData<String> = _type

    private val _bulletin: MutableLiveData<Bulletin> = MutableLiveData<Bulletin>()
    val bulletin: LiveData<Bulletin> = _bulletin

    private val _bulletin2: MutableLiveData<Bulletin> = MutableLiveData<Bulletin>()
    val bulletin2: LiveData<Bulletin> = _bulletin2

    fun getBulletin(id: String) = viewModelScope.launch(exceptionHandler) {
        _bulletin2.value = bulletinDao.getBulletin(id)
    }

    fun postBoard(title: String, content: String) =
        viewModelScope.launch(exceptionHandler) {
            when(type.value){
                "notice" -> {
                    _bulletin.value = service.postNotice(Preferences.userName, title, content)
                }

                "free" -> {
                    _bulletin.value = service.postFree(Preferences.userName, title, content)
                }

                "pray" -> {
                    _bulletin.value = service.postPray(Preferences.userName, title, content)
                }

                "qt" -> {
                    _bulletin.value = service.postQuitetime(Preferences.userName, title, content)
                }
            }
        }

    fun putBoard(boardId: String, title: String, content: String) = viewModelScope.launch(exceptionHandler) {
        when(type.value){
            "notice" -> {
                _bulletin.value = service.putNotice(Preferences.userName, boardId, title, content)
            }

            "free" -> {
                _bulletin.value = service.putFree(Preferences.userName, boardId, title, content)
            }

            "pray" -> {
                _bulletin.value = service.putPray(Preferences.userName, boardId, title, content)
            }

            "qt" -> {
                _bulletin.value = service.putQuitetime(Preferences.userName, boardId, title, content)
            }
        }
    }

    fun upDateBulletin(bulletin: Bulletin) = viewModelScope.launch(exceptionHandler) {
        bulletinDao.updateBulletin(bulletin)
    }

    fun changeType(type: String) {
        _type.value = type
    }
}