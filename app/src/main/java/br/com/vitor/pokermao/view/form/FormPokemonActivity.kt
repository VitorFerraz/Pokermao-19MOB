package br.com.vitor.pokermao.view.form

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import br.com.vitor.pokermao.R
import br.com.vitor.pokermao.model.Pokemon
import br.com.vitor.pokermao.view.ViewState
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_form_pokemon.*
import kotlinx.android.synthetic.main.include_loading.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class FormPokemonActivity : AppCompatActivity() {
    companion object {
        val REQUEST_ALTER_DATA = 200
        val EXTRA_POKEMON = "EXTRA_POKEMON"
    }
    val formPokemonViewModel: FormPokemonViewModel by viewModel()
    val picasso: Picasso by inject()
    lateinit var pokemon : Pokemon
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_pokemon)
        setValues()
        btSaveForm.setOnClickListener {
            pokemon.attack = sbAttack.progress
            pokemon.defense = sbDefense.progress
            pokemon.velocity = sbVelocity.progress
            pokemon.ps = sbPS.progress
            formPokemonViewModel.updatePokemon(pokemon)
        }
        formPokemonViewModel.viewState.observe(this, Observer {
            when (it) {
                is ViewState.Loading -> {
                    btSaveForm.isEnabled = false
                    btSaveForm.alpha = 0.5F
                }
                is ViewState.Failed -> {
                    btSaveForm.isEnabled = true
                    btSaveForm.alpha = 1.0F
                    val message = it.throwable.message
                    if (message != "") Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                }
                is ViewState.Success -> {
                    btSaveForm.isEnabled = true
                    btSaveForm.alpha = 1.0F
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        })
    }

    private fun setValues() {
        pokemon = intent.getParcelableExtra<Pokemon>(FormPokemonActivity.EXTRA_POKEMON)
        tvPokemonNameForm.text = pokemon.nome

        picasso.load("https://pokedexdx.herokuapp.com${pokemon.urlImagem}").into(ivPokemonForm)
        sbAttack.progress = pokemon.attack
        sbDefense.progress = pokemon.defense
        sbPS.progress = pokemon.ps
        sbVelocity.progress = pokemon.velocity
        tvAttackValue.text = pokemon.attack.toString()
        tvDefenseValue.text = pokemon.defense.toString()
        tvPSValue.text = pokemon.ps.toString()
        tvVelocityValue.text = pokemon.velocity.toString()
        setListener(sbAttack, tvAttackValue)
        setListener(sbDefense, tvDefenseValue)
        setListener(sbVelocity, tvVelocityValue)
        setListener(sbPS, tvPSValue)
    }

    private fun setListener(seekBar: SeekBar, textView: TextView) {
        seekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser:
            Boolean) {
                textView.text = progress.toString()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }
}
