package br.com.vitor.pokermao.view.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.vitor.pokermao.model.Pokemon
import br.com.vitor.pokermao.repository.PokemonRepository
import br.com.vitor.pokermao.view.ViewState

class ListPokemonsViewModel (val pokemonRepository: PokemonRepository) : ViewModel() {
    val viewState: MutableLiveData<ViewState<List<Pokemon>>> = MutableLiveData()
    fun getPokemons() {
        viewState.value = ViewState.Loading
        pokemonRepository.getPokemons(
            150, "number,asc", {
                viewState.value = ViewState.Success(it)
            }, {
                viewState.value = ViewState.Failed(it)

            }
        )
    }
}