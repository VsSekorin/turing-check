package com.vssekorin.turingcheck

import com.vssekorin.turingcheck.machine.Configuration

data class PageDto(
    val name: String,
    val description: String,
    val program: String,
    val tests: String,
    val empty: String,
    val initState: String
)

data class Decisions(val result: List<Decision>)

data class Decision(val test: String, val decision: DecisionName)

enum class DecisionName {
    NotTest,
    Correct,
    Incorrect,
    NonStandard,
    InfiniteLoop
}

data class DetailedRequest(val page: PageDto, val test: String)

data class DetailedResult(
    val decision: Decision,
    val start: Configuration,
    val result: Configuration,
    val history: List<Configuration>
)
