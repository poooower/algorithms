package com.github.poooower.algorithms

import kotlin.random.Random

inline fun <reified T : Comparable<T>> randomArr(n: Int, create: () -> T): Array<T> {
    return Array(n) {
        create()
    }
}

abstract class AbsSort {
    abstract fun <T : Comparable<T>> sort(arr: Array<T>)

    inline fun <T : Comparable<T>> less(a: T, b: T) = a <= b
    inline fun <T> exch(arr: Array<T>, i: Int, j: Int) {
        val tmp = arr[i]
        arr[i] = arr[j]
        arr[j] = tmp
    }

    fun <T> show(arr: Array<T>) {
        arr.forEach {
            print("$it ")
        }
        println()
    }

    fun <T : Comparable<T>> isSorted(arr: Array<T>): Boolean {
        for (i in 1 until arr.size) {
            if (less(arr[i], arr[i - 1])) return false
        }
        return true
    }


}


/**
 * 选择排序：
 * 从右边剩余元素中，选择出 最小元素，放在最左边有序元素的最右侧
 * 时间复杂度：N^2
 */
object Selection : AbsSort() {
    override fun <T : Comparable<T>> sort(arr: Array<T>) {
        for (i in 0 until arr.size - 1) {
            var min = i
            for (j in i + 1 until arr.size) {
                if (less(arr[j], arr[min])) {
                    min = j
                }
            }
            if (min != i) {
                exch(arr, i, min)
            }
        }
    }
}

/**
 * 插入排序：
 * 依次将右边剩余元素，插入到左边有序元素中
 * 时间复杂度：N^2
 */
object Insert : AbsSort() {
    override fun <T : Comparable<T>> sort(arr: Array<T>) {
        for (i in 1 until arr.size) {
            for (j in i downTo 1) {
                if (less(arr[j], arr[j - 1])) {
                    exch(arr, j, j - 1)
                } else {
                    break
                }
            }
        }
    }

}

/**
 * 希尔排序
 * 基于插入排序，把插入时交换步长变大，减少交换次数
 */
object Shell : AbsSort() {
    override fun <T : Comparable<T>> sort(arr: Array<T>) {
        val N = arr.size
        var h = 1
        while (h < N / 3) h = 3 * h + 1

        while (h >= 1) {
            for (i in h until arr.size) {
                for (j in i downTo 1 step h) {
                    if (less(arr[j], arr[j - 1])) {
                        exch(arr, j, j - 1)
                    } else {
                        break
                    }
                }
            }

            h /= 3
        }
    }

}

/**
 * 归并排序
 * 分治思想，递归合并两个有序数组
 */
object Merge : AbsSort() {
    override fun <T : Comparable<T>> sort(arr: Array<T>) {
        sort(arr, arr.clone(), 0, arr.size - 1)
    }

    private fun <T : Comparable<T>> sort(arr: Array<T>, extra: Array<T>, low: Int, high: Int) {
        if (high <= low) return
        val mid = (low + high) / 2
        sort(arr, extra, low, mid)
        sort(arr, extra, mid + 1, high)
        merge(arr, extra, low, mid, high)
    }

    private fun <T : Comparable<T>> merge(arr: Array<T>, extra: Array<T>, low: Int, mid: Int, high: Int) {
        var i = low
        var j = mid + 1

        for (k in low..high) {
            extra[k] = arr[k]
        }

        for (k in low..high) {
            when {
                i > mid -> arr[k] = extra[j++]
                j > high -> arr[k] = extra[i++]
                less(extra[i], extra[j]) -> arr[k] = extra[i++]
                else -> arr[k] = extra[j++]
            }
        }

    }
}

/**
 * 快速排序
 * 分治思想，递归将数组拆分成大小部分
 */
object Quick : AbsSort() {
    override fun <T : Comparable<T>> sort(arr: Array<T>) {
        sort(arr, 0, arr.size - 1)
    }

