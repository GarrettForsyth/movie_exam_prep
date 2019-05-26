package com.example.android.movieexamprep.di;

import com.example.android.movieexamprep.api.MovieService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public abstract class MovieServiceModule {
    @Provides
    @Singleton
    static HttpLoggingInterceptor provideLogger() {
        HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
        logger.setLevel(HttpLoggingInterceptor.Level.BASIC);
        return logger;
    }

    @Provides
    @Singleton
    static Interceptor provideInterceptor() {
        return chain -> {
            Request original = chain.request();
            HttpUrl originalHttpUrl = original.url();

            HttpUrl url = originalHttpUrl.newBuilder()
                    .addQueryParameter("api_key", "5fd4ff6a99131af91a95cc335ddbf7b6")
                    .build();

            // Request customization: add request headers
            Request.Builder requestBuilder = original.newBuilder()
                    .url(url);

            Request request = requestBuilder.build();
            return chain.proceed(request);
        };

    }

    @Provides
    @Singleton
    static OkHttpClient provideClient(Interceptor apiKey, HttpLoggingInterceptor logger) {
        return new OkHttpClient.Builder()
                .addInterceptor(logger)
                .addInterceptor(apiKey)
                .build();

    }

    @Provides
    @Singleton
    static MovieService provideMovieService(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MovieService.class);
    }
}
