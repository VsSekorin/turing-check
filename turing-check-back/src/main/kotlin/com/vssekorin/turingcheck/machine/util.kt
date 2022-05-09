package com.vssekorin.turingcheck.machine

import com.vssekorin.turingcheck.arrow
import com.vssekorin.turingcheck.comma

typealias State = String
typealias Cell = String

const val EMPTY: Cell = "∧"
const val STEP_LIMIT: Int = 1_000_000

fun String.head(): Cell = this.first().toString()

fun String.tail(): List<Cell> = this.takeLast(this.length - 1).toList().map { it.toString() }

fun Rule.isSuit(state: State, cell: Cell): Boolean = this.state == state && this.symbol == cell

fun rule(string: String): Rule {
    val (left, right) = string.trim().split(regex = arrow, limit = 2)
    val (state, symbol) = left.split(regex = comma, limit = 2)
    val (newState, newSymbol, shift) = right.split(regex = comma, limit = 3)
    return Rule(state, symbol, newState, newSymbol, shift(shift))
}

fun shift(str: String): Shift {
    return when (str) {
        "+1", "1", "R" -> Shift.R
        "-1", "L" -> Shift.L
        "0", "H", "N" -> Shift.N
        else -> throw IllegalArgumentException("Wrong shift: $str")
    }
}
