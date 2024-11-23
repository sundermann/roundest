package com.roundest.pokemon

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class PokemonService(val pokemonRepository: PokemonRepository) {
    @Transactional
    fun vote(vote: Vote): Pair<Pokemon, Pokemon> {
        if (vote.loserDexId == vote.winnerDexId) {
            throw IllegalArgumentException("winDexId and loserDexId cannot be the same")
        }

        val winPokemon = pokemonRepository.findById(vote.winnerDexId)
            .orElseThrow { IllegalArgumentException("Pokemon with dexId ${vote.winnerDexId} not found") }
        val loserPokemon = pokemonRepository.findById(vote.loserDexId)
            .orElseThrow { IllegalArgumentException("Pokemon with dexId ${vote.loserDexId} not found") }

        winPokemon.upVotes++
        loserPokemon.downVotes++
        val w = pokemonRepository.save(winPokemon)
        val l = pokemonRepository.save(loserPokemon)
        return Pair(w, l)
    }

    @Transactional
    fun getAllVotes(): List<Pokemon> {
        return pokemonRepository.findAll()
            .sortedByDescending { it.winPercentage }
    }

    @Transactional
    fun getPairing() = pokemonRepository.findTwoRandomPokemon()
        .let { Pair(it.first(), it.last()) }
}