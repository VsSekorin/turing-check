package com.vssekorin.turingcheck

import com.vssekorin.turingcheck.machine.Cell
import com.vssekorin.turingcheck.machine.Tape

fun String.turingWord(empty: Cell): String = trim().replace(" ", empty)

fun Tape.turingWord(empty: Cell): String =
    current + right.joinToString(separator = "").dropLastWhile { it.toString() == empty }

val arrow: Regex = Regex("\\s*->\\s*")
val comma: Regex = Regex("\\s*,\\s*")

fun String.head(): Cell = this.first().toString()

fun String.tail(): List<Cell> = this.takeLast(this.length - 1).toList().map { it.toString() }

fun Regex.or(other: Regex) = Regex("${this.pattern}|${other.pattern}")
