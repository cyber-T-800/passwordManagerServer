package com.example.manager.password

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface PasswordRepository : JpaRepository<Password, Long>{
    @Query("SELECT p FROM Password p WHERE p.clientId = ?1")
    fun findByClientId(clientID: Long) : Collection<Password>

    @Modifying
    @Query("UPDATE Password p SET p.website = ?2, p.username = ?3, p.encryptedPassword = ?4 WHERE p.id = ?1")
    fun update(id: Long, website: String, username: String, encryptedPassword: String)
}