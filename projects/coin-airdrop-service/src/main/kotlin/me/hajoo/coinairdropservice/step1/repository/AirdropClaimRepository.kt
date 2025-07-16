package me.hajoo.coinairdropservice.step1.repository

import me.hajoo.coinairdropservice.step1.domain.AirdropClaim
import org.springframework.data.jpa.repository.JpaRepository

interface AirdropClaimRepository : JpaRepository<AirdropClaim, Long> {
    fun existsByEventIdAndUserId(eventId: Long, userId: Long): Boolean
}