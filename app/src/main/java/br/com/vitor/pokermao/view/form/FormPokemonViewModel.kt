package br.com.vitor.pokermao.view.form

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.vitor.pokermao.model.Pokemon
import br.com.vitor.pokermao.repository.PokemonRepository
import br.com.vitor.pokermao.view.ViewState

class FormPokemonViewModel(
    val pokemonRepository: PokemonRepository
) : ViewModel() {
    val viewState: MutableLiveData<ViewState<String>> = MutableLiveData()
    fun updatePokemon(pokemon: Pokemon) {

        viewState.value = ViewState.Loading
        pokemonRepository.updatePokemon(
            pokemon = pokemon,
            onComplete = {
                viewState.value = ViewState.Success("Dados atualizados com sucesso")
            },
            onError = {
                viewState.value = ViewState.Failed(it)
            }
        )
    }
}