package unist.pjs.the

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import unist.pjs.the.data.*
import unist.pjs.the.repository.UserInfoService
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val service: UserInfoService,
    private val matchDao: MatchDao,
    private val boardDao: LikeBoardDao,
    private val roomDao: RoomDao
): BaseViewModel() {

    private val _userInfo: MutableLiveData<UserInfo> = MutableLiveData<UserInfo>()
    val userInfo: LiveData<UserInfo> = _userInfo

    init {
        viewModelScope.launch {
            matchDao.deleteMatch()
            boardDao.deleteAllLikeBoard()
            roomDao.deleteRoom()
        }
    }
    fun getUserInfo(name: String){
        viewModelScope.launch(exceptionHandler) {
            _userInfo.value = service.getUserInfo(name)
        }
    }

    fun insertMatches(matches: List<Match>) {
        viewModelScope.launch {
            matchDao.insertAllMatch(matches)
        }
    }

    fun insertLikeBoards(boards: List<String>){
        viewModelScope.launch {
            val arr = ArrayList<LikeBoard>()
            for(s in boards) {
                arr.add(LikeBoard(s))
            }
            boardDao.insertLikeBoards(arr)
        }
    }

    fun insertRooms(rooms: List<Room>?){
        viewModelScope.launch {
            rooms?.let { roomDao.insertAllRoom(rooms) }
        }
    }
}