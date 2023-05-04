package unist.pjs.the.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import unist.pjs.the.R
import unist.pjs.the.data.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            context.getString(R.string.database)
        ).fallbackToDestructiveMigration().build()
    @Provides
    fun provideLikeBoardDao(appDatabase: AppDatabase): LikeBoardDao = appDatabase.likeBoardDao()

    @Provides
    fun provideProfileDao(appDatabase: AppDatabase): ProfileDao = appDatabase.profileDao()

    @Provides
    fun provideBulletinDao(appDatabase: AppDatabase): BulletinDao = appDatabase.bulletinDao()

    @Provides
    fun provideMatchDao(appDatabase: AppDatabase): MatchDao = appDatabase.matchDao()

    @Provides
    fun provideGroupNameDao(appDatabase: AppDatabase): GroupNameDao = appDatabase.groupNameDao()

    @Provides
    fun provideBalanceDao(appDatabase: AppDatabase): BalanceDao = appDatabase.balanceDao()

    @Provides
    fun roomDao(appDatabase: AppDatabase): RoomDao = appDatabase.roomDao()
}