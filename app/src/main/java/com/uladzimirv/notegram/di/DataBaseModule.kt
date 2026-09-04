package com.uladzimirv.notegram.di

import android.content.Context
import androidx.room.Room
import com.uladzimirv.notegram.data.database.NotegramDataBase
import com.uladzimirv.notegram.util.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {

    @Provides
    @Singleton
    fun getRoomDataBase(
        @ApplicationContext context: Context
    ): NotegramDataBase {
        return Room.databaseBuilder(
            context = context,
            klass = NotegramDataBase::class.java,
            name = DATABASE_NAME
        )
            .fallbackToDestructiveMigration(true)
            .build()
    }

    @Provides
    @Singleton
    fun providesTextNotesDao(database: NotegramDataBase) = database.textNotesDao()

    @Provides
    @Singleton
    fun providesTodoNotesDao(database: NotegramDataBase) = database.todoNotesDao()

    @Provides
    @Singleton
    fun providesLabelDao(database: NotegramDataBase) = database.labelsDao()
}