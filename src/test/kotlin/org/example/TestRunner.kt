package org.example

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.PrintStream

object TestRunner {
    private val originalIn: InputStream = System.`in`
    private val originalOut: PrintStream = System.out

    fun run(input: String): String {
        val formattedInput = input
            .trimIndent()

        return ByteArrayOutputStream().use { outputStream ->
            PrintStream(outputStream).use { printStream ->
                try {
                    System.setIn(
                        ByteArrayInputStream(
                            formattedInput.toByteArray()
                        )
                    )

                    System.setOut(printStream)

                    App().run()
                } finally {
                    System.setIn(originalIn)
                    System.setOut(originalOut)
                }
            }

            outputStream
                .toString()
                .trim()
                .replace("\r\n", "\n")
        }
    }

    fun makeSampleData(size: Int) {
        val input = (1..size)
            .joinToString("\n") { i ->
                "등록\n명언$i\n작가$i\n"
            } + "종료\n"

        run(input)
    }
}