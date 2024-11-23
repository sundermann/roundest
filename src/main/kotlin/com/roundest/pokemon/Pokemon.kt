package com.roundest.pokemon

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class Pokemon(
    @Id
    val dexId: Int,
    val name: String,
    var upVotes: Int,
    var downVotes: Int,
) {
    val totalVotes: Int
        get() = upVotes + downVotes

    val winPercentage: Double
        get() = if (totalVotes > 0) upVotes / (totalVotes).toDouble() * 100.0 else 0.0
}