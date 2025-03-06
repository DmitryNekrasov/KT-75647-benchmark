package org.example.singlesequenceof

fun <T> singleSequenceOf(element: T): Sequence<T> = Sequence {
    object : Iterator<T> {
        var hasNext: Boolean = true

        override fun next(): T {
            if (!hasNext) throw NoSuchElementException()
            hasNext = false
            return element
        }

        override fun hasNext(): Boolean = hasNext
    }
}