package unist.pjs.the.ui.matching

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import unist.pjs.the.BaseViewModel
import unist.pjs.the.data.Match
import unist.pjs.the.data.MatchDao
import unist.pjs.the.data.Room
import unist.pjs.the.data.RoomDao
import unist.pjs.the.extends.Preferences
import javax.inject.Inject

@HiltViewModel
class ProfileDetailViewModel @Inject constructor(
    private val matchDao: MatchDao,
    private val roomDao: RoomDao,
) : BaseViewModel() {
    private val _isHeart: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isHeart: LiveData<Boolean> = _isHeart

    private val _roomId: MutableLiveData<String> = MutableLiveData<String>()
    val roomId: LiveData<String> = _roomId

    fun getMatch(name: String) =
        viewModelScope.launch(exceptionHandler) {
            val match: Match = matchDao.getMatch(name)
            if (match != null) {
                _isHeart.value = match.isHeart
            }
        }

    fun getRoomId(name: String) = viewModelScope.launch(exceptionHandler) {
        var room: Room? = roomDao.getRoom(listOf(Preferences.userName, name))

        if (room == null){
            room = roomDao.getRoom(listOf(name, Preferences.userName))
        }
        if (room != null) {
            _roomId.value = room.roomid
        } else {
            _roomId.value = "no"
        }
    }

    fun resetRoomId() = viewModelScope.launch(exceptionHandler){
        _roomId.value = ""
    }
}