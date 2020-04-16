package org.runestar.client.web.hiscore

import java.util.stream.Stream

data class HiscoreResult private constructor(private val categories: List<Category>) {

    operator fun get(stat: Int) = categories[stat + 1]

    operator fun get(category: HiscoreCategory) = categories[category.ordinal]

    data class Category(val rank: Int, val level: Int, val xp: Long) {

        val score: Long get() = xp
    }

    internal companion object {

        fun of(lines: Stream<String>): HiscoreResult {
            val categories = ArrayList<Category>()
            lines.forEach { line ->
                val split = line.split(',')
                when (split.size) {
                    3 -> categories.add(Category(split[0].toInt(), split[1].toInt(), split[2].toLong()))
                    2 -> categories.add(Category(split[0].toInt(), -1, split[1].toLong()))
                    else -> throw IllegalArgumentException(line)
                }
            }
            return HiscoreResult(categories)
        }
    }
}