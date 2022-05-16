package com.vssekorin.turingcheck.service

import com.vssekorin.turingcheck.*
import com.vssekorin.turingcheck.machine.*
import org.springframework.stereotype.Service
import kotlin.streams.toList

@Service
class TuringMachineService {

    fun checkCommands(commands: String): List<String> = commands.lines()
        .filterNot { it.isBlank() || it.isComment() || it.isCommand() }

    fun check(page: PageDto): Decisions {
        val settings = Settings(page.empty, page.initState, hasHistory = false)
        val machine = machine(page, settings)
        val tests = page.tests.lines()
        val decisionsForCorrect = tests.parallelStream()
            .filter { it.isNotBlank() }
            .filter { it.isTest() }
            .map { defineResult(machine, settings, it).decision }
            .toList()
        val decisionsForIncorrect = tests.filterNot { it.isTest() }.map { Decision(it, DecisionName.NotTest) }
        return Decisions(decisionsForCorrect + decisionsForIncorrect)
    }

    fun detailed(request: DetailedRequest): DetailedResult {
        val settings = Settings(request.page.empty, request.page.initState, hasHistory = true)
        val machine = machine(request.page, settings)
        return defineResult(machine, settings, request.test)
    }

    private fun machine(page: PageDto, settings: Settings) = TuringMachine(
        settings = settings,
        program = page.program.lines()
            .filterNot { it.isBlank() || it.isComment() }
            .map { rule(it) }
            .flatten().toSet()
    )

    private fun defineResult(machine: TuringMachine, settings: Settings, test: String): DetailedResult {
        val (word, expected) = test.split(regex = arrow, limit = 2)
        val (start, result, history) = machine(word)
        return DetailedResult(
            decision = Decision(test, makeDecision(result, expected, settings.empty)),
            start = start,
            result = result,
            history = history
        )
    }

    private fun makeDecision(actual: Configuration, expected: String, empty: Cell) = when {
        actual.isCycled() -> DecisionName.InfiniteLoop
        actual.turingWord(empty) != expected -> DecisionName.Incorrect
        actual.turingWord(empty) == expected && !actual.isStandard(empty) -> DecisionName.NonStandard
        actual.turingWord(empty) == expected && actual.isStandard(empty) -> DecisionName.Correct
        else -> DecisionName.Incorrect
    }
}

private val commandRegex: Regex =
    Regex(".*,.*->.*,.*,\\s*((-1)|(\\+1)|0|L|R|H|N)\\s*(;(.*in\\s*\\{.*})+)?")
private fun String.isCommand(): Boolean = commandRegex.matches(this)
private fun String.isTest(): Boolean = contains("->")
private fun String.isComment(): Boolean = startsWith("//")
