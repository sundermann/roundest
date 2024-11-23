package com.roundest.pokemon

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

data class Vote(val loserDexId: Int, val winnerDexId: Int)

@Controller
class PokemonController(private val pokemonService: PokemonService) {
    @GetMapping("/")
    @ResponseBody
    fun index(): String {
        val (first, second) = pokemonService.getPairing()
        return votePage(first, second)
    }

    @PostMapping("/vote")
    @ResponseBody
    fun vote(vote: Vote): String {
        pokemonService.vote(vote)
        val (first, second) = pokemonService.getPairing()
        return voteFragment(first, second)
    }

    @GetMapping("/results")
    @ResponseBody
    fun results(): String {
        return voteResultsPage(pokemonService.getAllVotes())
    }
}