    private fun <T : Comparable<T>> sort(arr: Array<T>, low: Int, high: Int) {
        if (high <= low) return
        val p = partition(arr, low, high)
        sort(arr, low, p - 1)
        sort(arr, p + 1, high)
    }

    private fun <T : Comparable<T>> partition(arr: Array<T>, low: Int, high: Int): Int {
        var l = low
        var h = high + 1
        var p = arr[low]
        while (true) {
            while (less(arr[++l], p)) if (l == high) break

            while (!less(arr[--h], p)) if (h == low) break

            if (l >= h) break

            exch(arr, l, h)
        }

        exch(arr, low, h)

        show((arr))
        return h

    }
}

/**
 * 大顶堆，数组存储，k为当前节点，2k为其左子节点，2k+1为其右子节点
 * 添加元素，添加的到最后，然后执行swim()方法上浮
 * 删除最大值，删除第1个元素（根结点），将最后一个元素移至第1个元素，执行sink方法下沉
 */
class MaxHeap<T : Comparable<T>>(capacity: Int) : AbsSort() {
    override fun <T : Comparable<T>> sort(arr: Array<T>) {
    }

    private val arr: Array<Any?> = arrayOfNulls<Any?>(capacity + 1)

    var size = 0
        private set

    private inline fun get(index: Int) = arr[index] as T

    /**
     * 和父元素比较
     * 如果比父元素大，则交换
     * 反之结束
     */
    fun swim(kParam: Int) {
        var k = kParam
        while (k > 1 && less(get(k / 2), get(k))) {
            exch(arr, k / 2, k)
            k /= 2
        }
//        println("swim")
//        show(arr)
    }

    /**
     * 右右子元素比较，选出较大值和父元素比较
     * 如果子元素大，则交换
     * 反之结束
     */
    fun sink(kParam: Int) {
        var k = kParam
        while (2 * k <= size) {
            var s = 2 * k
            if (s < size && less(get(s), get(s + 1))) s++
            if (less(get(s), get(k))) break

            exch(arr, k, s)

            k = s
        }
//        println("sink")
//        show(arr)
    }


    fun insert(t: T) {
        arr[++size] = t
        swim(size)
    }

    fun getMax() = arr[1] as T

    fun delMax(): T {
        val t = getMax()

        exch(arr, 1, size)
        arr[size--] = null
        sink(1)

        return t
    }
}

/**
 * 堆排序
 * 1 构建有序堆，对前一半元素从后向前做sink操作（因为后一半元素都是前一半元素的左右子节点）
 * 2 交换第0个元素和第N个元素（有序堆中，第0个元素是最大的，交换后最大的到最后去）
 * 3 对第0个元素到第（N-1）个元素做sink操作
 * 4 重复 2，3步
 */
object Heap : AbsSort() {
    override fun <T : Comparable<T>> sort(arr: Array<T>) {
        for (i in (arr.size / 2 - 1) downTo 0) {
            sink(arr, i, arr.size - 1)
        }

        for (i in (arr.size - 1) downTo 0) {
            exch(arr, 0, i)
            sink(arr, 0, i - 1)
        }
    }

    fun <T : Comparable<T>> sink(arr: Array<T>, kParam: Int, n: Int) {
        var k = kParam
        while (2 * k + 1 <= n) {
            var s = 2 * k + 1
            if (s < n && less(arr[s], arr[s + 1])) s++
            if (less(arr[s], arr[k])) break

            exch(arr, k, s)

            k = s
        }
//        println("sink")
//        show(arr)
    }

}


fun main() {
//    val originArr = randomArr(10) {
//        Random.nextInt(0, 10000)
//    }
    val originArr = arrayOf(2927, 4109, 9060, 5758, 640, 1221, 4858, 2104, 4183, 5489)
    val sort = Heap
    val arr = originArr.clone()
    sort.show(arr)
    println("*************************")
    sort.sort(arr)
    assert(sort.isSorted(arr))
    println("*************************")
    sort.show(arr)
}
