package br.com.blockchain.project.domain.model

import br.com.blockchain.project.infraestructure.HashUtils
import java.util.*


data class Block(var hash: String,
                 var previousHash: String,
                 var data: String,
                 var timeStamp: Long) {
    private var nonce: Int = 0

    constructor(data: String, previousHash: String) : this("", "", "", 0) {
        this.data = data
        this.previousHash = previousHash
        this.timeStamp = Date().time
        this.hash = calculateHash()
    }


    fun calculateHash(): String {
        return HashUtils.applySha256(
                previousHash +
                        timeStamp +
                        data
        )
    }

    //Increases nonce value until hash target is reached.
    fun mineBlock(difficulty: Int) {
        val target = HashUtils.getDificultyString(difficulty) //Create a string with difficulty * "0"
        while (hash.substring(0, difficulty) != target) {
            nonce++
            hash = calculateHash()
        }
        System.out.println("Block Mined!!! : $hash")
    }
}