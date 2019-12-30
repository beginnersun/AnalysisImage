package com.example.kotlinmvvm

import androidx.room.Room
import com.example.base_module.util.LenientGsonConverterFactory
import com.example.kotlinmvvm.dao.NewsDao
import com.example.kotlinmvvm.dao.NewsDatabase
import com.example.kotlinmvvm.interceptor.CustomInterceptor
import com.example.kotlinmvvm.model.NewsRepository
import com.example.kotlinmvvm.model.NewsService
import com.example.kotlinmvvm.model.UserRepository
import com.example.kotlinmvvm.model.UserService
import com.example.kotlinmvvm.vm.MainViewModel
import com.example.kotlinmvvm.vm.NewsViewModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
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
val newsViewModel = module {
    single(named("news")) {
        NewsViewModel(NewsRepository(get()),get())
    }
}
/**
 * 初始化远程连接model
 */
val remoteModule = module {
    single<Retrofit> {
        Retrofit.Builder().baseUrl("https://3g.163.com/touch/reconstruct/article/")
            .client(OkHttpClient.Builder().apply {
                addInterceptor(CustomInterceptor())
            }.build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(LenientGsonConverterFactory.create(Gson()))
            .build()
    }
    single<UserService> { get<Retrofit>().create(UserService::class.java) }
    single<NewsService> { get<Retrofit>().create(NewsService::class.java) }
}

val localModule = module{
    single { Room.databaseBuilder(androidApplication().applicationContext,NewsDatabase::class.java,"news.db").build().newsDao()}
}