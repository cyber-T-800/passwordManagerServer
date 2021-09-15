package com.example.manager.password

import com.example.manager.client.Client
import javax.persistence.*

@Entity
@Table(name = "passwords")
data class Password(
    @Id
    @SequenceGenerator(
        name = "password_id_generator",
        sequenceName = "sequence_generator",
        allocationSize = 1

    )
    @GeneratedValue(
        generator = "password_id_generator",
        strategy = GenerationType.SEQUENCE
    )
    var id : Long,
    var website : String,
    var username : String,
    @Column(name = "encrypted_password")
    var encryptedPassword : String,

    @OneToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    var client: Client?
){
    constructor() : this(0, "", "", "", null)
}