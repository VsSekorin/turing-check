package com.vssekorin.turingcheck

import com.vssekorin.turingcheck.machine.EMPTY
import com.vssekorin.turingcheck.machine.Tape

typealias WrongLines = List<String>

fun <T> List<T>.toPair(): Pair<T, T> {
    if (this.size != 2) {
        throw IllegalArgumentException("List is not of length 2")
    }
    return Pair(this[0], this[1])
}

fun String.turingWord() = trim().replace(" ", EMPTY)
fun Tape.turingWord() = current + right.joinToString(separator = "").dropLastWhile { it.toString() == EMPTY }

val arrow: Regex = Regex("\\s*->\\s*")
val comma: Regex = Regex("\\s*,\\s*")
