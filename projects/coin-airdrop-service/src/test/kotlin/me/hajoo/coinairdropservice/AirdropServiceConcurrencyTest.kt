package me.hajoo.coinairdropservice

import me.hajoo.coinairdropservice.step1.AirdropService
import me.hajoo.coinairdropservice.step1.repository.AirdropClaimRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import kotlin.test.Test

@SpringBootTest
class AirdropServiceConcurrencyTest @Autowired constructor(
    private val airdropService: AirdropService,
    private val claimRepo: AirdropClaimRepository
) {

    @Test
    fun `동시 요청 100명 - 선착순 100명 처리`() {
        val threadCount = 100
        val maxParticipants = 100L
        val eventId = 1L

        val executorService = Executors.newFixedThreadPool(threadCount)
        val latch = CountDownLatch(threadCount)

        (1..threadCount).forEach { userId ->
            executorService.submit {
                try {
                    airdropService.claimAirdrop(eventId, userId.toLong())
                } catch (e: Exception) {
                    println(e.message)
                    println(e.cause)
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()

        val totalClaims = claimRepo.count()
        assertEquals(maxParticipants, totalClaims)
    }
}