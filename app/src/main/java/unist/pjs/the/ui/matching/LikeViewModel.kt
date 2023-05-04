package unist.pjs.the.ui.matching

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import unist.pjs.the.BaseViewModel
import unist.pjs.the.data.Match
import unist.pjs.the.data.MatchDao
import unist.pjs.the.data.UserInfo
import unist.pjs.the.repository.LikeService
import javax.inject.Inject

@HiltViewModel
class LikeViewModel @Inject constructor(
    private val service: LikeService,
    private val matchDao: MatchDao,
) : BaseViewModel() {
    private val _userInfo: MutableLiveData<UserInfo> = MutableLiveData<UserInfo>()
    val userInfo: LiveData<UserInfo> = _userInfo

    private val _result: MutableLiveData<String> = MutableLiveData<String>()
    val result: LiveData<String> = _result

    fun sendHeart(name: String, partner: String) =
        viewModelScope.launch(exceptionHandler) {
            _userInfo.value = service.postSendHeart(name, partner)
        }

    fun sendLike(name: String, partner: String) =
        viewModelScope.launch(exceptionHandler) {
            _userInfo.value = service.postSendLike(name, partner)
        }

    fun insertMatch(matches: List<Match>) {
        viewModelScope.launch {
            matchDao.insertAllMatch(matches)
            _result.value = "ok"
        }
    }
}