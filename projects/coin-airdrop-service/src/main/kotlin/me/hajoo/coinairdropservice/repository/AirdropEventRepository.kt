package me.hajoo.coinairdropservice.repository

import me.hajoo.coinairdropservice.domain.AirdropEvent
import org.springframework.data.jpa.repository.JpaRepository

interface AirdropEventRepository : JpaRepository<AirdropEvent, Long>