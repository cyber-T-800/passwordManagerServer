package com.example.manager.client

import org.checkerframework.checker.units.qual.Length
import javax.persistence.*

@Entity
@Table(name = "clients")
data class Client(
    @Id
    @SequenceGenerator(
        name = "client_id_generator",
        sequenceName = "client_id_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        generator = "client_id_generator",
        strategy = GenerationType.SEQUENCE
    )
    var id : Long,
    var username : String,
    //hashed password saved in Base64
    var password : String,
    //private key encrypted by password and save in Base64
    @Column(length = 2000)
    var privateKey : String

){
    constructor() : this(0, "", "", "")
}
