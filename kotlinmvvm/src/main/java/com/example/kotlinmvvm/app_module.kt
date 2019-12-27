package com.example.kotlinmvvm

import com.example.kotlinmvvm.model.NewsRepository
import com.example.kotlinmvvm.model.NewsService
import com.example.kotlinmvvm.model.UserRepository
import com.example.kotlinmvvm.model.UserService
import com.example.kotlinmvvm.vm.MainViewModel
import com.example.kotlinmvvm.vm.NewsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 初始化需要的ViewModel
 */
val mainViewModel = module {
      viewModel {
          MainViewModel(UserRepository(get()))
      }
}
val newsViewModel = module{
    viewModel {
        NewsViewModel(NewsRepository(get()))
    }
}
/**
 * 初始化远程连接model
 */
val remoteModule = module {
    single<Retrofit>{Retrofit.Builder().baseUrl("https://3g.163.com/touch/reconstruct/article/").addCallAdapterFactory(RxJava2CallAdapterFactory.create()).addConverterFactory(GsonConverterFactory.create()).build()}
    single<UserService>{ get<Retrofit>().create(UserService::class.java) }
    single<NewsService>{ get<Retrofit>().create(NewsService::class.java)}
}