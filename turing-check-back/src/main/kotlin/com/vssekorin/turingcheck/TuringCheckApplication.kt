package com.vssekorin.turingcheck

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TuringCheckBackApplication

fun main(args: Array<String>) {
	runApplication<TuringCheckBackApplication>(*args)
}
