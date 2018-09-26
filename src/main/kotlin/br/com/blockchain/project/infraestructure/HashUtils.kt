package br.com.blockchain.project.infraestructure

import java.security.MessageDigest
import com.google.gson.GsonBuilder




class HashUtils {


    companion object {
        fun applySha256(input : String) : String {
            try {
                val digest = MessageDigest.getInstance("SHA-256")
                //Applies sha256 to our input,
                val hash = digest.digest(input.toByteArray(Charsets.UTF_8))
                val hexString = StringBuffer() // This will contain hash as hexidecimal

                for (i in hash.indices) {
                    val hex = Integer.toHexString(0xff and hash[i].toInt())
                    if (hex.length == 1) hexString.append('0')
                    hexString.append(hex)
                }
                return hexString.toString()
            } catch (e: Exception) {
                throw RuntimeException(e)
            }

        }


        //Short hand helper to turn Object into a json string
        fun getJson(o: Any): String {
            return GsonBuilder().setPrettyPrinting().create().toJson(o)
        }

        //Returns difficulty string target, to compare to hash. eg difficulty of 5 will return "00000"
        fun getDificultyString(difficulty: Int): String {
            return String(CharArray(difficulty)).replace('\u0000', '0')
        }
    }

}
