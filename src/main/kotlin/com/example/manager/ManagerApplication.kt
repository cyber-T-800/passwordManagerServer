package com.example.manager

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ManagerApplication

fun main(args: Array<String>) {
	runApplication<ManagerApplication>(*args)
}
