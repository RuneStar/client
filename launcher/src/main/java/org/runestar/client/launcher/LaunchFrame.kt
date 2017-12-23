package org.runestar.client.launcher

import org.eclipse.aether.transfer.TransferEvent
import org.eclipse.aether.transfer.TransferListener
import java.awt.Component
import java.awt.Dimension
import javax.swing.*

internal class LaunchFrame : JFrame("Launching RuneStar..."), TransferListener {

    private val label = JLabel("...").apply {
        alignmentX = Component.CENTER_ALIGNMENT
    }

    private val progressBar = JProgressBar().apply {
        isStringPainted = true
    }

    init {
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        setLocationRelativeTo(null)
        contentPane = Box.createVerticalBox()
        add(JLabel("Updating:").apply {
            alignmentX = Component.CENTER_ALIGNMENT
        })
        add(label)
        add(progressBar)
        pack()
        preferredSize = Dimension(width * 4, height)
        pack()
        isResizable = false
        isVisible = true
    }

    /**
     * @param[progress] value from 0.0 to 1.0
     */
    private fun updateProgress(string: String, progress: Double) {
        SwingUtilities.invokeLater {
            label.text = string
            progressBar.value = (progress * 100).toInt()
        }
    }

    override fun transferInitiated(event: TransferEvent) {
        updateProgress(event.resource.resourceName, 0.0)
    }

    override fun transferStarted(event: TransferEvent) {}

    override fun transferProgressed(event: TransferEvent) {
        updateProgress(
                event.resource.resourceName,
                event.transferredBytes.toDouble() / event.resource.contentLength
        )
    }

    override fun transferSucceeded(event: TransferEvent) {
        updateProgress(event.resource.resourceName, 1.0)
    }

    override fun transferCorrupted(event: TransferEvent) {}

    override fun transferFailed(event: TransferEvent) {}
}