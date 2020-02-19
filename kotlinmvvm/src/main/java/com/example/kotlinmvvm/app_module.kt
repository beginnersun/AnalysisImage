package com.example.kotlinmvvm

import androidx.room.Room
import com.example.base_module.util.LenientGsonConverterFactory
import com.example.kotlinmvvm.dao.NewsDao
import com.example.kotlinmvvm.dao.NewsDatabase
import com.example.kotlinmvvm.interceptor.CustomInterceptor
import com.example.kotlinmvvm.model.*
import com.example.kotlinmvvm.vm.MainViewModel
import com.example.kotlinmvvm.vm.NewsViewModel
import com.example.kotlinmvvm.vm.StzbViewModel
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
import retrofit2.converter.scalars.ScalarsConverterFactory

/**
 * 初始化需要的ViewModel
 */
val mainViewModel = module {
    single(named("main")) {
        MainViewModel(UserRepository(get()))
    }
    single(named("news")) {
        NewsViewModel(NewsRepository(get()))
    }
    single(named("stzb")) {
        StzbViewModel(StzbResponsitory(get()))
    }
}
val newsViewModel = module {
}
/**
 * 初始化远程连接model
 */
val remoteModule = module {
    single<Retrofit>(named("news_retrofit")) {
        Retrofit.Builder()
            .baseUrl("https://3g.163.com/touch/reconstruct/")
            .client(OkHttpClient.Builder().apply {
                addInterceptor(CustomInterceptor())
            }.build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(LenientGsonConverterFactory.create(Gson()))
            .build()
    }
    single<Retrofit>(named("stzb_retrofit"))
    {
        Retrofit.Builder()
            .baseUrl("https://mshare.cc.163.com/")
            .client(OkHttpClient.Builder().apply {
                addInterceptor(CustomInterceptor())
            }.build())
            .addConverterFactory(ScalarsConverterFactory.create())
//            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//            .addConverterFactory(LenientGsonConverterFactory.create(Gson()))
            .build()
    }
    single<UserService> {
        get<Retrofit>(
        named("news_retrofit")
        ).create(UserService::class.java)
    }
    single<NewsService> {
        get<Retrofit>(
        named("news_retrofit")
        ).create(NewsService::class.java)
    }
    single<StzbService> {
        get<Retrofit>(
        named("stzb_retrofit")
        ).create(StzbService::class.java)
    }
}

val localModule = module {
    //    single { Room.databaseBuilder(androidApplication().applicationContext,NewsDatabase::class.java,"news.db").build().newsDao()}
}