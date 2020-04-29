package com.bradrodgers.xkcdviewer

import com.bradrodgers.xkcdviewer.api.XkcdApi
import com.bradrodgers.xkcdviewer.doa.ComicInfoDoa
import com.bradrodgers.xkcdviewer.homepage.HomepageViewModel
import com.bradrodgers.xkcdviewer.repos.ComicRepo
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val homePageViewModelModule = module {
    viewModel {
        HomepageViewModel(get())
    }
}

val repoModule = module {
    single {
        ComicRepo(get(), get())
    }
}

val daoModule = module {
    single {
        ComicInfoDoa::class.java
    }
}

val apiModule = module {
    fun provideApi(retrofit: Retrofit): XkcdApi {
        return retrofit.create(XkcdApi::class.java)
    }

    single { provideApi(get()) }
}

val retrofitModule = module {

    fun provideMoshi(): MoshiConverterFactory{
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        return MoshiConverterFactory.create(moshi)
    }

    fun provideOkHttpClient(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
        return okHttpClientBuilder.build()
    }

    fun provideRetrofit(moshiConverterFactory: MoshiConverterFactory, okHttpClient: OkHttpClient): Retrofit {

        return Retrofit.Builder()
            .baseUrl("http://xkcd.com/")
            .addConverterFactory(moshiConverterFactory)
            .client(okHttpClient)
            .build()
    }

    single { provideMoshi() }
    single { provideOkHttpClient() }
    single { provideRetrofit(get(), get()) }
}