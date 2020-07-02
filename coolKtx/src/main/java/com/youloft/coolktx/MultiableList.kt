package com.youloft.coolktx

/**
 * @author you
 * @create 2020/7/2
 * @desc
 */


inline fun <T> MutableList<T>.mapInPlace(mutator: (T) -> T) {
    val iterator:MutableListIterator<T> = this.listIterator()
    while (iterator.hasNext()) {
        val old = iterator.next()
        val new = mutator(old)
        if (old!=new) {
            iterator.set(new)
        }
    }
}