package com.roundest.pokemon

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface PokemonRepository : JpaRepository<Pokemon, Int> {
    @Query("SELECT * FROM pokemon ORDER BY RAND() LIMIT 2", nativeQuery = true)
    fun findTwoRandomPokemon(): List<Pokemon>
}