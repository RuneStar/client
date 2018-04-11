package org.runestar.client.plugins.widgetexplorer

import org.runestar.client.common.ICON
import org.runestar.client.game.api.Widget
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.*
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel

class ExplorerFrame : JFrame("Widget Explorer") {

    private val tree: JTree

    private val treeModel: DefaultTreeModel

    private val textArea = JTextArea()

    private val root = DefaultMutableTreeNode("root")

    private val orderComboBox = JComboBox<WidgetOrder>(arrayOf(WidgetOrder.Id, WidgetOrder.Hierarchy))

    @Volatile
    var selectedWidget: Widget? = null

    init {
        iconImage = ICON
        defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
        treeModel = DefaultTreeModel(root)
        tree = JTree(treeModel)
        tree.isRootVisible = false
        tree.showsRootHandles = true
        textArea.isEditable = false
        add(
                JSplitPane(
                    JSplitPane.HORIZONTAL_SPLIT,
                    JPanel(BorderLayout()).apply {
                        add(
                                JScrollPane(tree).apply {
                                    preferredSize = Dimension(200, 400)
                                },
                                BorderLayout.CENTER
                        )
                        add(
                                Box.createHorizontalBox().apply {
                                    add(JLabel("Order: "))
                                    add(orderComboBox)
                                    add(Box.createGlue())
                                    add(JButton("Reload").apply {
                                        addActionListener { reload() }
                                    })
                                },
                                BorderLayout.SOUTH
                        )
                    },
                    JScrollPane(textArea).apply {
                        preferredSize = Dimension(200, 400)
                    }
                )
        )
        reload()
        pack()
        tree.selectionModel.addTreeSelectionListener {
            val node = tree.lastSelectedPathComponent as? DefaultMutableTreeNode ?: return@addTreeSelectionListener
            val ww = node.userObject as? WidgetWrapper ?: return@addTreeSelectionListener
            val w = ww.widget
            selectedWidget = w
            fillTextArea(textArea, w)
        }
        isVisible = true
    }

    private fun reload() {
        val selected = orderComboBox.selectedItem as? WidgetOrder ?: return
        selected.reloadTree(treeModel)
    }

    private fun fillTextArea(ta: JTextArea, w: Widget) {
        val x = w.accessor
        val s =
                """
$w

hierarchy:
${w.hierarchyString()}

x: ${x.x}
y: ${x.y}
paddingX: ${x.paddingX}
paddingY: ${x.paddingY}
scrollX: ${x.scrollX}
scrollY; ${x.scrollY}
scrollMax: ${x.scrollMax}
width: ${x.width}
height: ${x.height}

index: ${x.index}
cycle: ${x.cycle}
isHidden: ${x.isHidden}

text: ${x.text}
textColor: ${x.textColor}
dataText: ${x.dataText}
string1: ${x.string1}
actions: ${x.actions?.contentToString()}
okText: ${x.okText}
spellActionName: ${x.spellActionName}
spellName: ${x.spellName}

fontId: ${x.fontId}
font: ${x.font}

type: ${x.type}
contentType: ${x.contentType}

itemId: ${x.itemId}
itemQuantity: ${x.itemQuantity}

itemIds: ${x.itemIds?.contentToString()}
itemQuantities: ${x.itemQuantities?.contentToString()}

hasScript: ${x.hasScript}
noClickThrough: ${x.noClickThrough}
textureId: ${x.textureId}
"""
        ta.text = s
        ta.moveCaretPosition(0)
    }

    private fun Widget.hierarchyString(): String {
        val sb = StringBuilder()
        var w: Widget? = this
        while (w != null) {
            sb.append(w.idString())
            sb.append(" >\n")
            w = w.ancestor
        }
        sb.deleteCharAt(sb.length - 1)
        return sb.toString()
    }
}