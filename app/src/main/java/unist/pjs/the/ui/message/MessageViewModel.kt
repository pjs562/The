package unist.pjs.the.ui.message

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import unist.pjs.the.BaseViewModel
import unist.pjs.the.data.*
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val profileDao: ProfileDao,
    private val roomDao: RoomDao
): BaseViewModel() {

    private val _chatRoomList: MutableLiveData<List<ChatRoom>> = MutableLiveData<List<ChatRoom>>()
    val chatRoomList: LiveData<List<ChatRoom>> = _chatRoomList

    private val _profileList: MutableLiveData<List<Profile>> = MutableLiveData<List<Profile>>()
    val profileList: LiveData<List<Profile>> = _profileList

    private val _roomList: MutableLiveData<List<Room>> = MutableLiveData<List<Room>>()
    val roomList: LiveData<List<Room>> = _roomList

    fun upDateRoomList(list: List<ChatRoom>){
        _chatRoomList.value = list
        getRoomList()
    }

    fun upDateRoomList2(list: List<ChatRoom>){
        _chatRoomList.value = list
    }

    fun getProfileList() = viewModelScope.launch(exceptionHandler) {
        _profileList.value = profileDao.getProfileList()
    }

    fun getRoomList() = viewModelScope.launch(exceptionHandler) {
        _roomList.value = roomDao.getRoomList()
    }
}