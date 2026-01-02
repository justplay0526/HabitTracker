package com.justplay.data.di

import com.justplay.data.db.repo.TaskRepo
import com.justplay.data.db.repo.TaskRepoImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {
    @Binds
    abstract fun bindTaskRepo(impl: TaskRepoImpl): TaskRepo
}