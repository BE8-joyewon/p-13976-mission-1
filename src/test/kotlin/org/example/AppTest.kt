package org.example

import org.example.common.io.AppPaths
import org.example.domain.wiseSaying.repository.WiseSayingRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class AppTest {
    private val wiseSayingRepository = WiseSayingRepository()

    @BeforeEach
    fun setUp() {
        wiseSayingRepository.clear()
    }

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

        val path = AppPaths.WISE_SAYING_DIR.resolve("1.json")
        assert(path.toFile().exists())
        val content = path.toFile().readText()
        assert(content.contains("명언1"))
        assert(content.contains("작가1"))
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
    @DisplayName("목록 - 내용 검색")
    fun testListWithContentSearch() {
        val result = TestRunner.run(
            """
                등록
                명언1과 키워드
                작가1
                등록
                명언2
                작가2
                등록
                명언3과 키워드
                작가3
                목록?keywordType=content&keyword=키워드
                종료
            """
        )

        assert(result.contains("검색타입 : content"))
        assert(result.contains("검색어 : 키워드"))
        assert(result.contains("번호 / 작가 / 명언"))
        assert(result.contains("3 / 작가3 / 명언3과 키워드"))
        assert(result.contains("1 / 작가1 / 명언1과 키워드"))
        assert(!result.contains("2 / 작가2 / 명언2"))
    }

    @Test
    @DisplayName("목록 - 작가 검색")
    fun testListWithAuthorSearch() {
        val result = TestRunner.run(
            """
                등록
                명언1
                작가1
                등록
                명언2
                작가2
                등록
                명언3
                작가1
                목록?keywordType=author&keyword=작가1
                종료
            """
        )

        assert(result.contains("검색타입 : author"))
        assert(result.contains("검색어 : 작가1"))
        assert(result.contains("번호 / 작가 / 명언"))
        assert(result.contains("3 / 작가1 / 명언3"))
        assert(result.contains("1 / 작가1 / 명언1"))
        assert(!result.contains("2 / 작가2 / 명언2"))
    }

    @Test
    @DisplayName("목록 - 페이징")
    fun testListWithPaging() {
        TestRunner.makeSampleData(25)

        val result1 = TestRunner.run(
            """
                목록?pageSize=10&page=1
                종료
            """
        )
        assert(result1.contains("25 / 작가25 / 명언25"))
        assert(result1.contains("16 / 작가16 / 명언16"))
        assert(result1.contains("페이지 : [1] / 2 / 3"))
        assert(!result1.contains("15 / 작가15 / 명언15"))

        val result2 = TestRunner.run(
            """
                목록?pageSize=10&page=2
                종료
            """
        )
        assert(result2.contains("15 / 작가15 / 명언15"))
        assert(result2.contains("6 / 작가6 / 명언6"))
        assert(result2.contains("페이지 : 1 / [2] / 3"))
        assert(!result2.contains("16 / 작가6 / 명언6"))
        assert(!result2.contains("5 / 작가5 / 명언5"))

        val result3 = TestRunner.run(
            """
                목록?pageSize=10&page=3
                종료
            """
        )
        assert(result3.contains("5 / 작가5 / 명언5"))
        assert(result3.contains("1 / 작가1 / 명언1"))
        assert(result3.contains("페이지 : 1 / 2 / [3]"))
        assert(!result3.contains("6 / 작가6 / 명언6"))

        val result4 = TestRunner.run(
            """
                목록?pageSize=5&page=4
                종료
            """
        )
        assert(result4.contains("10 / 작가10 / 명언10"))
        assert(result4.contains("6 / 작가6 / 명언6"))
        assert(result4.contains("페이지 : 1 / 2 / 3 / [4] / 5"))
        assert(!result4.contains("11 / 작가11 / 명언11"))
        assert(!result4.contains("5 / 작가5 / 명언5"))
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

        val path = AppPaths.WISE_SAYING_DIR.resolve("2.json")
        assert(!path.toFile().exists())
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

    @Test
    @DisplayName("빌드")
    fun testBuild() {
        TestRunner.makeSampleData(3)
        val result = TestRunner.run(
            """
                빌드
                종료
            """
        )

        assert(result.contains("data.json 파일의 내용이 갱신되었습니다."))

        val path = AppPaths.DATA_FILE
        assert(path.toFile().exists())
        val content = path.toFile().readText()
        assert(content.contains("명언1"))
        assert(content.contains("작가1"))
        assert(content.contains("명언2"))
        assert(content.contains("작가2"))
        assert(content.contains("명언3"))
        assert(content.contains("작가3"))
    }
}