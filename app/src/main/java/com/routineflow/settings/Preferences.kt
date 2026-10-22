package com.routineflow.settings

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.preferencesDataStore by preferencesDataStore("routineflow_preferences")

class Preferences @Inject constructor(@ApplicationContext private val context: Context) {
    private val reducedMotionKey = booleanPreferencesKey("reduced_motion")
    private val onboardingCompletedKey = booleanPreferencesKey("onboarding_completed")
    private val onboardingCategoriesKey = stringSetPreferencesKey("onboarding_categories")
    val reducedMotion: Flow<Boolean> = context.preferencesDataStore.data.map { it[reducedMotionKey] ?: false }
    val onboardingCompleted: Flow<Boolean> =
        context.preferencesDataStore.data.map { it[onboardingCompletedKey] ?: false }

    suspend fun setReducedMotion(enabled: Boolean) {
        context.preferencesDataStore.edit { it[reducedMotionKey] = enabled }
    }

    suspend fun completeOnboarding(categories: Set<String>) {
        context.preferencesDataStore.edit {
            it[onboardingCompletedKey] = true
            it[onboardingCategoriesKey] = categories
        }
    }
}
