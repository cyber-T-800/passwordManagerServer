package com.example.manager.password

import com.example.manager.client.Client
import javax.persistence.*

@Entity
@Table(name = "passwords")
data class Password(
    @Id
    @SequenceGenerator(
        name = "password_id_generator",
        sequenceName = "password_id_sequence",
        allocationSize = 1

    )
    @GeneratedValue(
        generator = "password_id_generator",
        strategy = GenerationType.SEQUENCE
    )
    var id : Long,
    var website : String,
    var username : String,
    @Column(name = "encrypted_password", length = 2000)
    var encryptedPassword : String,
    var clientId : Long
){
    constructor() : this(0, "", "", "", 0)
}