package unist.pjs.the.ui.board

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import unist.pjs.the.BaseViewModel
import unist.pjs.the.data.*
import unist.pjs.the.extends.Preferences
import unist.pjs.the.repository.BoardService
import javax.inject.Inject

@HiltViewModel
class BalanceDetailViewModel @Inject constructor(
    private val service: BoardService,
    private val balanceDao: BalanceDao,
    private val profileDao: ProfileDao,
    private val likeBoardDao: LikeBoardDao,
) : BaseViewModel() {
    private val _balance: MutableLiveData<Balance> = MutableLiveData<Balance>()
    val balance: LiveData<Balance> = _balance

    private val _profile: MutableLiveData<Profile> = MutableLiveData<Profile>()
    val profile: LiveData<Profile> = _profile

    private val _profileList: MutableLiveData<List<Profile>> = MutableLiveData<List<Profile>>()
    val profileList: LiveData<List<Profile>> = _profileList

    private val _isLeft: MutableLiveData<String> = MutableLiveData<String>()
    val isLeft: LiveData<String> = _isLeft

    private val _isLike: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isLike: LiveData<Boolean> = _isLike

    private val _confirm: MutableLiveData<Confirm> = MutableLiveData<Confirm>()
    val confirm: LiveData<Confirm> = _confirm

    fun getBalance(id: String) = viewModelScope.launch(exceptionHandler) {
        _balance.value = balanceDao.getBalance(id)
        _balance.value?.let {
            getProfile(it.name)
            getLikeState()
        }
    }

    private fun getProfile(name: String) = viewModelScope.launch(exceptionHandler) {
        _profile.value = profileDao.getProfile(name)
    }

    fun getProfileList() = viewModelScope.launch(exceptionHandler) {
        _profileList.value = profileDao.getProfileList()
    }

    fun getProfile2(name: String): Profile? {
        return profileList.value?.find { it.name == name }
    }

    fun deleteReply(name: String, replyId: String) = viewModelScope.launch(exceptionHandler) {
        _balance.value =
            balance.value?.let { service.deleteBalanceReply(name, it._id, replyId) }
        _balance.value?.let { balanceDao.updateBalance(it) }
    }

    fun postLike(balanceId: String, isLeft: Boolean) = viewModelScope.launch(exceptionHandler) {
        _balance.value = service.postBalanceLike(Preferences.userName, balanceId, isLeft)
        _balance.value?._id?.let {
            if (isLeft) {
                likeBoardDao.insertLikeBoard(LikeBoard(it, "true"))
                _isLeft.value = "true"
            } else {
                likeBoardDao.insertLikeBoard(LikeBoard(it, "false"))
                _isLeft.value = "false"
            }

            _isLike.value = true
        }
    }

    private fun getLikeState() = viewModelScope.launch(exceptionHandler) {
        balance.value?._id?.let {
            likeBoardDao.getLikeBoard(it)?.let {
                _isLike.value = true
            }
        }
    }

    fun postReply(name: String, boardId: String, content: String) =
        viewModelScope.launch(exceptionHandler) {
            _balance.value = service.postBalanceReply(name, boardId, content)
            _balance.value?.let { balanceDao.updateBalance(it) }
        }

    fun deleteBalance(balanceId: String) = viewModelScope.launch(exceptionHandler) {
        _confirm.value = service.deleteBalance(Preferences.userName, balanceId)
    }
}