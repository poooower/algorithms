package com.github.poooower.algorithms

import java.lang.StringBuilder
import java.util.*
import kotlin.random.Random

abstract class AbsST<Key : Comparable<Key>, Value> {
    abstract val size: Int
    val isEmpty
        get() = size == 0

    abstract fun get(key: Key): Value?

    abstract fun put(key: Key, value: Value)
    abstract fun delete(key: Key)
}

abstract class AbsArrayST<Key : Comparable<Key>, Value>(var capacity: Int) : AbsST<Key, Value>() {
    internal var keys: Array<Any?> = arrayOfNulls<Any?>(capacity)
    internal var values: Array<Any?> = arrayOfNulls<Any?>(capacity)

    private var _size = 0

    override val size: Int
        get() = _size

    override fun get(key: Key): Value? {
        if (isEmpty) return null
        val i = rank(key)
        if (i < size && (keys[i] as Key).compareTo(key) == 0) return values[i] as? Value
        return null
    }

    abstract fun rank(key: Key): Int

    override fun put(key: Key, value: Value) {
        val i = rank(key)
        if (i < size && (keys[i] as Key).compareTo(key) == 0) {
            values[i] = value
            return
        }

        for (j in size downTo i + 1) {
            keys[j] = keys[j - 1]
            values[j] = values[j - 1]
        }
        keys[i] = key
        values[i] = value
        _size++
    }

    override fun delete(key: Key) {
    }

    override fun toString(): String {
        val sb = StringBuilder("{")
        for (i in 0 until size) {
            sb.append("[").append(keys[i]).append(",").append(values[i]).append("]")
        }
        sb.append("}")
        return sb.toString()
    }
}

class BinarySearchST<Key : Comparable<Key>, Value>(capacity: Int) : AbsArrayST<Key, Value>(capacity) {
    override fun rank(key: Key): Int {
        var low = 0
        var high = size - 1

        while (low <= high) {
            val mid = (low + high) / 2
            val vl = (keys[mid] as Key).compareTo(key)
            when {
                vl < 0 -> {
                    low = mid + 1
                }
                vl > 0 -> {
                    high = mid - 1
                }
                else -> {
                    return mid
                }
            }
        }

        return low
    }

}

class BST<Key : Comparable<Key>, Value> : AbsST<Key, Value>() {
    inner class Node(val key: Key, var value: Value) {
        var left: Node? = null
        var right: Node? = null
        var size: Int = 1
    }

    private var root: Node? = null

    override val size: Int
        get() = root?.size ?: 0

    override fun get(key: Key): Value? {
        val r = root ?: return null

        return get(r, key)
    }

    private fun get(node: Node?, key: Key): Value? {
        node ?: return null
        val ret = node.key.compareTo(key)
        return when {
            ret > 0 -> get(node.left, key)
            ret < 0 -> get(node.right, key)
            else -> node.value
        }
    }

    override fun put(key: Key, value: Value) {
        root = put(root, key, value)
    }

    private fun put(node: Node?, key: Key, value: Value): Node {
        node ?: return Node(key, value)
        val ret = node.key.compareTo(key)
        when {
            ret > 0 -> node.left = put(node.left, key, value)
            ret < 0 -> node.right = put(node.right, key, value)
            else -> node.value = value
        }
        node.size = (node.left?.size ?: 0) + (node.right?.size ?: 0) + 1
        return node

    }

    override fun delete(key: Key) {
        root = delete(root, key)
    }

    private fun delete(node: Node?, key: Key): Node? {
        node ?: return null
        val ret = node.key.compareTo(key)
        when {
            ret > 0 -> node.left = delete(node.left, key)
            ret < 0 -> node.right = delete(node.right, key)
            else -> {
                node.left ?: return node.right
                val right = node.right ?: return node.left
                val min = getMin(right)
                min.left = node.left
                min.right = deleteMin(node.right)
                return min
            }
        }
        return node
    }

    private fun deleteMin(node: Node?): Node? {
        node ?: return null
        node.left ?: return node.right
        node.left = deleteMin(node.left)
        node.size = (node.left?.size ?: 0) + (node.right?.size ?: 0) + 1
        return node
    }

    private fun getMin(node: Node): Node {
        val left = node.left ?: return node
        return getMin(left)
    }

    override fun toString(): String {
        val r = root ?: return ""
        val queue = LinkedList<Node>()

        queue.add(r)

        val sb = StringBuilder("")
        while (queue.isNotEmpty()) {
            val n = queue.remove()
            sb.append(n.key).append("[").append(n.value).append("]")
            n.left?.let { queue.add(it) }
            n.right?.let { queue.add(it) }
        }

        return sb.toString()
    }
}

fun main() {
//    val st = BinarySearchST<String, Int>(10)
    val st = BST<String, Int>()
    for (i in 0 until 10) {
        st.put("key_$i", Random.nextInt(0, 1000))
    }
    println(st)


    st.delete("key_3")

    println(st)


}