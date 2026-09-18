package org.example.project.di

import org.example.project.presenter.home.HomeViewModel
import org.example.project.presenter.matchup.MatchUpViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::MatchUpViewModel)
}
