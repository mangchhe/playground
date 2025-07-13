package me.hajoo.coinairdropservice.repository

import me.hajoo.coinairdropservice.domain.AirdropClaim
import org.springframework.data.jpa.repository.JpaRepository

interface AirdropClaimRepository : JpaRepository<AirdropClaim, Long> {
    fun existsByEventIdAndUserId(eventId: Long, userId: Long): Boolean
}