package org.runestar.client.game.api.live

import org.runestar.client.game.api.SkillLevel
import org.runestar.client.game.raw.CLIENT
import org.runestar.general.Skill

object Stats : AbstractMap<Skill, SkillLevel>() {

    override val entries: Set<Map.Entry<Skill, SkillLevel>> = object : AbstractSet<Map.Entry<Skill, SkillLevel>>() {

        override val size = Skill.VALUES.size

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
        val id = key.id
        return SkillLevel(
                CLIENT.currentLevels[id],
                CLIENT.levels[id],
                CLIENT.experience[id]
        )
    }

    val totalLevel: Int get() = values.sumBy { it.level }

    val totalExperience: Long get() = values.fold(0L) { acc, sl -> acc + sl.experience.toLong() }
}