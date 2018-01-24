package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Type
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Method2
import java.lang.reflect.Modifier

@DependsOn(AbstractFont::class)
class Font : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<AbstractFont>() }

    @DependsOn(AbstractFont.drawGlyph::class)
    class drawGlyph : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<AbstractFont.drawGlyph>().mark }
    }

    @DependsOn(AbstractFont.drawGlyphAlpha::class)
    class drawGlyphAlpha : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<AbstractFont.drawGlyphAlpha>().mark }
    }
}