package com.vssekorin.turingcheck.service

import com.vssekorin.turingcheck.arrow
import com.vssekorin.turingcheck.controller.dto.PageDto
import com.vssekorin.turingcheck.machine.*
import com.vssekorin.turingcheck.turingWord
import org.springframework.stereotype.Service
import kotlin.streams.toList

@Service
class TuringMachineService {

    fun checkProgram(program: String): List<String> = program.lines()
        .filterNot { it.isBlank() }
        .filterNot { it.isCommand() }

    fun check(page: PageDto): Report {
        val program = page.program.lines().filterNot { it.isBlank() }.map { rule(it) }.toSet()
        val settings = Settings(page.empty, page.initState)
        val machine = TuringMachine(settings = settings, program = program)
        return Report(
            result = page.tests.lines().parallelStream()
                .filter { it.isNotBlank() }
                .map { TestResult(it, makeDecision(machine, settings, it)) }
                .toList()
        )
    }

    private fun makeDecision(machine: TuringMachine, settings: Settings, test: String): TestDecision =
        if (test.isTest()) {
            val (word, expected) = test.split(regex = arrow, limit = 2)
            val actual = machine(word)
            when {
                actual.count >= STEP_LIMIT -> TestDecision.InfiniteLoop
                actual.tape.left.dropWhile { it == settings.empty }.isNotEmpty() || actual.position != 0 ->
                    TestDecision.NotStandard
                actual.tape.turingWord(settings.empty) == expected.turingWord(settings.empty) -> TestDecision.Accepted
                else -> TestDecision.Failed
            }
        } else TestDecision.Wrong
}

private val commandRegex: Regex = Regex(".*,.*->.*,.*,\\s*((-1)|(\\+1)|0|L|R|H|N)\\s*")
private fun String.isCommand(): Boolean = commandRegex.matches(this)

private fun String.isTest(): Boolean = this.contains("->")
