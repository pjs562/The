package unist.pjs.the.ui.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.compose.rememberAsyncImagePainter
import dagger.hilt.android.AndroidEntryPoint
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import kotlinx.coroutines.*
import unist.pjs.the.R
import unist.pjs.the.databinding.FragmentProfileModifyBinding
import unist.pjs.the.extends.*
import unist.pjs.the.ui.theme.TheTheme

@AndroidEntryPoint
class ProfileModifyFragment : Fragment() {

    //Initialization of job
    private var job = Job()

    // Initialization of scope for the coroutine to run in
    private var cs = CoroutineScope(job + Dispatchers.Main)

    private lateinit var binding: FragmentProfileModifyBinding
    private val viewModel: ProfileModifyViewModel by viewModels()

    private val permissions = arrayOf(
        Manifest.permission.READ_MEDIA_IMAGES
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProfileModifyBinding.inflate(inflater, container, false)

        binding.tvContent.text = Preferences.userBio
        binding.tvInputLength.text = Preferences.userBio.length.toString() + "/500"
        binding.tvAge.text = Preferences.userAge
        binding.tvName.text = Preferences.userName
        binding.tvCellName.text = Preferences.userGroup
        binding.ivProfileImage.setContent {
            TheTheme {
                ProfileImage(imageUrl = Preferences.userImage)
            }
        }

        binding.mtb.apply {
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }

        binding.tvContent.setOnClickListener {
            val action =
                ProfileModifyFragmentDirections.actionProfileModifyFragmentToProfileContentFragment()
            findNavController().navigate(action)
        }

        initLiveDataObservers()
        return binding.root
    }

    private fun initLiveDataObservers() {
        viewModel.userInfo.observe(viewLifecycleOwner) {
            showToast("이미지가 업로드되었습니다.")
            Preferences.userImage = if (it.imageUrls?.isNotEmpty() == true) it.imageUrls[0] else ""
            findNavController().popBackStack()
        }
    }

    @Composable
    fun ProfileImage(imageUrl: String) {
        Surface(shape = AbsoluteRoundedCornerShape(40.dp),
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    requestPermission.launch(permissions)
                }) {
            Image(modifier = Modifier.fillMaxSize(),
                painter = rememberAsyncImagePainter(model = "https://api.theloveyouth.com/$imageUrl",
                    error = if (Preferences.userGender == "남자") painterResource(id = R.drawable.ic_men)
                    else painterResource(id = R.drawable.ic_girl)),
                contentDescription = null)
        }
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.also {
                    cs.launch(Dispatchers.Default) {
                        val bitmap = it.data?.let { it1 -> requireContext().getBitmap(it.data)
                            ?.let { it2 -> rotateImage(it1, it2) } }

                        val file = bitmap?.compress(createTempFile())
                        val compressedImageFile =
                            file?.let { it1 ->
                                Compressor.compress(requireContext(), it1) {
                                    default(width = 640, format = Bitmap.CompressFormat.JPEG)
                                }
                            }
                        launch {
                            if (file != null) {
                                if (compressedImageFile != null) {
                                    viewModel.postUploadImage(Preferences.userName,
                                        compressedImageFile)
                                }
                            } else {
                                showToast("이미지를 찾을 수 없습니다.")
                            }
                        }
                    }
                }
            }
        }

    private fun goGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startForResult.launch(intent)
    }

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            for (entry in it.entries) {
                if (!entry.value) {
                    showPermissionSnackBar(view, R.string.permission_read)
                    return@registerForActivityResult
                }
            }

            goGallery()
        }

    override fun onDestroy() {
        super.onDestroy()
        cs.cancel()
    }

    fun rotateImage(uri: Uri, bitmap: Bitmap): Bitmap {
        val inputStream = requireContext().contentResolver.openInputStream(uri)!!
        val exif = ExifInterface(inputStream)
        inputStream.close()

        val orientation =
            exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> {
                matrix.postRotate(90F)
            }
            ExifInterface.ORIENTATION_ROTATE_180 -> {
                matrix.postRotate(180F)
            }
            ExifInterface.ORIENTATION_ROTATE_270 -> {
                matrix.postRotate(270F)
            }
            else -> {}
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
}