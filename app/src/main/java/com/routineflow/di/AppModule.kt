package com.routineflow.di

import android.content.Context
import androidx.room.Room
import com.routineflow.data.local.RoutineDao
import com.routineflow.data.local.RoutineDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): RoutineDatabase =
        RoutineDatabase.getInstance(context)

    @Provides
    fun provideDao(database: RoutineDatabase): RoutineDao = database.routineDao()
}
