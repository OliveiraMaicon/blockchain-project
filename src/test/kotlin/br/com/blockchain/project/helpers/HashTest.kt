package br.com.blockchain.project.helpers

import org.junit.Test

import org.junit.Assert.assertEquals


class HashTest {

    @Test
    fun test() {

        val hash = SHA256.generateHash("TEST String")
        println(hash)
        assertEquals(64, hash.length.toLong())
        assertEquals("48ec9ab2710338d58ac4328ea9d47cf483d91082271541e5da43b0b583061183", hash)
    }

}
