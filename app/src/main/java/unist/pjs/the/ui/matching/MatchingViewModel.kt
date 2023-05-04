package unist.pjs.the.ui.matching

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import unist.pjs.the.BaseViewModel
import unist.pjs.the.data.*
import unist.pjs.the.repository.GroupService
import unist.pjs.the.repository.PersonService
import javax.inject.Inject

@HiltViewModel
class MatchingViewModel @Inject constructor(
    private val groupNameDao: GroupNameDao,
    private val service: PersonService,
    private val groupService: GroupService,
    private val profileDao: ProfileDao
) : BaseViewModel() {
    val groupNameList = groupNameDao.getGroupNameList().asLiveData()

    private val _cellList: MutableLiveData<List<GroupName>> = MutableLiveData<List<GroupName>>()
    private val cellList: LiveData<List<GroupName>> = _cellList

    private val _type: MutableLiveData<Boolean> = MutableLiveData(true)
    val type: LiveData<Boolean> = _type

    private val _position: MutableLiveData<Int> = MutableLiveData(0)
    val position: LiveData<Int> = _position

    private val _titleIndexList: MutableLiveData<List<Int>> = MutableLiveData()
    val titleIndexList: LiveData<List<Int>> = _titleIndexList

    private val _groupList: MutableLiveData<List<Group>> = MutableLiveData<List<Group>>()
    val groupList: LiveData<List<Group>> = _groupList

    private val _ageList: MutableLiveData<List<Group>> = MutableLiveData<List<Group>>()
    private val ageList: LiveData<List<Group>> = _ageList

    private val _cellNameList: MutableLiveData<List<Group>> = MutableLiveData<List<Group>>()
    private val cellNameList: LiveData<List<Group>> = _cellNameList

    init {
        viewModelScope.launch {
            profileDao.deleteAll()
        }
        getCellList()
    }

    private fun getCellList() =
        viewModelScope.launch(exceptionHandler) {
            _cellList.value = groupService.getGroupList().groups
        }

    private fun insertAllGroupName(items: List<GroupName>) {
        viewModelScope.launch {
            groupNameDao.insertAllGroupName(items)
        }
    }

    private fun deleteAllGroupName() {
        viewModelScope.launch {
            groupNameDao.deleteAllGroupName()
        }
    }

    fun changePosition(index: Int) {
        _position.value = index
    }

    fun setTitleIndexList(list: List<Int>) {
        _titleIndexList.value = list
    }

    fun onTypeChanged(bool: Boolean) {
        _type.value = bool
        deleteAllGroupName()

        val items: List<GroupName>? = if (bool) {
            _groupList.value = ageList.value

            listOf(
                GroupName("1", "20 - 22"),
                GroupName("2", "23 - 26"),
                GroupName("3", "27 - 29"),
                GroupName("4", "30 -")
            )
        } else {
            _groupList.value = cellNameList.value
            cellList.value
        }

        if (items != null) {
            insertAllGroupName(items)
        }
    }

    fun initGetPeoples() = viewModelScope.launch(exceptionHandler) {
        _ageList.value = service.getPerson("age").peoples
        _cellNameList.value = service.getPerson("group").peoples
        _groupList.value = _ageList.value
        val array = ArrayList<Profile>()
        _ageList.value.let {
            if (it != null) {
                for (item in it) {
                    item.members?.let { it1 -> array.addAll(it1) }
                }
            }
        }
        profileDao.insertProfiles(array)
    }

    fun initPeoples2(type: Boolean) = viewModelScope.launch(exceptionHandler) {
        if (type) {
            _ageList.value = service.getPerson("age").peoples
            _groupList.value = _ageList.value
        } else {
            _cellNameList.value = service.getPerson("group").peoples
            _groupList.value = _cellNameList.value
        }

        val array = ArrayList<Profile>()
        _ageList.value.let {
            if (it != null) {
                for (item in it) {
                    item.members?.let { it1 -> array.addAll(it1) }
                }
            }
        }

        profileDao.insertProfiles(array)
    }
}