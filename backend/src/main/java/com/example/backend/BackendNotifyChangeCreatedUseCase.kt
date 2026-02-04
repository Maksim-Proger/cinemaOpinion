package com.example.backend

class BackendNotifyChangeCreatedUseCase {
    suspend operator fun invoke(userId: String, changeId: String) {
        try {
            BackendApiProvider.api.notifyChangeCreated(
                ChangeCreatedRequest(
                    userId = userId,
                    changeId = changeId
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            // Здесь можно добавить обработку ошибок (например, через Result)
        }
    }
}
