package br.com.blockchain.project

import br.com.blockchain.project.domain.model.Block
import br.com.blockchain.project.infraestructure.HashUtils
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import com.google.gson.GsonBuilder
import sun.plugin2.util.PojoUtil.toJson
import java.util.ArrayList




@SpringBootApplication
class Application {
    companion object {
        val blockchain = ArrayList<Block>()
        var difficulty: Int = 5

        @JvmStatic
        fun addBlock(newBlock: Block) {
            newBlock.mineBlock(difficulty)
            blockchain.add(newBlock)
        }

        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<Application>(*args)

            /* val genesisBlock = Block("Hi im the first block", "0")
             println("Hash for block 1 : " + genesisBlock.hash)

             val secondBlock = Block("Yo im the second block", genesisBlock.hash)
             println("Hash for block 2 : " + secondBlock.hash)

             val thirdBlock = Block("Hey im the third block", secondBlock.hash)
             println("Hash for block 3 : " + thirdBlock.hash)*/

            //add our blocks to the blockchain ArrayList:
            /*blockchain.add(Block("Hi im the first block", "0"))
            blockchain.add(Block("Yo im the second block", blockchain[blockchain.size - 1].hash))
            blockchain.add(Block("Hey im the third block", blockchain[blockchain.size - 1].hash))

            val blockchainJson = GsonBuilder().setPrettyPrinting().create().toJson(blockchain)
            println(blockchainJson)
            println(isChainValid())*/
            var newBlock = Block("Hi im the first block", "0")
            println("Trying to Mine block 1... ")
            newBlock.mineBlock(difficulty)
            blockchain.add(newBlock)
            //addBlock(Block("Hi im the first block", "0"))

            println("Trying to Mine block 2... ")
            addBlock(Block("Yo im the second block", blockchain[blockchain.size - 1].hash))

            println("Trying to Mine block 3... ")
            addBlock(Block("Hey im the third block", blockchain[blockchain.size - 1].hash))

            System.out.println("\nBlockchain is Valid: " + isChainValid())

            val blockchainJson = HashUtils.getJson(blockchain)
            println("\nThe block chain: ")
            println(blockchainJson)

        }


        fun isChainValid(): Boolean {
            var currentBlock: Block
            var previousBlock: Block

            //loop through blockchain to check hashes:
            for (i in 1 until blockchain.size) {
                currentBlock = blockchain[i]
                previousBlock = blockchain[i - 1]
                //compare registered hash and calculated hash:
                if (!currentBlock.hash.equals(currentBlock.calculateHash(),false)) {
                    println("Current Hashes not equal")
                    return false
                }
                //compare previous hash and registered previous hash
                if (!previousBlock.hash.equals(currentBlock.previousHash,false)) {
                    println("Previous Hashes not equal")
                    return false
                }
            }
            return true
        }
    }



}
