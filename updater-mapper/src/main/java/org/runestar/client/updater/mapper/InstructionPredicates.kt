package org.runestar.client.updater.mapper

fun Predicate<Instruction2>.skip(n: Int): Predicate<Instruction2> {
    return { insn ->
        if (this(insn)) {
            if (n > 0) {
                repeat(n) { insn.next() }
            } else {
                repeat(-1 * n) { insn.prev() }
            }
            true
        } else {
            false
        }
    }
}

inline fun Predicate<Instruction2>.nextIn(n: Int, crossinline other: Predicate<Instruction2>): Predicate<Instruction2> {
    return skip(n).and { it.exists }.and(other)
}

inline fun Predicate<Instruction2>.next(crossinline other: Predicate<Instruction2>): Predicate<Instruction2> {
    return nextIn(1, other)
}

inline fun Predicate<Instruction2>.prevIn(n: Int, crossinline other: Predicate<Instruction2>): Predicate<Instruction2> {
    return skip(-1 * n).and { it.exists }.and(other)
}

inline fun Predicate<Instruction2>.prev(crossinline other: Predicate<Instruction2>): Predicate<Instruction2> {
    return prevIn(1, other)
}

// todo
inline fun Predicate<Instruction2>.nextWithin(n: Int, crossinline other: Predicate<Instruction2>): Predicate<Instruction2> {
    return object : Predicate<Instruction2> {
        override fun invoke(insn: Instruction2): Boolean {
            if (this@nextWithin(insn)) {
                repeat(n) {
                    insn.next()
                    if (insn.exists && other(insn)) {
                        return true
                    }
                }
                return false
            } else {
                return false
            }
        }
    }
}

inline fun Predicate<Instruction2>.prevWithin(n: Int, crossinline other: Predicate<Instruction2>): Predicate<Instruction2> {
    return object : Predicate<Instruction2> {
        override fun invoke(insn: Instruction2): Boolean {
            if (this@prevWithin(insn)) {
                repeat(n) {
                    insn.prev()
                    if (insn.exists && other(insn)) {
                        return true
                    }
                }
                return false
            } else {
                return false
            }
        }
    }
}