package com.vssekorin.turingcheck.service

data class Decisions(val result: List<Decision>)

data class Decision(val test: String, val decision: DecisionName)

enum class DecisionName {
    Accepted,
    Failed,
    Wrong,
    NotStandard,
    InfiniteLoop
}
