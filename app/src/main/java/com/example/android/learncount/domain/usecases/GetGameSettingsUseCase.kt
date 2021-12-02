package com.example.android.learncount.domain.usecases

import com.example.android.learncount.domain.entity.GameSettings
import com.example.android.learncount.domain.entity.Level
import com.example.android.learncount.domain.repository.GameRepository

class GetGameSettingsUseCase(private val repository: GameRepository) {

    operator fun invoke(level: Level): GameSettings {
        return repository.getGameSettings(level)
    }

}