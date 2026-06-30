package com.example.backend

import android.util.Log
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class BackendUploadAvatarUseCase {
    suspend operator fun invoke(
        userId: String,
        fileBytes: ByteArray,
        mimeType: String,
        fileName: String
    ): Result<UploadAvatarResponse> =
        runCatching {
            val filePart = MultipartBody.Part.createFormData(
                name = "file",
                filename = fileName,
                body = fileBytes.toRequestBody(mimeType.toMediaTypeOrNull())
            )
            val userIdPart = userId.toRequestBody("text/plain".toMediaTypeOrNull())
            BackendApiProvider.api.uploadAvatar(userIdPart, filePart)
        }.onFailure { e ->
            Log.e("BackendUploadAvatar", "Failed to upload avatar", e)
        }
}
