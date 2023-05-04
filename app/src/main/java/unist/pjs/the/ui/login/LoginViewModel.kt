package unist.pjs.the.ui.login


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import unist.pjs.the.BaseViewModel
import unist.pjs.the.data.*
import unist.pjs.the.extends.Preferences
import unist.pjs.the.repository.LoginService
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val service: LoginService
) : BaseViewModel() {
    private val _userInfo: MutableLiveData<UserInfo> = MutableLiveData<UserInfo>()
    val userInfo: LiveData<UserInfo> = _userInfo

    private val _duplicate: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val duplicate: LiveData<Boolean> = _duplicate

    private val _response: MutableLiveData<String> = MutableLiveData<String>()
    val response: LiveData<String> = _response

    private val _check: MutableLiveData<Confirm> = MutableLiveData<Confirm>()
    val check: LiveData<Confirm> = _check

    private val _confirm: MutableLiveData<Confirm> = MutableLiveData<Confirm>()
    val confirm: LiveData<Confirm> = _confirm

    fun getCheck(name: String, phone: String) =
        viewModelScope.launch(exceptionHandler) {
            _check.value = service.getCheck(name, phone)
        }

    fun getSendAuth(name: String, phone: String) =
        viewModelScope.launch(exceptionHandler) {
            _response.value = service.getSendAuth(name, phone)
        }

    fun postConfirm(requestAuth: RequestAuth) =
        viewModelScope.launch(exceptionHandler) {
            _userInfo.value = service.postConfirm(requestAuth.name, requestAuth.auth_no)
        }

    fun checkName(name: String) =
        viewModelScope.launch(exceptionHandler) {
            val check = service.checkName(name)
            _duplicate.value = check.ok.isEmpty()
        }

    fun postToken(id: String, token: String) =
        viewModelScope.launch(exceptionHandler) {
            _confirm.value = service.postToken(id, token)
        }
}