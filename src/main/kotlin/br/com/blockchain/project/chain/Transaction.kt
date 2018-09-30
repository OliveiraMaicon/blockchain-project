package br.com.blockchain.project.chain


import br.com.blockchain.project.helpers.SHA256

class Transaction(var value: String) : Tx {

    private var hash: String = SHA256.generateHash(value)
    // new value need to recalc hash


    /* var value: String? = null
        set(value) {

        }*/

    override fun hash(): String {
        return hash
    }

    init {
        this.hash = SHA256.generateHash(value)
        this.value = value
    }

    override fun toString(): String {
        return this.hash + " : " + this.value
    }


}
