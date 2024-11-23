package com.roundest.pokeapi

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.graphql.client.HttpSyncGraphQlClient
import org.springframework.web.client.RestClient

private data class RawPokemonData(
    val id: Int,
    @JsonProperty("pokemon_v2_pokemonspecy")
    val pokemonV2Pokemonspecy: PokemonSpecy
)

data class PokemonSpecy(
    val name: String
)

data class PokemonData(
    val id: Int,
    val name: String
)

private const val POKEAPI_ENDPOINT = "https://beta.pokeapi.co/graphql/v1beta"

class PokeAPIClient {
    private var restClient = RestClient.create(POKEAPI_ENDPOINT)
    private var graphQlClient = HttpSyncGraphQlClient.create(restClient)
    private val document = """
    query GetAllPokemon {
        pokemon_v2_pokemon(where: {id: {_lte: 1025}}) {
            id
            pokemon_v2_pokemonspecy {
                name
            }
        }
    }"""

    fun getAllPokemon(): List<PokemonData> =
        graphQlClient.document(document)
            .retrieveSync("pokemon_v2_pokemon")
            .toEntityList(RawPokemonData::class.java)
            .map { PokemonData(it.id, it.pokemonV2Pokemonspecy.name) }
}