package org.runestar.client.plugins.dev.interfaceexplorer

import org.runestar.client.api.ICON
import org.runestar.client.game.api.Component
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.*
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel

class ExplorerFrame : JFrame("Interface Explorer") {

    private val tree: JTree

    private val treeModel: DefaultTreeModel

    private val textArea = JTextArea()

    private val root = DefaultMutableTreeNode("root")

    private val orderComboBox = JComboBox<ComponentOrder>(arrayOf(ComponentOrder.Id, ComponentOrder.Hierarchy))

    @Volatile
    var selectedComponent: Component? = null

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
            val ww = node.userObject as? ComponentWrapper ?: return@addTreeSelectionListener
            val w = ww.component
            selectedComponent = w
            fillTextArea(textArea, w)
        }
        isVisible = true
    }

    private fun reload() {
        val selected = orderComboBox.selectedItem as? ComponentOrder ?: return
        selected.reloadTree(treeModel)
    }

    private fun fillTextArea(ta: JTextArea, w: Component) {
        val x = w.accessor
        val s =
                """
$w

hierarchy:
${w.hierarchyString()}

rawX: ${x.rawX}
rawY: ${x.rawY}
rawWidth: ${x.rawWidth}
rawHeight: ${x.rawHeight}
xAlignment: ${x.xAlignment}
yAlignment: ${x.yAlignment}
widthAlignment: ${x.widthAlignment}
heightAlignment: ${x.heightAlignment}

x: ${x.x}
y: ${x.y}
width: ${x.width}
height: ${x.height}
paddingX: ${x.paddingX}
paddingY: ${x.paddingY}
scrollX: ${x.scrollX}
scrollY: ${x.scrollY}
scrollWidth: ${x.scrollWidth}
scrollHeight: ${x.scrollHeight}

rootIndex: ${x.rootIndex}
cycle: ${x.cycle}
isHidden: ${x.isHidden}

text: ${x.text}
color: ${x.color}
targetVerb: ${x.targetVerb}
ops: ${x.ops?.contentToString()}
buttonText: ${x.buttonText}
opbase: ${x.opbase}
spellName: ${x.spellName}

fontId: ${x.fontId}

type: ${x.type}
clientCode: ${x.clientCode}

itemId: ${x.itemId}
itemQuantity: ${x.itemQuantity}

itemIds: ${x.itemIds?.contentToString()}
itemQuantities: ${x.itemQuantities?.contentToString()}

noClickThrough: ${x.noClickThrough}
spriteId: ${x.spriteId}
spriteId2: ${x.spriteId2}
"""
        ta.text = s
        ta.moveCaretPosition(0)
    }

    private fun Component.hierarchyString(): String {
        val sb = StringBuilder()
        var w: Component? = this
        while (w != null) {
            sb.append(w.idString())
            sb.append(" >\n")
            w = w.parent
        }
        sb.deleteCharAt(sb.length - 1)
        return sb.toString()
    }
}