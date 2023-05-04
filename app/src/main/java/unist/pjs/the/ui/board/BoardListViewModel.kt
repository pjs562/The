package unist.pjs.the.ui.board

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import unist.pjs.the.BaseViewModel
import unist.pjs.the.data.Bulletin
import unist.pjs.the.data.BulletinDao
import unist.pjs.the.repository.BoardService
import javax.inject.Inject

@HiltViewModel
class BoardListViewModel @Inject constructor(
    private val service: BoardService,
    private val bulletinDao: BulletinDao,
) : BaseViewModel() {
    val bulletinList = bulletinDao.getBulletin().asLiveData()
    private var count = 20
    lateinit var list: List<Bulletin>

    private val _type: MutableLiveData<String> = MutableLiveData<String>()
    val type: LiveData<String> = _type

    fun getBoardList(lastId: String) =
        viewModelScope.launch(exceptionHandler) {
            if (count == 20) {
                when (type.value) {
                    "notice" -> {
                        list = service.getNotice(lastId).boards
                        count = list.size
                        bulletinDao.insertAllBulletin(list)
                    }

                    "free" -> {
                        list = service.getFree(lastId).boards
                        count = list.size
                        bulletinDao.insertAllBulletin(list)
                    }

                    "pray" -> {
                        list = service.getPray(lastId).boards
                        count = list.size
                        bulletinDao.insertAllBulletin(list)
                    }

                    "qt" -> {
                        list = service.getQuitetime(lastId).boards
                        count = list.size
                        bulletinDao.insertAllBulletin(list)
                    }
                }
            }
        }

    fun changeType(type: String) {
        _type.value = type
        viewModelScope.launch(exceptionHandler) {
            bulletinDao.deleteAll()
            count = 20
            getBoardList("")
        }
    }
}