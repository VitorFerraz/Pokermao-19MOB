package br.com.vitor.pokermao.view.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.vitor.pokermao.model.Pokemon
import br.com.vitor.pokermao.repository.PokemonRepository
import br.com.vitor.pokermao.view.ViewState


class DetailViewModel(
    val pokemonRepository: PokemonRepository
) : ViewModel() {
    val viewState: MutableLiveData<ViewState<Pokemon?>> = MutableLiveData()

    fun getPokemon(number: String) {
        viewState.value = ViewState.Loading
        pokemonRepository.getPokemon(
            number, onComplete = {
                viewState.value = ViewState.Success(it)
            },
            onError = {
                viewState.value = ViewState.Failed(it)
            } )
    } }
