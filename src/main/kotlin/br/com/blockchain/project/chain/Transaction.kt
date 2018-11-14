package br.com.blockchain.project.chain


import br.com.blockchain.project.helpers.SHA256

class Transaction : Tx {


    private var hash: String
    // new value need to recalc hash

     var value: String = ""
         set(value) {
             this.hash = SHA256.generateHash(value)
        }

    constructor()

    constructor(value: String) : this(){
        this.hash = SHA256.generateHash(value)
        this.value = value
    }

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
