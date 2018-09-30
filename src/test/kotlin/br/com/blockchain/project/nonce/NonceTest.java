package br.com.blockchain.project.nonce;

import br.com.blockchain.project.helpers.SHA256;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class NonceTest {

	@Test
	public void test() {
		String message = "Keyhole Software";

		System.out.println("Message: " + message);

		String hashValue = SHA256.INSTANCE.generateHash(message);

		System.out.println(String.format("Hash: %s", hashValue));

		String nonceKey = "54321";
		// E.g. "00000" :
		System.out.println(new String(new char[nonceKey.length()]));
		String zeroGoal = new String(new char[nonceKey.length()]).replace("\0", "0");
		System.out.println(zeroGoal);
		long nonce = 0;
		boolean isNonceFound = false;
		String nonceHash = "";
         
		long start = System.currentTimeMillis();

		while (!isNonceFound) {

			nonceHash = SHA256.INSTANCE.generateHash(message + nonce);
			//System.out.println("H:" + nonceHash);
			//System.out.println("S:" + nonceHash.substring(0, nonceKey.length()) + "Z:" + zeroGoal);
			isNonceFound = nonceHash.substring(0, nonceKey.length()).equals(zeroGoal);
			if (!isNonceFound) {
				nonce++;
			}
		}

		long ms = System.currentTimeMillis() - start;

		System.out.println(String.format("Nonce: %d", nonce));
		System.out.println(String.format("Nonce Hash: %s", nonceHash));
		System.out.println(String.format("Nonce Search Time: %s ms", ms));
		
		assertTrue(isNonceFound);

	}

}
