package com.vssekorin.turingcheck.controller

import com.vssekorin.turingcheck.entity.Page
import com.vssekorin.turingcheck.repository.PageRepository
import com.vssekorin.turingcheck.service.Decisions
import com.vssekorin.turingcheck.service.TuringMachineService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/page")
@CrossOrigin
class PageController(val pageRepository: PageRepository, val turingMachineService: TuringMachineService) {

    @PostMapping("/create")
    fun create(@RequestParam name: String): ResponseEntity<PageDto> =
        if (name != EXAMPLE_NAME && pageRepository.findByName(name) == null) {
            val saved = pageRepository.save(Page(name = name))
            ResponseEntity.status(HttpStatus.CREATED).body(saved.dto())
        } else {
            ResponseEntity.badRequest().build()
        }

    @PutMapping("/save")
    fun save(@RequestBody dto: PageDto): ResponseEntity<Unit> =
        if (dto.name != EXAMPLE_NAME) {
            pageRepository.findByName(dto.name)?.let {
                it.description = dto.description
                it.program = dto.program
                it.tests = dto.tests
                it.empty = dto.empty
                it.initState = dto.initState
                pageRepository.save(it)
                ResponseEntity.ok()
            } ?: ResponseEntity.badRequest()
        } else {
            ResponseEntity.badRequest()
        }.build()

    @GetMapping("/open")
    fun open(@RequestParam name: String): ResponseEntity<PageDto> {
        val page = pageRepository.findByName(name)
        return if (page != null) {
            ResponseEntity.ok(page.dto())
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping("/test")
    fun test(@RequestBody dto: PageDto): ResponseEntity<Decisions> =
        if (turingMachineService.checkProgram(dto.program).isEmpty()) {
            ResponseEntity.ok(turingMachineService.check(dto))
        } else {
            ResponseEntity.badRequest().build()
        }

    companion object {
        const val EXAMPLE_NAME = "example"
    }
}

