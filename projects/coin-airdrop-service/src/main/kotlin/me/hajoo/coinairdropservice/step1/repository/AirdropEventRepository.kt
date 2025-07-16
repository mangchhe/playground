package me.hajoo.coinairdropservice.step1.repository

import me.hajoo.coinairdropservice.step1.domain.AirdropEvent
import org.springframework.data.jpa.repository.JpaRepository

interface AirdropEventRepository : JpaRepository<AirdropEvent, Long>