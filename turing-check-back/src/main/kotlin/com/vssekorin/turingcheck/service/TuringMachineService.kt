package com.vssekorin.turingcheck.service

import com.vssekorin.turingcheck.arrow
import com.vssekorin.turingcheck.controller.PageDto
import com.vssekorin.turingcheck.machine.*
import com.vssekorin.turingcheck.turingWord
import org.springframework.stereotype.Service
import kotlin.streams.toList

@Service
class TuringMachineService {

    fun checkProgram(program: String): List<String> = program.lines()
        .filterNot { it.isBlank() || it.isComment() || it.isCommand() }

    fun check(page: PageDto): Decisions {
        val program = page.program.lines()
            .filterNot { it.isBlank() || it.isComment() }
            .map { rule(it) }
            .flatten().toSet()
        val settings = Settings(page.empty, page.initState)
        val machine = TuringMachine(settings = settings, program = program)
        return Decisions(
            result = page.tests.lines().parallelStream()
                .filter { it.isNotBlank() }
                .map { Decision(it, makeDecision(machine, settings, it)) }
                .toList()
        )
    }

    private fun makeDecision(machine: TuringMachine, settings: Settings, test: String): DecisionName =
        if (test.isTest()) {
            val (word, expected) = test.split(regex = arrow, limit = 2)
            val actual = machine(word)
            when {
                actual.isCycled() -> DecisionName.InfiniteLoop
                actual.tape.left.dropWhile { it == settings.empty }.isNotEmpty() || actual.position != 0 ->
                    DecisionName.NotStandard
                actual.tape.turingWord(settings.empty) == expected.turingWord(settings.empty) -> DecisionName.Accepted
                else -> DecisionName.Failed
            }
        } else DecisionName.Wrong
}

private val commandRegex: Regex =
    Regex(".*,.*->.*,.*,\\s*((-1)|(\\+1)|0|L|R|H|N)\\s*(;(.*in\\s*\\{.*})+)?")
private fun String.isCommand(): Boolean = commandRegex.matches(this)
private fun String.isTest(): Boolean = contains("->")
private fun String.isComment(): Boolean = startsWith("//")
