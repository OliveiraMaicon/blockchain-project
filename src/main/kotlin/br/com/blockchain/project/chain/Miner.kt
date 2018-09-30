package br.com.blockchain.project.chain

import br.com.blockchain.project.helpers.SHA256
import com.google.gson.Gson

import java.util.ArrayList

/**
 *
 * Simulates a Miner that performs a Proof of Work consensus mechanism
 * transactions created in the network.
 *
 * This is used for Unit testing, in a real blockchain network,
 * miners would listen for transactions nodes created and then perform
 * POW and create a block, which would be sent back to network, and validated
 * by each node.
 *
 */
class Miner<T : Tx>(chain: SimpleBlockchain<T>) {

    private var transactionPool: MutableList<T> = ArrayList()
    private var chain: SimpleBlockchain<T>? = null

    init {
        this.chain = chain
    }

    fun mine(tx: T) {
        transactionPool.add(tx)
        if (transactionPool.size > SimpleBlockchain.BLOCK_SIZE) {
            createBlockAndApplyToChain()
        }
    }

    private fun createBlockAndApplyToChain() {

        val block = chain!!.newBlock()
        // set previous hash with current hash
        block.previousHash = chain!!.head!!.hash
        // set block hashes from POW
        // block
        block.hash = proofOfWork(block)
        chain!!.addAndValidateBlock(block)
        // empty pool
        transactionPool = ArrayList()
    }

    private fun proofOfWork(block: Block<*>): String {

        val nonceKey = block.nonce
        var nonce: Long = 0
        var nonceFound = false
        var nonceHash = ""

        val parser = Gson()
        val serializedData = parser.toJson(transactionPool)
        val message = ((block.timeStamp + block.index).toString() + block.merkleRoot + serializedData
                + block.previousHash)

        while (!nonceFound) {

            nonceHash = SHA256.generateHash(message + nonce)
            nonceFound = nonceHash.substring(0, nonceKey.length) == nonceKey
            nonce++

        }

        return nonceHash

    }

}
