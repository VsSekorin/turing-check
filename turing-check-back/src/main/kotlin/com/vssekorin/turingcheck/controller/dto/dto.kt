package com.vssekorin.turingcheck.controller.dto

import com.vssekorin.turingcheck.entity.Page

data class PageDto(
    val name: String,
    val description: String,
    val program: String,
    val tests: String,
    val empty: String,
    val initState: String
)

fun Page.dto() = PageDto(name, description, program, tests, empty, initState)
