package br.com.blockchain.project.helpers

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.experimental.and

object SHA256 {

    fun generateHash(value: String): String {
        var hash = ""
        try {
            val md = MessageDigest.getInstance("SHA-256")
            val bytes = md.digest(value.toByteArray(StandardCharsets.UTF_8))
            val sb = StringBuilder()
            for (i in bytes.indices) {
                sb.append(Integer.toString((bytes[i].toInt() and 0xff) + 0x100, 16).substring(1))
            }
            hash = sb.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        return hash
    }
}
