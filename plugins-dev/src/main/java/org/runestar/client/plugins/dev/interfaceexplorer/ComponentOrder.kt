package org.runestar.client.plugins.dev.interfaceexplorer

import org.runestar.client.game.api.ComponentType
import org.runestar.client.game.api.live.Interfaces
import org.runestar.client.game.api.live.Components
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel

enum class ComponentOrder {

    Id {

        override fun reloadTree(treeModel: DefaultTreeModel) {
            val root = treeModel.root as DefaultMutableTreeNode
            root.removeAllChildren()
            Interfaces.asSequence().filterNotNull().forEach { wg ->
                val groupNode = DefaultMutableTreeNode(wg.id)
                wg.forEach { wp ->
                    val parentNode = DefaultMutableTreeNode(ComponentWrapper(wp))
                    if (wp.type == ComponentType.LAYER) {
                        wp.dynamicChildren.asSequence().filterNotNull().forEach { wc ->
                            val childNode = DefaultMutableTreeNode(ComponentWrapper(wc))
                            parentNode.add(childNode)
                        }
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
            Components.roots.forEach {
                val node = DefaultMutableTreeNode(ComponentWrapper(it))
                addChildren(node)
                root.add(node)
            }
            treeModel.reload()
        }

        private fun addChildren(node: DefaultMutableTreeNode) {
            val component = (node.userObject as ComponentWrapper).component
            if (component.type == ComponentType.LAYER) {
                component.children.forEach {
                    val c = DefaultMutableTreeNode(ComponentWrapper(it))
                    addChildren(c)
                    node.add(c)
                }
            }
        }
    };

    abstract fun reloadTree(treeModel: DefaultTreeModel)
}