package org.example.domain.wiseSaying.repository

import org.example.domain.wiseSaying.entity.WiseSaying

class WiseSayingRepository {
    private val wiseSayings = mutableListOf<WiseSaying>()

    fun save(wiseSaying: WiseSaying): WiseSaying {
        if (wiseSaying.id > 0) {
            val existingIndex = wiseSayings.indexOfFirst { it.id == wiseSaying.id }
            if (existingIndex != -1) {
                wiseSayings[existingIndex] = wiseSaying
                return wiseSaying
            }
        }
        val newId = if (wiseSayings.isEmpty()) 1 else wiseSayings.maxOf { it.id } + 1
        val savedWiseSaying = WiseSaying(newId, wiseSaying.content, wiseSaying.author)
        wiseSayings.add(savedWiseSaying)
        return savedWiseSaying
    }

    fun getAll(): List<WiseSaying> {
        return wiseSayings.toList()
    }

    fun findById(id: Int): WiseSaying? {
        return wiseSayings.find { it.id == id }
    }

    fun delete(id: Int) {
        wiseSayings.removeIf { it.id == id }
    }
}