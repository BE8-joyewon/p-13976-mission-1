package org.example

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class AppTest {

    @Test
    @DisplayName("종료")
    fun testExit() {
        val result = TestRunner.run(
            """
                종료
            """
        )

        assert(result.contains(""))
    }

    @Test
    @DisplayName("등록")
    fun testWrite() {
        val result = TestRunner.run(
            """
                등록
                명언1
                작가1
                종료
            """
        )
        assert(result.contains("명언: "))
        assert(result.contains("작가: "))
        assert(result.contains("1번 명언이 등록되었습니다."))
    }

    @Test
    @DisplayName("목록")
    fun testList() {
        val result = TestRunner.run(
            """
                등록
                명언1
                작가1
                등록
                명언2
                작가2
                목록
                종료
            """
        )

        assert(result.contains("번호 / 작가 / 명언"))
        assert(result.contains("2 / 작가2 / 명언2"))
        assert(result.contains("1 / 작가1 / 명언1"))
    }

    @Test
    @DisplayName("삭제")
    fun testDelete() {
        val result = TestRunner.run(
            """
                등록
                명언1
                작가1
                등록
                명언2
                작가2
                삭제?id=2
                목록
                종료
            """
        )

        assert(result.contains("2번 명언이 삭제되었습니다."))
        assert(!result.contains("2 / 작가2 / 명언2"))
    }

    @Test
    @DisplayName("삭제 - 존재하지 않는 ID")
    fun testDeleteNonExistent() {
        val result = TestRunner.run(
            """
                삭제?id=999
                삭제?id=0
                종료
            """
        )

        assert(result.contains("999번 명언은 존재하지 않습니다."))
        assert(result.contains("0번 명언은 존재하지 않습니다."))
    }

    @Test
    @DisplayName("수정")
    fun testModify() {
        val result = TestRunner.run(
            """
                등록
                명언1
                작가1
                등록
                명언2
                작가2
                수정?id=2
                수정된 명언2
                수정된 작가2
                목록
                종료
            """
        )

        assert(result.contains("2번 명언이 수정되었습니다."))
        assert(result.contains("2 / 수정된 작가2 / 수정된 명언2"))
        assert(!result.contains("2 / 작가2 / 명언2"))
    }

    @Test
    @DisplayName("수정 - 존재하지 않는 ID")
    fun testModifyNonExistent() {
        val result = TestRunner.run(
            """
                수정?id=999
                수정?id=0
                종료
            """
        )

        assert(result.contains("999번 명언은 존재하지 않습니다."))
        assert(result.contains("0번 명언은 존재하지 않습니다."))
    }
}