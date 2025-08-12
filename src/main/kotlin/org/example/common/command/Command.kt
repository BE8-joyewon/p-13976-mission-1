package org.example.common.command

data class Command(val action: String, val params: Map<String, String>) {
    companion object {
        fun parse(line: String): Command? {
            val trimmed = line.trim()
            if (trimmed.isEmpty()) return null

            val (action, query) = trimmed.split("?", limit = 2).let {
                it[0].trim() to it.getOrNull(1)
            }
            if (action.isEmpty()) return null

            val params = mutableMapOf<String, String>()
            if (!query.isNullOrBlank()) {
                query.split("&")
                    .asSequence()
                    .mapNotNull { part ->
                        val kv = part.split("=", limit = 2)
                        if (kv.size == 2) {
                            val key = kv[0].trim()
                            val value = java.net.URLDecoder.decode(kv[1].trim(), java.nio.charset.StandardCharsets.UTF_8)
                            key to value
                        } else null
                    }
                    .forEach { (k, v) -> params[k] = v }
            }

            return Command(action, params)
        }
    }
}