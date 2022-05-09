package com.vssekorin.turingcheck.machine

class TuringMachine(private val initState: State, private val program: Set<Rule>): (String) -> Configuration {

    override fun invoke(word: String): Configuration =
        process(Configuration(state = initState, position = 0, tape = Tape(word), count = 0))

    private tailrec fun process(conf: Configuration): Configuration {
        val rule: Rule? = program.firstOrNull { it.isSuit(conf.state, conf.tape.current) }
        return if (rule != null && conf.count < STEP_LIMIT) process(step(conf, rule)) else conf
    }

    private fun step(conf: Configuration, rule: Rule) = Configuration(
        state = rule.newState,
        position = conf.position + rule.shift.delta,
        tape = shift(conf.tape, rule),
        count = conf.count + 1
    )

    private fun shift(tape: Tape, rule: Rule): Tape = when (rule.shift) {
        Shift.N -> tape.copy(current = rule.newSymbol)
        Shift.L -> shiftLeft(tape, rule)
        Shift.R -> shiftRight(tape, rule)
    }

    private fun shiftLeft(tape: Tape, rule: Rule): Tape = if (tape.left.isNotEmpty()) {
        tape.copy(
            current = tape.left.last(),
            left = tape.left.dropLast(1),
            right = listOf(rule.newSymbol) + tape.right
        )
    } else {
        tape.copy(
            current = EMPTY,
            right = listOf(rule.newSymbol) + tape.right
        )
    }

    private fun shiftRight(tape: Tape, rule: Rule): Tape = if (tape.right.isNotEmpty()) {
        tape.copy(
            current = tape.right.first(),
            left = tape.left + listOf(rule.newSymbol),
            right = tape.right.drop(1)
        )
    } else {
        tape.copy(
            current = EMPTY,
            left = tape.left + listOf(rule.newSymbol)
        )
    }
}