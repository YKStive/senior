package com.youloft.senior.di

import com.youloft.senior.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * @author you
 * @create 2020/6/18
 * @desc model统一注入
 */
val viewModels = module {
    viewModel { MainViewModel() }
}