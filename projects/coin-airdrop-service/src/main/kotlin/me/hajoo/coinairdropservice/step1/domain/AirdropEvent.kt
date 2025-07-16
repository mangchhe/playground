package me.hajoo.coinairdropservice.step1.domain

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class AirdropEvent(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val name: String,

    val totalLimit: Int,

    var currentCount: Int = 0,

    @Version
    val version: Int = 0,

    val createdAt: LocalDateTime = LocalDateTime.now()
)