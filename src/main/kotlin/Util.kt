package org.education

fun getSpaces(count : Int) : String {
    val sb = StringBuilder()
    for (i in 0..36 -count) {
        sb.append(" ")
    }
    return sb.toString();
}

fun getSymbols(value: String, count : Int) : String {
    val sb = StringBuilder()
    for (i in 0..36 -count) {
        sb.append(value)
    }
    return sb.toString();
}