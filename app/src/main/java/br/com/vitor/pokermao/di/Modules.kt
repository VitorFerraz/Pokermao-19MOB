package br.com.vitor.pokermao.di

import android.content.Context
import br.com.vitor.pokermao.api.AuthInterceptor
import br.com.vitor.pokermao.api.PokemonService
import br.com.vitor.pokermao.repository.PokemonRepository
import br.com.vitor.pokermao.repository.PokemonRepositoryImpl
import br.com.vitor.pokermao.view.form.FormPokemonViewModel
import br.com.vitor.pokermao.view.list.ListPokemonsAdapter
import br.com.vitor.pokermao.view.list.ListPokemonsViewModel
import br.com.vitor.pokermao.view.splash.SplashViewModel
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val viewModule = module {
    factory { ListPokemonsAdapter(get())}
}

private fun createPicassoAuth(context: Context, okHttpClient: OkHttpClient): Picasso {
    return Picasso
        .Builder(context)
        .downloader(OkHttp3Downloader(okHttpClient))
        .build()
}

private fun createNetworkClient(okHttpClient: OkHttpClient, baseUrl: String): Retrofit {
    return Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
private fun createOkhttpClientAuth(authInterceptor: Interceptor): OkHttpClient {
    val builder = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addNetworkInterceptor(StethoInterceptor())
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
    return builder.build()
}

val viewModelModule = module {
    viewModel { ListPokemonsViewModel(get()) }
    viewModel { SplashViewModel(get()) }
    viewModel { FormPokemonViewModel(get()) }
}
val repositoryModule = module {
    single<PokemonRepository> { PokemonRepositoryImpl(get()) }
}
val networkModule = module {
    single { createPicassoAuth(get(), get()) }
    single<Interceptor> { AuthInterceptor() }
    single { createNetworkClient(get(), get(named("baseUrl"))).create(PokemonService::class.java) }
    single { createOkhttpClientAuth(get()) }
    single (named("baseUrl")) {"https://pokedexdx.herokuapp.com"}
}