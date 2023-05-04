package unist.pjs.the.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import unist.pjs.the.repository.*
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Singleton
    @Provides
    fun provideLoginService(): LoginService = LoginService.create()

    @Singleton
    @Provides
    fun providePersonService(): PersonService = PersonService.create()

    @Singleton
    @Provides
    fun provideGroupService(): GroupService = GroupService.create()

    @Singleton
    @Provides
    fun provideUserInfoService(): UserInfoService = UserInfoService.create()

    @Singleton
    @Provides
    fun provideLikeService(): LikeService = LikeService.create()

    @Singleton
    @Provides
    fun provideUpdateService(): UpdateService = UpdateService.create()

    @Singleton
    @Provides
    fun provideBoardService(): BoardService = BoardService.create()
}