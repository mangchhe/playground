package me.hajoo.coinairdropservice.domain

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class AirdropClaim(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    val event: AirdropEvent,

    val userId: Long,

    val claimedAt: LocalDateTime = LocalDateTime.now()
)