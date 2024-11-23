package com.roundest.pokemon

import com.roundest.pokeapi.PokeAPIClient
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class PokemonSeeder(private val pokemonRepository: PokemonRepository) : CommandLineRunner {
    override fun run(vararg args: String) {
        val pokeAPIClient = PokeAPIClient()
        val pokemon = pokeAPIClient.getAllPokemon()
            .map { pokemonData ->
                Pokemon(
                    dexId = pokemonData.id,
                    name = pokemonData.name,
                    upVotes = 0,
                    downVotes = 0
                )
            }
        pokemonRepository.deleteAll()
        pokemonRepository.saveAll(pokemon)
    }
}