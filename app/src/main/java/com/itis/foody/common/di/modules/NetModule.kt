package com.itis.foody.common.di.modules

import com.itis.foody.BuildConfig
import com.itis.foody.common.di.modules.qualifiers.ApiKey
import com.itis.foody.common.di.modules.qualifiers.Logger
import com.itis.foody.features.recipe.data.api.Api
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.spoonacular.com/recipes/"
private const val QUERY_API_KEY = "apiKey"
private const val API_KEY = "61a5d2ee34a3498e90607b5291bcb354"

@Module
@InstallIn(SingletonComponent::class)
class NetModule {

    @Provides
    @ApiKey
    fun providesApiKeyInterceptor() = Interceptor { chain ->
        val original = chain.request()
        val newURL = original.url.newBuilder()
            .addQueryParameter(QUERY_API_KEY, API_KEY)
            .build()

        chain.proceed(
            original.newBuilder()
                .url(newURL)
                .build()
        )
    }

    @Provides
    @Logger
    fun provideLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor()
            .setLevel(
                HttpLoggingInterceptor.Level.BODY
            )
    }

    @Provides
    fun providesOkhttp(
        @ApiKey apiKeyInterceptor: Interceptor,
        @Logger loggingInterceptor: Interceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .also {
                if (BuildConfig.DEBUG) {
                    it.addInterceptor(loggingInterceptor)
                }
            }
            .build()

    @Provides
    fun provideGsonConverter(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    fun providesApi(
        okHttpClient: OkHttpClient,
        gsonConverter: GsonConverterFactory
    ): Api =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverter)
            .build()
            .create(Api::class.java)
}
