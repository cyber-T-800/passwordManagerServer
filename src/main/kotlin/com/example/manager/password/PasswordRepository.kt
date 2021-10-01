package com.example.manager.password

import com.example.manager.client.Client
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PasswordRepository : JpaRepository<Password, Long>{
    @Query("SELECT p FROM Password p WHERE p.clientId = ?1")
    fun findByClientId(clientID: Long) : Collection<Password>
}