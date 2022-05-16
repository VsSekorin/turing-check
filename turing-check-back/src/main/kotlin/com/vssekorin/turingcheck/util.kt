package com.vssekorin.turingcheck

import com.vssekorin.turingcheck.entity.Page
import com.vssekorin.turingcheck.machine.Cell
import com.vssekorin.turingcheck.machine.Configuration

fun Configuration.turingWord(empty: Cell): String =
    tape.left.dropWhile { it == empty }.joinToString(separator = "") +
            tape.current +
            tape.right.dropLastWhile { it == empty }.joinToString(separator = "")

fun Configuration.isStandard(empty: Cell): Boolean =
    tape.left.dropWhile { it == empty }.isEmpty() && position == 0

val arrow: Regex = Regex("\\s*->\\s*")
val comma: Regex = Regex("\\s*,\\s*")

fun String.head(): Cell = this.first().toString()

fun String.tail(): List<Cell> = this.takeLast(this.length - 1).toList().map { it.toString() }

fun Regex.or(other: Regex) = Regex("${this.pattern}|${other.pattern}")

fun Page.dto() = PageDto(name, description, program, tests, empty, initState)
