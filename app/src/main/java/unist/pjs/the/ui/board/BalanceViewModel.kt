package unist.pjs.the.ui.board

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import unist.pjs.the.BaseViewModel
import unist.pjs.the.data.Balance
import unist.pjs.the.data.BalanceDao
import unist.pjs.the.repository.BoardService
import javax.inject.Inject

@HiltViewModel
class BalanceViewModel @Inject constructor(
    private val service: BoardService,
    private val balanceDao: BalanceDao
): BaseViewModel() {
    val balanceList = balanceDao.getBalanceList().asLiveData()
    var count = 20
    lateinit var list: List<Balance>

    fun deleteBalance() = viewModelScope.launch(exceptionHandler) {
        balanceDao.deleteAll()
    }

    fun getBalanceList(lastId: String) = viewModelScope.launch(exceptionHandler) {
        if(count == 20){
            list = service.getBalance(lastId).balances
            count = list.size
            balanceDao.insertAllBalance(list)
        }
    }
}