package org.example.domain.wiseSaying.entity

class WiseSaying(
    var id: Int,
    var content: String,
    var author: String
) {
    constructor(content: String, author: String) : this(0, content, author)

    companion object {
        fun fromJsonString(json: String): WiseSaying {
            val regex = """"id":\s*(\d+),\s*"content":\s*"([^"]*)",\s*"author":\s*"([^"]*)"""".toRegex()
            val matchResult = regex.find(json)
            return if (matchResult != null) {
                WiseSaying(
                    id = matchResult.groupValues[1].toInt(),
                    content = matchResult.groupValues[2],
                    author = matchResult.groupValues[3]
                )
            } else {
                throw IllegalArgumentException("Invalid JSON format")
            }
        }
    }

    fun toJsonString(): String {
        return toJsonString(0)
    }

    fun toJsonString(indent: Int) : String {
        val indentStr = "  ".repeat(indent)

        return """
            {
              "id": $id,
              "content": "$content",
              "author": "$author"
            }
        """.trimIndent().replace("\n", "\n$indentStr")
    }
}