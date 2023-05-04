package unist.pjs.the.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import unist.pjs.the.BaseViewModel
import unist.pjs.the.data.*
import unist.pjs.the.repository.GroupService
import javax.inject.Inject

@HiltViewModel
class SignupFragment2ViewModel @Inject constructor(
    private val service: GroupService
) : BaseViewModel() {
    private val _cellList: MutableLiveData<List<GroupName>> = MutableLiveData<List<GroupName>>()
    val cellList: LiveData<List<GroupName>> = _cellList

    private val _group: MutableLiveData<String> = MutableLiveData<String>()
    val group: LiveData<String> = _group

    private val _userInfo: MutableLiveData<UserInfo> = MutableLiveData<UserInfo>()
    val userInfo: LiveData<UserInfo> = _userInfo

    private val _confirm: MutableLiveData<Confirm> = MutableLiveData<Confirm>()
    val confirm: LiveData<Confirm> = _confirm

    fun getCellList() =
        viewModelScope.launch(exceptionHandler) {
            _cellList.value = service.getGroupList().groups
        }

    fun setGroup(group: String) {
        _group.value = group
    }

    fun postRegister(
        name: String, birth: String, gender: String, phone: String, group: String, auth_no: String,
    ) = viewModelScope.launch (exceptionHandler) {
            _userInfo.value = service.postRegister(name, birth, gender, phone, group, auth_no)
        }

    fun postToken(id: String, token: String) =
        viewModelScope.launch(exceptionHandler) {
            _confirm.value = service.postToken(id, token)
        }
}