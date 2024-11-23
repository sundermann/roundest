package com.roundest.pokemon

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.util.*

@DataJpaTest
class PokemonServiceTest {

    companion object {
        val pikachu = Pokemon(1, "Pikachu", 5, 3)
        val charmander = Pokemon(2, "Charmander", 3, 5)
        val squirtle = Pokemon(3, "Squirtle", 4, 4)
    }

    @MockkBean
    lateinit var pokemonRepository: PokemonRepository

    @Test
    fun `vote increases up and down votes`() {
        every { pokemonRepository.findById(pikachu.dexId) } returns Optional.of(pikachu)
        every { pokemonRepository.findById(charmander.dexId) } returns Optional.of(charmander)
        every { pokemonRepository.save(any()) } answers { firstArg() }

        val pokemonService = PokemonService(pokemonRepository)

        assertEquals(
            Pair(
                pikachu.copy(upVotes = 6),
                charmander.copy(downVotes = 6),
            ), pokemonService.vote(Vote(pikachu.dexId, charmander.dexId))
        )

        verify(exactly = 1) { pokemonRepository.save(pikachu.copy(upVotes = 6)) }
        verify(exactly = 1) { pokemonRepository.save(charmander.copy(downVotes = 6)) }
    }

    @Test
    fun `voting for same pokemon throws exception`() {
        every { pokemonRepository.findById(pikachu.dexId) } returns Optional.of(pikachu)

        val pokemonService = PokemonService(pokemonRepository)

        assertEquals(
            "winDexId and loserDexId cannot be the same",
            runCatching { pokemonService.vote(Vote(pikachu.dexId, pikachu.dexId)) }.exceptionOrNull()?.message
        )
    }

    @Test
    fun `voting for not existing pokemon throws exception`() {
        every { pokemonRepository.findById(5) } returns Optional.empty()

        val pokemonService = PokemonService(pokemonRepository)

        assertEquals(
            "Pokemon with dexId 1 not found",
            runCatching { pokemonService.vote(Vote(pikachu.dexId, squirtle.dexId)) }.exceptionOrNull()?.message
        )
    }

    @Test
    fun `pairing returns two different random pokemon`() {
        every { pokemonRepository.findAll() } returns listOf(pikachu, charmander, squirtle)

        val pokemonService = PokemonService(pokemonRepository)

        val (first, second) = pokemonService.getPairing()
        assertTrue { first != second }
    }
}