package me.hajoo.coinairdropservice.step2

import me.hajoo.coinairdropservice.step1.domain.AirdropClaim
import me.hajoo.coinairdropservice.step1.repository.AirdropClaimRepository
import me.hajoo.coinairdropservice.step1.repository.AirdropEventRepository
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime

@Service
class AirdropService(
    private val claimRepository: AirdropClaimRepository,
    private val eventRepository: AirdropEventRepository,
    private val redisTemplate: StringRedisTemplate
) {
    private val airdropCountKey = "airdrop:count"
    private val airdropUsersKey = "airdrop:users"
    private val totalLimit = 10000

    fun claimAirdrop(userId: Long) {
        val ops = redisTemplate.opsForValue()
        val setOps = redisTemplate.opsForSet()

        val added = setOps.add(airdropUsersKey, userId.toString())
        if (added == 0L) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "이미 참여한 사용자입니다.")
        }

        val currentCount = ops.increment(airdropCountKey) ?: 0
        if (currentCount > totalLimit) {
            setOps.remove(airdropUsersKey, userId.toString())
            throw ResponseStatusException(HttpStatus.GONE, "에어드랍 수량이 모두 소진되었습니다.")
        }

        val event = eventRepository.findById(1).orElseThrow()
        claimRepository.save(
            AirdropClaim(
                event = event,
                userId = userId,
                claimedAt = LocalDateTime.now()
            )
        )
    }
}