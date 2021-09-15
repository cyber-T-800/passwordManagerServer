package com.example.manager.client

import com.example.manager.password.Password
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ClientRepository : JpaRepository<Client, Long>{
    @Query("SELECT c FROM Client c WHERE c.username = ?1")
    fun findByName(name : String) : Client?
}