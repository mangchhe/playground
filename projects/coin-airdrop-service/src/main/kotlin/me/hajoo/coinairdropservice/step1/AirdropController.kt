package me.hajoo.coinairdropservice.step1

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

//@RestController
@RequestMapping("/airdrop")
class AirdropController(
    private val airdropService: AirdropService
) {

    @PostMapping("/claim")
    fun claim(@RequestHeader("X-USER-ID") userId: Long): ResponseEntity<String> {
        return try {
            airdropService.claimAirdrop(1L, userId)
            ResponseEntity.ok("에어드랍 참여 성공")
        } catch (e: IllegalStateException) {
            ResponseEntity.status(HttpStatus.CONFLICT).body(e.message)
        }
    }

    @GetMapping("/status")
    fun status(): ResponseEntity<Map<String, Int>> {
        return ResponseEntity.ok(airdropService.getStatus(1L))
    }
}