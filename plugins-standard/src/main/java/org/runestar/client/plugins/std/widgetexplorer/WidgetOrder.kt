package org.runestar.client.plugins.std.widgetexplorer

import org.runestar.client.game.api.Widget
import org.runestar.client.game.api.live.WidgetGroups
import org.runestar.client.game.api.live.Widgets
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel

enum class WidgetOrder {

    Id {

        override fun reloadTree(treeModel: DefaultTreeModel) {
            val root = treeModel.root as DefaultMutableTreeNode
            root.removeAllChildren()
            WidgetGroups.asSequence().filterNotNull().forEach { wg ->
                val groupNode = DefaultMutableTreeNode(wg.id)
                wg.forEach { wp ->
                    val parentNode = DefaultMutableTreeNode(WidgetWrapper(wp))
                    wp.children.asSequence().filterNotNull().forEach { wc ->
                        val childNode = DefaultMutableTreeNode(WidgetWrapper(wc))
                        parentNode.add(childNode)
                    }
                    groupNode.add(parentNode)
                }
                root.add(groupNode)
            }
            treeModel.reload()
        }
    },

    Hierarchy {

        override fun reloadTree(treeModel: DefaultTreeModel) {
            val root = treeModel.root as DefaultMutableTreeNode
            root.removeAllChildren()
            Widgets.roots.forEach {
                val node = DefaultMutableTreeNode(WidgetWrapper(it))
                addChildren(node)
                root.add(node)
            }
            treeModel.reload()
        }

        private fun addChildren(node: DefaultMutableTreeNode) {
            val widget = (node.userObject as WidgetWrapper).widget as Widget.Parent
            widget.descendants.forEach {
                val c = DefaultMutableTreeNode(WidgetWrapper(it))
                addChildren(c)
                node.add(c)
            }
            widget.successors.forEach {
                val c = DefaultMutableTreeNode(WidgetWrapper(it))
                addChildren(c)
                node.add(c)
            }
            widget.children.asSequence().filterNotNull().forEach {
                val c = DefaultMutableTreeNode(WidgetWrapper(it))
                node.add(c)
            }
        }
    };

    abstract fun reloadTree(treeModel: DefaultTreeModel)
}