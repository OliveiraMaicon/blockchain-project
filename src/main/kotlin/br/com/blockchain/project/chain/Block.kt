package br.com.blockchain.project.chain

import br.com.blockchain.project.helpers.SHA256
import com.google.gson.Gson

import java.util.ArrayList
import java.util.HashMap
import java.util.function.Consumer

class Block<T : Tx> {
    var timeStamp: Long = 0
    var index: Int = 0
    var transactions: ArrayList<T> = ArrayList()
    // calc hash if not defined, just for testing...
    var hash: String? = null
        get() {
            if (field == null) {
                computeHash()
            }

            return field
        }
    var previousHash: String? = null
    var merkleRoot: String? = null
    var nonce = "0000"


    // caches Transaction SHA256 hashes
    var map: MutableMap<String, T> = HashMap()

    fun add(tx: T): Block<T> {
        transactions.add(tx)
        map[tx.hash()] = tx
        computeMerkleRoot()
        computeHash()
        return this
    }


    fun computeMerkleRoot() {
        val treeList = merkleTree()
        // Last element is the merkle root hash if transactions
        merkleRoot = treeList[treeList.size - 1]
    }


    fun Clone(): Block<T> {
        // Object serialized then rehydrated into a new instance of an object so
        // memory conflicts don't happen
        // There are more efficent ways but this is the most reaadable
        val clone = Block<T>()
        clone.index = this.index
        clone.previousHash = this.previousHash
        clone.merkleRoot = this.merkleRoot
        clone.timeStamp = this.timeStamp
        //clone.setTransactions(this.getTransactions());

        val clonedtx = ArrayList<T>()


        this.transactions.forEach{ clonedtx.add(it)}
        clone.transactions = clonedtx

        return clone
    }

    fun transasctionsValid(): Boolean {

        val tree = merkleTree()
        val root = tree[tree.size - 1]
        return root.equals(this.merkleRoot,true)

    }

    /*

	    This method was adapted from the https://github.com/bitcoinj/bitcoinj project
	      Copyright 2011 Google Inc.
          Copyright 2014 Andreas Schildbach

	    The Merkle root is based on a tree of hashes calculated from the
	    transactions:

			 root
			 / \
			 A B
			 / \ / \
		   t1 t2 t3 t4

		The tree is represented as a list: t1,t2,t3,t4,A,B,root where each
		entry is a hash.

		The hashing algorithm is SHA-256. The leaves are a hash of the
		serialized contents of the transaction.
	    The interior nodes are hashes of the concatenation of the two child
	    hashes.

		This structure allows the creation of proof that a transaction was
		included into a block without having to
		provide the full block contents. Instead, you can provide only a
		Merkle branch. For example to prove tx2 was
		in a block you can just provide tx2, the hash(tx1) and A. Now the
		other party has everything they need to
		derive the root, which can be checked against the block header. These
		proofs aren't used right now but
		will be helpful later when we want to download partial block
		contents.

		Note that if the number of transactions is not even the last tx is
		repeated to make it so (see
		tx3 above). A tree with 5 transactions would look like this:

		        root
		 	    / \
		        1 5
		      / \ / \
		     2 3 4 4
		   / \ / \ / \
		t1 t2 t3 t4 t5 t5

	*/
    fun merkleTree(): List<String> {
        val tree = ArrayList<String>()
        // add all transactions as leaves of the tree.
        for (t in transactions) {
            tree.add(t.hash())
        }
        var levelOffset = 0 // first level

        // Iterate through each level, stopping when we reach the root (levelSize
        // == 1).
        var levelSize = transactions.size
        while (levelSize > 1) {
            // For each pair of nodes on that level:
            var left = 0
            while (left < levelSize) {
                // The right hand node can be the same as the left hand, in the
                // case where we don't have enough
                // transactions.
                val right = Math.min(left + 1, levelSize - 1)
                val tleft = tree[levelOffset + left]
                val tright = tree[levelOffset + right]
                tree.add(SHA256.generateHash(tleft + tright))
                left += 2
            }
            // Move to the next level.
            levelOffset += levelSize
            levelSize = (levelSize + 1) / 2
        }
        return tree
    }

    fun computeHash() {
        val parser = Gson()
        val serializedData = parser.toJson(transactions)
        hash = SHA256.generateHash("" + timeStamp + index + merkleRoot + serializedData + nonce + previousHash)
    }

}
