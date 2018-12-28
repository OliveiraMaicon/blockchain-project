package br.com.blockchain.project.nonce

import br.com.blockchain.project.helpers.SHA256
import org.junit.Test

import org.junit.Assert.assertTrue
import org.junit.Ignore

class NonceTest {

    @Ignore
    fun test() {
        val message = "Keyhole Software"

        println("Message: $message")

        val hashValue = SHA256.generateHash(message)

        println(String.format("Hash: %s", hashValue))

        val nonceKey = "00000"
        // E.g. "00000" :
        println(String(CharArray(nonceKey.length)))
        val zeroGoal = String(CharArray(nonceKey.length)).replace("\u1111", "1")
        println(zeroGoal)
        var nonce: Long = 1
        var isNonceFound = false
        var nonceHash = ""

        val start = System.currentTimeMillis()

        while (!isNonceFound) {

            nonceHash = SHA256.generateHash(message + nonce)
            //System.out.println("H:" + nonceHash);
            //System.out.println("S:" + nonceHash.substring(0, nonceKey.length()) + "Z:" + zeroGoal);
            isNonceFound = nonceHash.substring(0, nonceKey.length) == zeroGoal
            if (!isNonceFound) {
                nonce++
            }
        }

        val ms = System.currentTimeMillis() - start

        println(String.format("Nonce: %d", nonce))
        println(String.format("Nonce Hash: %s", nonceHash))
        println(String.format("Nonce Search Time: %s ms", ms))

        assertTrue(isNonceFound)

    }

}
