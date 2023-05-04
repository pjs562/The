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
class BoardDetailViewModel @Inject constructor(
    private val bulletinDao: BulletinDao,
    private val boardService: BoardService,
    private val likeBoardDao: LikeBoardDao,
    private val profileDao: ProfileDao
) : BaseViewModel() {
    private val _bulletin: MutableLiveData<Bulletin> = MutableLiveData<Bulletin>()
    val bulletin: LiveData<Bulletin> = _bulletin

    private val _profile: MutableLiveData<Profile> = MutableLiveData<Profile>()
    val profile: LiveData<Profile> = _profile

    private val _profileList: MutableLiveData<List<Profile>> = MutableLiveData<List<Profile>>()
    val profileList: LiveData<List<Profile>> = _profileList

    private val _confirm: MutableLiveData<Confirm> = MutableLiveData<Confirm>()
    val confirm: LiveData<Confirm> = _confirm

    private val _type: MutableLiveData<String> = MutableLiveData<String>()
    val type: LiveData<String> = _type

    private val _isLike: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isLike: LiveData<Boolean> = _isLike

    fun getBulletin(id: String) = viewModelScope.launch(exceptionHandler) {
        _bulletin.value = bulletinDao.getBulletin(id)
        _bulletin.value?.let {
            getProfile(it.name)
            getLikeState()
        }
    }

    private fun getLikeState() = viewModelScope.launch(exceptionHandler) {
        bulletin.value?._id?.let {
            likeBoardDao.getLikeBoard(it)?.let {
                _isLike.value = true
            }
        }
    }
    fun getProfileList() = viewModelScope.launch(exceptionHandler) {
        _profileList.value = profileDao.getProfileList()
    }

    private fun getProfile(name: String) = viewModelScope.launch(exceptionHandler) {
        _profile.value = profileDao.getProfile(name)
    }

    fun deleteBoard(boardId: String) = viewModelScope.launch(exceptionHandler) {
        when (type.value) {
            "notice" -> {
                _confirm.value = boardService.deleteNotice(Preferences.userName, boardId)
            }

            "free" -> {
                _confirm.value = boardService.deleteFree(Preferences.userName, boardId)
            }

            "pray" -> {
                _confirm.value = boardService.deletePray(Preferences.userName, boardId)
            }

            "qt" -> {
                _confirm.value = boardService.deleteQuitetime(Preferences.userName, boardId)
            }
        }
    }

    fun postLike(boardId: String) = viewModelScope.launch(exceptionHandler) {
        when (type.value) {
            "notice" -> {
                _bulletin.value = boardService.postNoticeLike(Preferences.userName, boardId)
                _bulletin.value?._id?.let { LikeBoard(it) }
                    ?.let {
                        likeBoardDao.insertLikeBoard(it)
                        _isLike.value = true
                    }
            }

            "free" -> {
                _bulletin.value = boardService.postFreeLike(Preferences.userName, boardId)
                _bulletin.value?._id?.let { LikeBoard(it) }
                    ?.let {
                        likeBoardDao.insertLikeBoard(it)
                        _isLike.value = true
                    }
            }

            "pray" -> {
                _bulletin.value = boardService.postPrayLike(Preferences.userName, boardId)
                _bulletin.value?._id?.let { LikeBoard(it) }
                    ?.let {
                        likeBoardDao.insertLikeBoard(it)
                        _isLike.value = true
                    }
            }

            "qt" -> {
                _bulletin.value = boardService.postQuitetimeLike(Preferences.userName, boardId)
                _bulletin.value?._id?.let { LikeBoard(it) }
                    ?.let {
                        likeBoardDao.insertLikeBoard(it)
                        _isLike.value = true
                    }
            }
        }
    }

    fun postReply(name: String, boardId: String, content: String) =
        viewModelScope.launch(exceptionHandler) {
            when (type.value) {
                "notice" -> {
                    _bulletin.value = boardService.postNoticeReply(name, boardId, content)
                    _bulletin.value?.let { bulletinDao.updateBulletin(it) }
                }

                "free" -> {
                    _bulletin.value = boardService.postFreeReply(name, boardId, content)
                    _bulletin.value?.let { bulletinDao.updateBulletin(it) }
                }

                "pray" -> {
                    _bulletin.value = boardService.postPrayReply(name, boardId, content)
                    _bulletin.value?.let { bulletinDao.updateBulletin(it) }
                }

                "qt" -> {
                    _bulletin.value = boardService.postQuitetimeReply(name, boardId, content)
                    _bulletin.value?.let { bulletinDao.updateBulletin(it) }
                }
            }
        }

    fun deleteReply(name: String, replyId: String) = viewModelScope.launch(exceptionHandler) {
        when (type.value) {
            "notice" -> {
                _bulletin.value =
                    bulletin.value?.let { boardService.deleteNoticeReply(name, it._id, replyId) }
                _bulletin.value?.let { bulletinDao.updateBulletin(it) }
            }

            "free" -> {
                _bulletin.value =
                    bulletin.value?.let { boardService.deleteFreeReply(name, it._id, replyId) }
                _bulletin.value?.let { bulletinDao.updateBulletin(it) }
            }

            "pray" -> {
                _bulletin.value =
                    bulletin.value?.let { boardService.deletePrayReplay(name, it._id, replyId) }
                _bulletin.value?.let { bulletinDao.updateBulletin(it) }
            }

            "qt" -> {
                _bulletin.value =
                    bulletin.value?.let { boardService.deleteQuitetimeReplay(name, it._id, replyId) }
                _bulletin.value?.let { bulletinDao.updateBulletin(it) }
            }
        }
    }

    fun setType(type: String) {
        _type.value = type
    }
}