package com.vssekorin.turingcheck.machine

import com.vssekorin.turingcheck.arrow
import com.vssekorin.turingcheck.comma
import com.vssekorin.turingcheck.or

typealias State = String
typealias Cell = String

const val STEP_LIMIT: Int = 300_000

fun Rule.isSuit(state: State, cell: Cell): Boolean = this.state == state && this.symbol == cell

fun rule(string: String): Rule = string.trim().split(arrow.or(comma)).let {
    Rule(it[0], it[1], it[2], it[3], shift(it[4]))
}

fun shift(str: String): Shift = when (str) {
    "+1", "1", "R" -> Shift.R
    "-1", "L" -> Shift.L
    "0", "H", "N" -> Shift.N
    else -> throw IllegalArgumentException("Wrong shift: $str")
}
