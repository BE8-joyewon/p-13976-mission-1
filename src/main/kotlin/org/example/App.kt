package org.example

import org.example.common.command.Command
import org.example.domain.wiseSaying.controller.WiseSayingController

class App {
    private val wiseSayingController = WiseSayingController()

    fun run() {
        println("== 명언 앱 ==")
        while (true) {
            print("명령) ")
            val line = readlnOrNull()?.trim() ?: ""
            val cmd = Command.parse(line) ?: continue
            when (cmd.action) {
                "종료" -> break
                "등록" -> wiseSayingController.actionWrite()
                "목록" -> wiseSayingController.actionList(cmd.params)
                "삭제" -> wiseSayingController.actionDelete(cmd.params)
                "수정" -> wiseSayingController.actionModify(cmd.params)
                "빌드" -> wiseSayingController.actionBuild()
            }
        }
    }
}