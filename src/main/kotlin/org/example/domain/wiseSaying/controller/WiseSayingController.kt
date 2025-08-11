package org.example.domain.wiseSaying.controller

import org.example.domain.wiseSaying.service.WiseSayingService

class WiseSayingController {
    private val wiseSayingService = WiseSayingService()

    fun actionWrite(params: Map<String, String>) {
        print("명언: ")
        val content = readlnOrNull()?.trim() ?: ""
        print("작가: ")
        val author = readlnOrNull()?.trim() ?: ""

        val wiseSaying = wiseSayingService.write(content, author)

        println("${wiseSaying.id}번 명언이 등록되었습니다.")
    }

    fun actionList(params: Map<String, String>) {
        println("번호 / 작가 / 명언")
        println("----------------------")

        wiseSayingService.getAll().sortedByDescending { it.id }.forEach {
            wiseSaying -> println("${wiseSaying.id} / ${wiseSaying.author} / ${wiseSaying.content}")
        }
    }

    fun actionDelete(params: Map<String, String>) {
        val id = params["id"]?.toIntOrNull()
        if (id == null) {
            println("삭제할 명언의 ID를 입력해주세요.")
            return
        }

        val wiseSaying = wiseSayingService.findById(id)
        if (wiseSaying == null) {
            println("${id}번 명언은 존재하지 않습니다.")
            return
        }

        wiseSayingService.delete(id)
        println("${id}번 명언이 삭제되었습니다.")
    }

    fun actionModify(params: Map<String, String>) {
        val id = params["id"]?.toIntOrNull()
        if (id == null) {
            println("수정할 명언의 ID를 입력해주세요.")
            return
        }

        val wiseSaying = wiseSayingService.findById(id)
        if (wiseSaying == null) {
            println("${id}번 명언은 존재하지 않습니다.")
            return
        }

        println("명언(기존): ${wiseSaying.content}")
        print("명언: ")
        val content = readlnOrNull()?.trim() ?: wiseSaying.content
        println("작가(기존): ${wiseSaying.author}")
        print("작가: ")
        val author = readlnOrNull()?.trim() ?: wiseSaying.author

        wiseSayingService.modify(wiseSaying, content, author)
        println("${id}번 명언이 수정되었습니다.")
    }

    fun actionBuild(params: Map<String, String>) {

        // TODO: 데이터 파일을 갱신합니다.

        println("data.json 파일의 내용이 갱신되었습니다.")
    }
}