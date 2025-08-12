package org.example.domain.wiseSaying.repository

import org.example.common.io.AppPaths
import org.example.common.dto.Page
import org.example.domain.wiseSaying.entity.WiseSaying

class WiseSayingRepository {
    private val baseDir = AppPaths.WISE_SAYING_DIR
    private val lastIdPath = AppPaths.LAST_ID_FILE
    private val dataJsonPath = AppPaths.DATA_FILE

    private fun getLastId(): Int {
        if (!lastIdPath.toFile().exists()) {
            lastIdPath.toFile().parentFile.mkdirs()
            lastIdPath.toFile().writeText("0")
        }
        return lastIdPath.toFile().readText().toInt()
    }

    private fun updateLastId(lastId: Int) {
        lastIdPath.toFile().writeText(lastId.toString())
    }

    fun clear() {
        baseDir.toFile().deleteRecursively()
    }

    fun save(wiseSaying: WiseSaying): WiseSaying {
        if (wiseSaying.id <= 0) {
            wiseSaying.id = getLastId() + 1
            updateLastId(wiseSaying.id)
        }

        if (!baseDir.toFile().exists()) {
            baseDir.toFile().mkdirs()
        }

        val path = baseDir.resolve("${wiseSaying.id}.json")
        path.toFile().writeText(wiseSaying.toJsonString())

        return wiseSaying
    }

    fun getAll(): List<WiseSaying> {
        if (!baseDir.toFile().exists()) {
            return emptyList()
        }
        return baseDir.toFile().listFiles()?.mapNotNull { file ->
            if (file.extension == "json" && file.name != "data.json") {
                WiseSaying.fromJsonString(file.readText())
            } else {
                null
            }
        }?.sortedByDescending { it.id } ?: emptyList()
    }

    fun findById(id: Int): WiseSaying? {
        val path = baseDir.resolve("$id.json")
        if (!path.toFile().exists()) {
            return null
        }
        return WiseSaying.fromJsonString(path.toFile().readText())
    }

    fun delete(id: Int) {
        val path = baseDir.resolve("$id.json")
        if (path.toFile().exists()) {
            path.toFile().delete()
        }
    }

    fun build() {
        val wiseSayings = getAll()
        val jsonArray = wiseSayings.joinToString(
            separator = ",\n  ",
            prefix = "[\n  ",
            postfix = "\n]"
        ) { it.toJsonString(1) }

        val dataJsonPath = dataJsonPath
        if (!dataJsonPath.parent.toFile().exists()) {
            dataJsonPath.parent.toFile().mkdirs()
        }
        dataJsonPath.toFile().writeText(jsonArray)
    }

    private fun findByAuthor(author: String): List<WiseSaying> {
        return getAll().filter { it.author.contains(author, ignoreCase = true) }
    }

    private fun findByContent(content: String): List<WiseSaying> {
        return getAll().filter { it.content.contains(content, ignoreCase = true) }
    }

    fun findByKeywordPaged(
        keywordType: String,
        keyword: String,
        pageSize: Int,
        pageNumber: Int
    ): Page<WiseSaying> {
        val wiseSayings = when (keywordType) {
            "all" -> getAll()
            "author" -> findByAuthor(keyword)
            "content" -> findByContent(keyword)
            else -> emptyList()
        }

        val content = wiseSayings
            .drop((pageNumber - 1) * pageSize)
            .take(pageSize)
        val totalElements = wiseSayings.size
        val totalPages = if (totalElements == 0) 1 else (totalElements + pageSize - 1) / pageSize

        return Page(
            content = content,
            pageNumber = pageNumber,
            pageSize = pageSize,
            totalElements = totalElements.toLong(),
            totalPages = totalPages
        )
    }
}