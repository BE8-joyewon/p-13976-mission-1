package org.example.domain.wiseSaying.entity

class WiseSaying(
    val id: Int,
    var content: String,
    var author: String
) {
    constructor(content: String, author: String) : this(0, content, author)
}