package org.runestar.client.game.api.live

import org.runestar.client.game.api.SkillLevel
import org.runestar.client.game.raw.Client.accessor
import org.runestar.general.Skill

object Stats : AbstractMap<Skill, SkillLevel>() {

    override val entries: Set<Map.Entry<Skill, SkillLevel>> = object : AbstractSet<Map.Entry<Skill, SkillLevel>>() {

        override val size = Skill.VALUES.size - 1

        override fun iterator() = object : AbstractIterator<Map.Entry<Skill, SkillLevel>>() {

            private var i = 0

            override fun computeNext() {
                if (i >= size) return done()
                val skill = Skill.of(i++)
                setNext(java.util.AbstractMap.SimpleImmutableEntry(skill, get(skill)))
            }
        }
    }

    override fun get(key: Skill) : SkillLevel {
        require(key != Skill.OVERALL) { "Must be a regular skill" } // todo
        return SkillLevel(
                accessor.currentLevels[key.id],
                accessor.levels[key.id],
                accessor.experience[key.id]
        )
    }

    val totalLevel: Int get() = values.sumBy { it.level }

    val totalExperience: Long get() = values.fold(0L) { acc, sl -> acc + sl.experience.toLong() }
}