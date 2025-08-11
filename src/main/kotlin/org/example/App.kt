package org.example

import org.example.domain.wiseSaying.controller.WiseSayingController

class App {
    private val wiseSayingController = WiseSayingController()

    fun run() {
        println("== 명언 앱 ==")
        while (true) {
            print("명령) ")
            val command = readlnOrNull()?.trim() ?: ""

            val params = parseCommand(command)
            when (params["action"]) {
                "종료" -> break
                "등록" -> wiseSayingController.actionWrite(params)
                "목록" -> wiseSayingController.actionList(params)
                "삭제" -> wiseSayingController.actionDelete(params)
                "수정" -> wiseSayingController.actionModify(params)
                "빌드" -> wiseSayingController.actionBuild(params)
            }
        }
    }

    private fun parseCommand(command: String): Map<String, String> {
        val params = mutableMapOf<String, String>()
        val parts = command.split("?", limit = 2)
        params["action"] = parts[0].trim()
        if (parts.size > 1) {
            val paramParts = parts[1].split("&")
            for (param in paramParts) {
                val keyValue = param.split("=", limit = 2)
                if (keyValue.size == 2) {
                    params[keyValue[0].trim()] = keyValue[1].trim()
                }
            }
        }
        return params
    }
}