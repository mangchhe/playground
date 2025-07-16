package me.hajoo.coinairdropservice.step2

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/airdrop")
class AirdropController(
    private val airdropService: AirdropService
) {
    @PostMapping("/claim")
    fun claimAirdrop(@RequestHeader("X-USER-ID") userId: Long): ResponseEntity<String> {
        airdropService.claimAirdrop(userId)
        return ResponseEntity.ok("에어드랍 참여 완료")
    }

    @PostMapping("/test")
    fun orderRedirect(@RequestBody params: String) {
        println(params)
    }
}