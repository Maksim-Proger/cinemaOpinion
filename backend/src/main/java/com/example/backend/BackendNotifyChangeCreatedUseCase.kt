package com.example.backend

import android.util.Log

class BackendNotifyChangeCreatedUseCase {
    suspend operator fun invoke(userId: String, changeId: String): Result<Unit> =
        runCatching {
            BackendApiProvider.api.notifyChangeCreated(
                ChangeCreatedRequest(
                    userId = userId,
                    changeId = changeId
                )
            )
        }.onFailure { e ->
            Log.e("BackendNotify", "Failed to notify change $changeId", e)
        }
}
