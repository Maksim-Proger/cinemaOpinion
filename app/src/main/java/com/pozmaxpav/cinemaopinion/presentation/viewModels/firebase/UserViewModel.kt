package com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.annotation.ExperimentalCoilApi
import coil.imageLoader
import coil.memory.MemoryCache
import com.example.backend.BackendApiProvider
import com.example.backend.BackendUploadAvatarUseCase
import com.example.core.domain.DomainUserModel
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.user.GetUserDataUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.user.GetUsersUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.user.UpdateSpecificFieldUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.user.UpdatingUserDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

enum class AvatarUploadError { TOO_LARGE, INVALID_FILE, NETWORK, UNKNOWN }

private const val MAX_AVATAR_SIZE_BYTES = 5 * 1024 * 1024

@HiltViewModel
class UserViewModel @Inject constructor(
    private val updatingUserDataUseCase: UpdatingUserDataUseCase,
    private val updateSpecificFieldUseCase: UpdateSpecificFieldUseCase,
    private val getUsersUseCase: GetUsersUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val uploadAvatarUseCase: BackendUploadAvatarUseCase,
    @ApplicationContext private val context: Context,
    ) : ViewModel() {

    private val _userData = MutableStateFlow<DomainUserModel?>(null)
    val userData = _userData.asStateFlow()

    private val _avatarPreviewUri = MutableStateFlow<Uri?>(null)
    val avatarPreviewUri = _avatarPreviewUri.asStateFlow()

    private val _isUploadingAvatar = MutableStateFlow(false)
    val isUploadingAvatar = _isUploadingAvatar.asStateFlow()

    private val _avatarUploadError = MutableSharedFlow<AvatarUploadError>()
    val avatarUploadError = _avatarUploadError.asSharedFlow()

    private val _seasonalEventPoints = MutableStateFlow(0L)
    val seasonalEventPoints = _seasonalEventPoints.asStateFlow()

    private val _listAwards = MutableStateFlow("")
    val listAwards = _listAwards.asStateFlow()

    fun getUserData(userId: String) {
        viewModelScope.launch {
            try {
                val userData = getUserDataUseCase(userId)
                _userData.value = userData
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun updatingUserData(userId: String, nikName: String, email: String, password: String) {
        if (userId.isBlank() || userId == "Unknown") return
        viewModelScope.launch {
            try {
                val newUser = DomainUserModel(
                    userId,
                    nikName,
                    email,
                    password
                )
                updatingUserDataUseCase(newUser)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun getSeasonalEventPoints(userId: String) {
        viewModelScope.launch {
            try {
                val userData = getUserDataUseCase(userId)
                userData?.let { _seasonalEventPoints.value = it.seasonalEventPoints }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun getAwardsList(userId: String) {
        viewModelScope.launch {
            try {
                val userData = getUserDataUseCase(userId)
                userData?.let { _listAwards.value = it.awards }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    private fun updateAwardsList(userId: String, newAward: String) {
        viewModelScope.launch {
            try {
                val userData = getUserDataUseCase(userId)
                userData?.let {
                    val currentAwards = it.awards

                    // Проверяем, есть ли уже такая награда в списке
                    if (currentAwards.contains(newAward)) {
                        return@launch
                    }

                    val updatedAwards = if (currentAwards.isEmpty()) newAward
                    else "$currentAwards,$newAward"

                    updateSpecificField(userId, "awards", updatedAwards)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun updatingEventData(userId: String) {
        viewModelScope.launch {
            var points = _seasonalEventPoints.value

            if (_seasonalEventPoints.value < 80L) {
                points += 20L
                _seasonalEventPoints.value = points
                updateSpecificField(userId, "seasonalEventPoints", points)
            }

            if (points == 40L) {
                updateAwardsList(userId, R.drawable.half_done.toString())
            }

            if (points == 80L) {
                updateAwardsList(userId, R.drawable.complete_passage.toString())
            }
        }
    }
    private fun updateSpecificField(userId: String, fieldName: String, newValue: Any) {
        viewModelScope.launch {
            try {
                updateSpecificFieldUseCase(userId, fieldName, newValue)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun uploadAvatar(userId: String, uri: Uri) {
        viewModelScope.launch {
            _isUploadingAvatar.value = true
            _avatarPreviewUri.value = uri
            try {
                val resolver = context.contentResolver
                val mimeType = resolver.getType(uri) ?: "image/jpeg"
                val bytes = withContext(Dispatchers.IO) {
                    resolver.openInputStream(uri)?.use { it.readBytes() }
                }

                if (bytes == null) {
                    _avatarPreviewUri.value = null
                    _avatarUploadError.emit(AvatarUploadError.UNKNOWN)
                    return@launch
                }
                if (bytes.size > MAX_AVATAR_SIZE_BYTES) {
                    _avatarPreviewUri.value = null
                    _avatarUploadError.emit(AvatarUploadError.TOO_LARGE)
                    return@launch
                }

                uploadAvatarUseCase(userId, bytes, mimeType, "avatar.jpg")
                    .onSuccess { evictAvatarCache(userId) }
                    .onFailure { e ->
                        _avatarPreviewUri.value = null
                        _avatarUploadError.emit(mapUploadError(e))
                    }
            } catch (e: Exception) {
                _avatarPreviewUri.value = null
                _avatarUploadError.emit(AvatarUploadError.UNKNOWN)
            } finally {
                _isUploadingAvatar.value = false
            }
        }
    }
    @OptIn(ExperimentalCoilApi::class)
    private fun evictAvatarCache(userId: String) {
        val url = BackendApiProvider.avatarUrl(userId)
        val imageLoader = context.imageLoader
        imageLoader.memoryCache?.remove(MemoryCache.Key(url))
        imageLoader.diskCache?.remove(url)
    }
    private fun mapUploadError(e: Throwable): AvatarUploadError {
        return when (e) {
            is HttpException -> {
                val detail = e.response()?.errorBody()?.string().orEmpty()
                when {
                    detail.contains("too large", ignoreCase = true) -> AvatarUploadError.TOO_LARGE
                    detail.contains("Invalid image", ignoreCase = true) -> AvatarUploadError.INVALID_FILE
                    else -> AvatarUploadError.UNKNOWN
                }
            }
            is IOException -> AvatarUploadError.NETWORK
            else -> AvatarUploadError.UNKNOWN
        }
    }

}