package com.vssekorin.turingcheck.service

data class Report(val result: List<TestResult>)

data class TestResult(val test: String, val decision: TestDecision)

enum class TestDecision {
    Accepted,
    Failed,
    Wrong,
    NotStandard,
    InfiniteLoop
}
