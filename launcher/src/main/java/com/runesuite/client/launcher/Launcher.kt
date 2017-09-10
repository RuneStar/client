@file:JvmName("Launcher")

package com.runesuite.client.launcher

import com.alee.laf.WebLookAndFeel
import com.alee.laf.label.WebLabel
import com.alee.laf.progressbar.WebProgressBar
import com.alee.laf.rootpane.WebFrame
import com.runesuite.client.shared.CORE
import java.awt.Dimension
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.Box
import javax.swing.SwingUtilities

fun main(args: Array<String>) {

    val frame by lazy { WebFrame("RuneSuite Client") }
    val progressBar by lazy { WebProgressBar(0, 100) }
    val label by lazy { WebLabel("Starting...") }

    SwingUtilities.invokeLater {

        WebLookAndFeel.install()
        WebLookAndFeel.setDecorateAllWindows(true)

        val gbc = GridBagConstraints().apply {
            gridwidth = GridBagConstraints.REMAINDER
        }

        frame.apply {
            isUndecorated = true
            isShowWindowButtons = false
            isShowResizeCorner = false
            setLocationRelativeTo(null)
            layout = GridBagLayout()
            size = Dimension(350, 200)
        }

        progressBar.apply {
            isStringPainted = true
        }

        frame.add(progressBar, gbc)
        frame.add(Box.createRigidArea(Dimension(0, 20)), gbc)
        frame.add(label, gbc)
        frame.isVisible = true
    }

    Thread.sleep(5_000)

    // todo

    SwingUtilities.invokeLater {
        progressBar.value = 100
        label.text = "Done"
        frame.dispose()
    }

    ProcessBuilder("java", "-jar", CORE.toString()).start()
}