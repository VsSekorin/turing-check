package com.vssekorin.turingcheck.machine

enum class Shift(val delta: Int) {
    L(-1), R(+1), N(0);
}

data class Rule(val state: State, val symbol: Cell, val newState: State, val newSymbol: Cell, val shift: Shift)

data class Tape(val left: List<Cell> = emptyList(), val current: Cell = EMPTY, val right: List<Cell> = emptyList()) {
    constructor(word: String): this(current = word.head(), right = word.tail())
}

data class Configuration(val state: State, val position: Int, val tape: Tape, val count: Int)
