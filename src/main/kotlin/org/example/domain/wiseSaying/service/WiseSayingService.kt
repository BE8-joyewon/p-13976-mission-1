package org.example.domain.wiseSaying.service

import org.example.common.dto.Page
import org.example.domain.wiseSaying.entity.WiseSaying
import org.example.domain.wiseSaying.repository.WiseSayingRepository

class WiseSayingService {
    private val wiseSayingRepository = WiseSayingRepository()

    fun write(content: String, author: String): WiseSaying {
        return wiseSayingRepository.save(
            WiseSaying(content, author)
        )
    }

    fun findById(id: Int): WiseSaying? {
        return wiseSayingRepository.findById(id)
    }

    fun delete(id: Int) {
        wiseSayingRepository.delete(id)
    }

    fun modify(wiseSaying: WiseSaying, content: String, author: String): WiseSaying {
        wiseSaying.apply {
            this.content = content
            this.author = author
        }
        return wiseSayingRepository.save(wiseSaying)
    }

    fun build() {
        wiseSayingRepository.build()
    }

    fun findByKeywordPaged(
        keywordType: String,
        keyword: String,
        pageSize: Int,
        pageNumber: Int
    ): Page<WiseSaying> {
        return wiseSayingRepository.findByKeywordPaged(
            keywordType,
            keyword,
            pageSize,
            pageNumber
        )
    }
}