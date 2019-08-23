package org.runestar.cacheids.generate

import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugins.annotations.LifecyclePhase
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import org.apache.maven.project.MavenProject
import org.runestar.cache.content.config.ConfigType
import org.runestar.cache.format.Cache
import org.runestar.cache.format.disk.DiskCache
import org.runestar.cache.format.net.NetCache
import org.runestar.cache.tools.NameExtractor
import org.runestar.client.common.JAV_CONFIG
import org.runestar.client.common.REVISION
import java.net.InetSocketAddress
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.SortedMap
import java.util.TreeMap

@Mojo(
        name = "generate",
        defaultPhase = LifecyclePhase.GENERATE_SOURCES
)
class GenerateMojo : AbstractMojo() {

    private companion object {
        const val NAMES_URL = "https://raw.githubusercontent.com/RuneStar/cache-names/master/names.tsv"
        const val NAMES_FILE = "names.tsv"
    }

    @Parameter(defaultValue = "\${project}")
    private lateinit var project: MavenProject

    override fun execute() {
        val targetDir = Paths.get(project.build.directory)
        val outputDir = Paths.get(project.build.directory, "generated-sources", "org", "runestar", "client", "cacheids")
        Files.createDirectories(outputDir)
        project.addCompileSourceRoot(outputDir.toString())

        writeFromCache(targetDir, outputDir)
        writeFromTsv(targetDir.resolve(NAMES_FILE), outputDir)
    }

    private fun writeFromCache(cacheDir: Path, outputDir: Path) {
        lateinit var extractor: NameExtractor
        DiskCache.open(cacheDir).use { disk ->
            NetCache.connect(InetSocketAddress(JAV_CONFIG.gamepackUrl.host, NetCache.DEFAULT_PORT), REVISION).use { net ->
                Cache.update(net, disk, ConfigType.ARCHIVE).join()
            }
            extractor = NameExtractor(disk)
        }
        writeJavaFile(outputDir, "LocId", extractor.locs, false)
        writeJavaFile(outputDir, "ModelId", extractor.models, false)
        writeJavaFile(outputDir, "NpcId", extractor.npcs, false)
        writeJavaFile(outputDir, "ObjId", extractor.objs, false)
        writeJavaFile(outputDir, "SeqId", extractor.seqs, false)
        writeJavaFile(outputDir, "StructId", extractor.structs, false)
        writeJavaFile(outputDir, "StatId", extractor.stats, true)
    }

    private fun writeFromTsv(tsv: Path, outputDir: Path) {
        if (Files.notExists(tsv)) {
            URL(NAMES_URL).openStream().use { Files.copy(it, tsv) }
        }
        val graphics = TreeMap<Int, String>()
        val fonts = TreeMap<Int, String>()
        val scripts = TreeMap<Int, String>()
        tsv.toFile().forEachLine { line ->
            val split = line.split('\t')
            var name = split.last()
            if (name.isEmpty() || name.toIntOrNull() != null) return@forEachLine
            val map = when (split[0].toInt()) {
                8 -> graphics
                12 -> scripts
                13 -> fonts
                else -> return@forEachLine
            }
            name = name.toUpperCase()
                    .removePrefix("[")
                    .removeSuffix("]")
                    .replace(',', '_')
            map[split[1].toInt()] = name
        }
        writeJavaFile(outputDir, "GraphicId", graphics, true)
        writeJavaFile(outputDir, "FontId", fonts, true)
        writeJavaFile(outputDir, "ScriptId", scripts, true)
    }

    private fun writeJavaFile(
            outputDir: Path,
            className: String,
            constants: SortedMap<Int, String>,
            uniqueValues: Boolean
    ) {
        Files.newBufferedWriter(outputDir.resolve("$className.java")).use { w ->
            w.write("package org.runestar.client.cacheids;\n\n")
            w.write("public interface $className {\n")
            for ((n, name) in constants) {
                val name2 = (if (name[0].isDigit()) "_$name" else name).toUpperCase()
                w.write("\tint $name2")
                if (!uniqueValues) w.write("_$n")
                w.write("=$n;\n")
            }
            w.write("}\n")
        }
    }
}