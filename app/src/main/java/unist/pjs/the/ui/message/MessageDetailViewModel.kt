package unist.pjs.the.ui.message


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import unist.pjs.the.BaseViewModel
import unist.pjs.the.data.Chat
import unist.pjs.the.data.ChatRoom
import unist.pjs.the.data.Profile
import unist.pjs.the.data.ProfileDao
import javax.inject.Inject

@HiltViewModel
class MessageDetailViewModel @Inject constructor(
    private val profileDao: ProfileDao
) : BaseViewModel() {
    private val _chatList: MutableLiveData<List<Chat>> = MutableLiveData<List<Chat>>()
    val chatList: LiveData<List<Chat>> = _chatList

    private val _profileList: MutableLiveData<List<Profile>> = MutableLiveData<List<Profile>>()
    private val profileList: LiveData<List<Profile>> = _profileList

    private val _profile: MutableLiveData<Profile> = MutableLiveData<Profile>()
    val profile: LiveData<Profile> = _profile

    private val _position: MutableLiveData<Int> = MutableLiveData(0)
    val position: LiveData<Int> = _position

    private val _user: MutableLiveData<String> = MutableLiveData<String>()
    val user: LiveData<String> = _user

    fun upDateChatList(array: List<Chat>){
        _chatList.value = array
        if(array.isNotEmpty())
            _position.value = array.size - 1
        else
            _position.value = 0
    }

    fun getProfileList() = viewModelScope.launch(exceptionHandler) {
        _profileList.value = profileDao.getProfileList()
    }

    fun setProfile(id: String){
        _user.value = id
        _profile.value = profileList.value?.find { it._id == id }
    }
}