package unist.pjs.the.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import unist.pjs.the.BaseViewModel
import unist.pjs.the.data.UserInfo
import unist.pjs.the.repository.UpdateService
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileModifyViewModel @Inject constructor(
    private val service: UpdateService,
) : BaseViewModel() {
    private val _userInfo: MutableLiveData<UserInfo> = MutableLiveData<UserInfo>()
    val userInfo: LiveData<UserInfo> = _userInfo

    fun postUploadImage(name: String, file: File) =
        viewModelScope.launch(exceptionHandler) {
                val reqFile = RequestBody.create("image/jpg".toMediaTypeOrNull(), file)
                val body = MultipartBody.Part.createFormData(
                    "image",
                    file.name, reqFile
                )
                val nameBody = MultipartBody.Part.createFormData("name", name)
                _userInfo.value = service.postUploadImage(nameBody, body)
        }
}