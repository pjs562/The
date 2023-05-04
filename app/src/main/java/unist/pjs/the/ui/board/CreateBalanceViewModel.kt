package unist.pjs.the.ui.board

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import unist.pjs.the.BaseViewModel
import unist.pjs.the.data.Balance
import unist.pjs.the.data.BalanceDao
import unist.pjs.the.data.Bulletin
import unist.pjs.the.extends.Preferences
import unist.pjs.the.repository.BoardService
import javax.inject.Inject

@HiltViewModel
class CreateBalanceViewModel @Inject constructor(
    private val service: BoardService,
    private val balanceDao: BalanceDao
): BaseViewModel() {
    private val _balance: MutableLiveData<Balance> = MutableLiveData<Balance>()
    val balance: LiveData<Balance> = _balance

    private val _balance2: MutableLiveData<Balance> = MutableLiveData<Balance>()
    val balance2: LiveData<Balance> = _balance2

    fun postBalance(left: String, right: String) = viewModelScope.launch(exceptionHandler) {
        _balance.value = service.postBalance(Preferences.userName, left, right)
    }

    fun putBalance(id: String, left: String, right: String) = viewModelScope.launch(exceptionHandler) {
        _balance.value = service.putBalance(Preferences.userName, id, left, right)
    }

    fun upDateBalance(balance: Balance) = viewModelScope.launch(exceptionHandler) {
        balanceDao.updateBalance(balance)
    }

    fun getBalance(id: String) = viewModelScope.launch(exceptionHandler) {
        _balance2.value = balanceDao.getBalance(id)
    }
}