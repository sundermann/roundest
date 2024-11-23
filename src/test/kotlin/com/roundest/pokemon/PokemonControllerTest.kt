package com.roundest.pokemon

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest
class PokemonControllerTest(@Autowired val mockMvc: MockMvc) {

    companion object {
        val pikachu = Pokemon(1, "Pikachu", 5, 3)
        val charmander = Pokemon(2, "Charmander", 3, 5)
        val squirtle = Pokemon(3, "Squirtle", 4, 4)
    }

    @MockkBean
    lateinit var pokemonService: PokemonService

    @Test
    fun `Result page renders`() {
        every { pokemonService.getAllVotes() } returns listOf(pikachu, charmander, squirtle)
        mockMvc.perform(get("/results").accept("text/html"))
            .andExpect(status().isOk)
            .andExpect(content().string(containsString("Pikachu")))
            .andExpect(content().string(containsString("Charmander")))
            .andExpect(content().string(containsString("Squirtle")))
    }

    @Test
    fun `Index page renders`() {
        every { pokemonService.getPairing() } returns Pair(pikachu, charmander)
        mockMvc.perform(get("/").accept("text/html"))
            .andExpect(status().isOk)
            .andExpect(content().string(containsString("Pikachu")))
            .andExpect(content().string(containsString("Charmander")))
    }

    @Test
    fun `Voting renders template`() {
        val vote = Vote(pikachu.dexId, charmander.dexId)
        every { pokemonService.vote(vote) } returns Pair(pikachu, charmander)
        every { pokemonService.getPairing() } returns Pair(pikachu, charmander)
        mockMvc.perform(post("/vote?winnerDexId=1&loserDexId=2").accept("text/html"))
            .andExpect(status().isOk)
            .andExpect(content().string(containsString("Pikachu")))
            .andExpect(content().string(containsString("Charmander")))
    }
}