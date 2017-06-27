package com.runesuite.client.ext.swing

import java.awt.event.MouseEvent
import javax.swing.SwingUtilities

val MouseEvent.isLeftButton get() = SwingUtilities.isLeftMouseButton(this)

val MouseEvent.isRightButton get() = SwingUtilities.isRightMouseButton(this)

val MouseEvent.isMiddleButton get() = SwingUtilities.isMiddleMouseButton(this)