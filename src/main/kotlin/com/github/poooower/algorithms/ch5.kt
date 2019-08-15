package com.github.poooower.algorithms

abstract class AbsPattern {
    fun match(str: String, pattern: String): Int {
        if (str.length < pattern.length) return -1
        return search(str.toCharArray(), pattern.toCharArray())
    }

    abstract fun search(text: CharArray, pattern: CharArray): Int


}

object ForceSearch : AbsPattern() {
    override fun search(text: CharArray, pattern: CharArray): Int {
        val textLen = text.size
        val patternLen = pattern.size

        var i = 0
        var j = 0
        while (i < textLen) {
            if (text[i] == pattern[j]) j++
            else {
                i -= j
                j = 0
            }
            i++
        }
        if (j == patternLen) return i - j
        return -1
    }

}

fun main() {
    val search = ForceSearch

    println(search.match("AAAAAAAAAAAB", "AAAAAB"))
}