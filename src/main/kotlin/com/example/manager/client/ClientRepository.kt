package com.example.manager.client

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ClientRepository : JpaRepository<Client, Long>{
    @Query("SELECT c FROM Client c WHERE c.username = ?1")
    fun findByName(name : String) : Client?

    @Query("SELECT c FROM Client c WHERE c.username = ?1 and c.password = ?2")
    fun findByNameAndPassword(name : String, password: String) : Client?
}