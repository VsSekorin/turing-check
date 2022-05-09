package com.vssekorin.turingcheck.service

import com.vssekorin.turingcheck.WrongLines
import com.vssekorin.turingcheck.arrow
import com.vssekorin.turingcheck.controller.dto.PageDto
import com.vssekorin.turingcheck.machine.EMPTY
import com.vssekorin.turingcheck.machine.STEP_LIMIT
import com.vssekorin.turingcheck.machine.TuringMachine
import com.vssekorin.turingcheck.machine.rule
import com.vssekorin.turingcheck.turingWord
import org.springframework.stereotype.Service
import kotlin.streams.toList

@Service
class TuringMachineService {

    fun checkProgram(program: String): WrongLines = program.lines()
        .filterNot { it.isBlank() }
        .filterNot { it.isCommand() }

    fun check(page: PageDto): Report {
        val program = page.program.lines().filterNot { it.isBlank() }.map { rule(it) }.toSet()
        val machine = TuringMachine(initState = "q0", program = program)
        return Report(
            result = page.tests.lines().parallelStream()
                .filter { it.isNotBlank() }
                .map { makeDecision(machine, it) }
                .toList()
        )
    }

    private fun makeDecision(machine: TuringMachine, test: String): TestResult {
        val decision = if (test.isTest()) {
            val (word, expected) = test.split(regex = arrow, limit = 2)
            val actual = machine(word)
            when {
                actual.count >= STEP_LIMIT -> TestDecision.InfiniteLoop
                actual.tape.left.dropWhile { it == EMPTY }.isNotEmpty() || actual.position != 0 ->
                    TestDecision.NotStandard
                actual.tape.turingWord() == expected.turingWord() -> TestDecision.Accepted
                else -> TestDecision.Failed
            }
        } else {
            TestDecision.Wrong
        }
        return TestResult(test, decision)
    }
}

private val commandRegex: Regex = Regex(".*,.*->.*,.*,\\s*((-1)|(\\+1)|0|L|R|H|N)\\s*")
private fun String.isCommand(): Boolean = commandRegex.matches(this)

private fun String.isTest(): Boolean = this.contains("->")
