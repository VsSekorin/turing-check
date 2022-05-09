package com.vssekorin.turingcheck.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin
class TechController {

    /**
     * It is the technical endpoint.
     * This endpoint always returns ok.
     */
    @GetMapping("/hello")
    fun hello() = ResponseEntity.ok("Ok")
}
