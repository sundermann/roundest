package com.roundest.pokeapi

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PokeAPIClientTest {
    @Test
    fun `PokeAPI returns pokemon`() {
        val pokeAPIClient = PokeAPIClient()
        val pokemon = pokeAPIClient.getAllPokemon()

        assertTrue { pokemon.isNotEmpty() }
    }
}