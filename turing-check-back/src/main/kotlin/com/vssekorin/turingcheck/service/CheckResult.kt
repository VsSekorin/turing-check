package com.vssekorin.turingcheck.service

sealed interface CheckResult

data class Incorrect(val error: String) : CheckResult
data class Report(val result: List<TestResult>) : CheckResult

data class TestResult(val test: String, val decision: TestDecision)

enum class TestDecision {
    Accepted,
    Failed,
    Wrong,
    NotStandard,
    InfiniteLoop
}
