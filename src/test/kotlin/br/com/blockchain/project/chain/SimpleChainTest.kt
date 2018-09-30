package br.com.blockchain.project.chain

import org.junit.Test
import java.util.Objects

import org.junit.Assert.*

class SimpleChainTest {

    @Test
    fun testBlockchain() {

        val chain1 = SimpleBlockchain<Transaction>()

        chain1.add(Transaction("A")).add(Transaction("B")).add(Transaction("C"))

        val chain2 = chain1.Clone()

        chain1.add(Transaction("D"))

        println(String.format("Chain 1 Hash: %s", Objects.requireNonNull<Block<Transaction>>(chain1.head).hash))
        println(String.format("Chain 2 Hash: %s", Objects.requireNonNull<Block<Transaction>>(chain2.head).hash))
        println(
                String.format("Chains Are In Sync: %s", chain1.head!!.hash == chain2.head!!.hash))

        chain2.add(Transaction("D"))

        println(String.format("Chain 1 Hash: %s", chain1.head!!.hash))
        println(String.format("Chain 2 Hash: %s", chain2.head!!.hash))
        println(
                String.format("Chains Are In Sync: %s", chain1.head!!.hash == chain2.head!!.hash))

        assertEquals(chain1.blockChainHash(), chain2.blockChainHash())

        println("Current Chain Head Transactions: ")
        for (block in chain1.getChain()) {
            println(block.previousHash)
            for (tx in block.transactions) {
                println("\t" + tx)
            }
        }

        // Block Merkle root should equal root hash in Merkle Tree computed from
        // block transactions
        val headBlock = chain1.head
        val merkleTree = headBlock!!.merkleTree()
        assertEquals(headBlock.merkleRoot, merkleTree[merkleTree.size - 1])

        // Validate block chain
        assertTrue(chain1.validate())
        println(String.format("Chain is Valid: %s", chain1.validate()))

    }

    @Test
    fun merkleTreeTest() {

        // create chain, add transaction

        val chain1 = SimpleBlockchain<Transaction>()

        chain1.add(Transaction("A")).add(Transaction("B")).add(Transaction("C")).add(Transaction("D"))

        // get a block in chain
        val block = chain1.head

        println("Merkle Hash tree :" + block!!.merkleTree())

        // get a transaction from block
        val tx = block.transactions[0]

        // see if block transactions are valid, they should be
        block.transasctionsValid()
        assertTrue(block.transasctionsValid())

        // mutate the data of a transaction
        tx.value = "Z"

        // block should no longer be valid, blocks MerkleRoot does not match computed merkle tree of transactions
        assertFalse(block.transasctionsValid())

    }

    @Test
    fun blockMinerTest() {

        // create 30 transactions, that should result in 3 blocks in the chain.
        val chain = SimpleBlockchain<Transaction>()

        // Respresents a proof of work miner
        // Creates
        val miner = Miner(chain)

        // This represents transactions being created by a network
        for (i in 0..29) {
            miner.mine(Transaction("" + i))
        }

        println("Number of Blocks Mined = " + chain.getChain().size)
        assertTrue(chain.getChain().size == 3)

    }

    @Test
    fun testValidateBlockchain() {

        val chain = SimpleBlockchain<Transaction>()
        // add 30 transaction should result in 3 blocks in chain.
        for (i in 0..29) {
            chain.add(Transaction("tx:$i"))
        }

        // is chain valid
        println(String.format("Chain is Valid: %s", chain.validate()))

        // get second block from chain and add a tx..
        val block = chain.getChain()[1]
        val tx = Transaction("X")
        block.add(tx)

        // is chain valid, should not be changed a block...
        println(String.format("Chain is Valid: %s", chain.validate()))


    }

}
