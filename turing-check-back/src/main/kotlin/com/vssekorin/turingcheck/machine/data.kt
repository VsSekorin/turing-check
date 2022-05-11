package com.vssekorin.turingcheck.machine

import com.vssekorin.turingcheck.head
import com.vssekorin.turingcheck.tail

enum class Shift(val delta: Int) {
    L(-1), R(+1), N(0);
}

data class Rule(val state: State, val symbol: Cell, val newState: State, val newSymbol: Cell, val shift: Shift)

data class Tape(val left: List<Cell> = emptyList(), val current: Cell, val right: List<Cell> = emptyList()) {
    constructor(word: String): this(current = word.head(), right = word.tail())
}

data class Configuration(val state: State, val position: Int, val tape: Tape, val count: Int)

data class Settings(val empty: Cell, val initState: State)
