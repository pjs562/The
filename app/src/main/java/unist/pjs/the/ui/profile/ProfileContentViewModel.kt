package unist.pjs.the.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import unist.pjs.the.BaseViewModel
import unist.pjs.the.data.UserInfo
import unist.pjs.the.repository.UpdateService
import javax.inject.Inject

@HiltViewModel
class ProfileContentViewModel @Inject constructor(
    private val service: UpdateService,
) : BaseViewModel() {
    private val _userInfo: MutableLiveData<UserInfo> = MutableLiveData<UserInfo>()
    val userInfo: LiveData<UserInfo> = _userInfo

    fun postUpdateContent(name: String, bio: String) =
        viewModelScope.launch(exceptionHandler) {
            _userInfo.value = service.postUpdateContent(name, bio)
        }

}