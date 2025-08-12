package org.example.common.io

import java.nio.file.Path

object AppPaths {
    private val BASE_DB_DIR: Path = Path.of("db")
    val WISE_SAYING_DIR: Path = BASE_DB_DIR.resolve("wiseSaying")
    val LAST_ID_FILE: Path = WISE_SAYING_DIR.resolve("lastId.txt")
    val DATA_FILE: Path = WISE_SAYING_DIR.resolve("data.json")
}