package me.hajoo.coinairdropservice

import me.hajoo.coinairdropservice.domain.AirdropClaim
import me.hajoo.coinairdropservice.repository.AirdropClaimRepository
import me.hajoo.coinairdropservice.repository.AirdropEventRepository
import jakarta.transaction.Transactional
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.stereotype.Service

@Service
class AirdropService(
    private val eventRepo: AirdropEventRepository,
    private val claimRepo: AirdropClaimRepository
) {

    @Transactional
    fun claimAirdrop(eventId: Long, userId: Long) {
        if (claimRepo.existsByEventIdAndUserId(eventId, userId)) {
            throw IllegalStateException("이미 참여한 사용자입니다.")
        }

        val event = eventRepo.findById(eventId)
            .orElseThrow { IllegalArgumentException("이벤트가 존재하지 않습니다.") }

        if (event.currentCount >= event.totalLimit) {
            throw IllegalStateException("에어드랍이 모두 소진되었습니다.")
        }

        try {
            event.currentCount += 1
            eventRepo.save(event)

            val claim = AirdropClaim(event = event, userId = userId)
            claimRepo.save(claim)
        } catch (e: OptimisticLockingFailureException) {
            throw IllegalStateException("동시 요청 처리 중 실패했습니다. 다시 시도하세요.")
        }
    }

    fun getStatus(eventId: Long): Map<String, Int> {
        val event = eventRepo.findById(eventId)
            .orElseThrow { IllegalArgumentException("이벤트가 존재하지 않습니다.") }
        return mapOf(
            "totalLimit" to event.totalLimit,
            "currentCount" to event.currentCount
        )
    }
}