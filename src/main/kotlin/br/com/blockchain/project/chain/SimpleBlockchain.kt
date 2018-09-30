package br.com.blockchain.project.chain

import java.util.ArrayList
import java.util.function.Consumer
import java.util.function.Predicate

class SimpleBlockchain<T : Tx>() {
    private var chain: MutableList<Block<T>> = ArrayList()

    val head: Block<T>?
        get() {

            val result: Block<T>?
            if (this.chain.size > 0) {
                result = this.chain[this.chain.size - 1]
            } else {

                throw RuntimeException("No Block's have been added to chain...")
            }

            return result
        }

    init {
        // create genesis block
        chain.add(newBlock())
    }

    constructor(blocks: MutableList<Block<T>>) : this() {
        chain = blocks
    }

    fun addAndValidateBlock(block: Block<T>) {

        // compare previous block hash back to genesis hash
        var current = block
        for (i in chain.indices.reversed()) {
            val b = chain[i]
            if (b.hash == current.previousHash) {
                current = b
            } else {

                throw RuntimeException("Block Invalid")
            }

        }

        this.chain.add(block)

    }

    fun validate(): Boolean {

        var previousHash = chain[0].hash
        for (block in chain) {
            val currentHash = block.hash
            if (currentHash != previousHash) {
                return false
            }

            previousHash = currentHash

        }

        return true

    }

    fun newBlock(): Block<T> {
        val count = chain.size
        var previousHash: String? = "root"

        if (count > 0)
            previousHash = blockChainHash()

        val block = Block<T>()

        block.timeStamp = System.currentTimeMillis()
        block.index = count
        block.previousHash = previousHash
        return block
    }

    fun add(item: T): SimpleBlockchain<T> {

        if (chain.size == 0) {
            // genesis block
            this.chain.add(newBlock())
        }

        // See if head block is full
        if (head!!.transactions.size >= BLOCK_SIZE) {
            this.chain.add(newBlock())
        }

        head!!.add(item)

        return this
    }

    /* Deletes the index of the after. */
   /* fun DeleteAfterIndex(index: Int) {
        if (index >= 0) {
            val predicate = { (b) -> chain.indexOf(b) >= index }
            chain.removeIf(predicate)
        }
    }*/

    fun Clone(): SimpleBlockchain<T> {
        val clonedChain = ArrayList<Block<T>>()
        chain.forEach{clonedChain.add(it.Clone())}
        return SimpleBlockchain(clonedChain)
    }

    fun getChain(): List<Block<T>> {
        return chain
    }

    fun setChain(chain: MutableList<Block<T>>) {
        this.chain = chain
    }

    /* Gets the root hash. */
    fun blockChainHash(): String? {
        return head!!.hash
    }

    companion object {
        const val BLOCK_SIZE = 10
    }

}