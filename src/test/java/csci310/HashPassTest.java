/**
 * 
 */
package csci310;

import static org.junit.Assert.*;

import java.security.NoSuchAlgorithmException;

import org.junit.Test;

/**
 * @author vonzg
 *
 */
public class HashPassTest {

	/**
	 * Test method for {@link csci310.HashPass#makePasswordSecure(java.lang.String, byte[])}.
	 * @throws NoSuchAlgorithmException 
	 */
	@Test
	public void testMakePasswordSecure() throws NoSuchAlgorithmException {
		String password = "Hallu123";
		String secured = HashPass.makePasswordSecure(password);
		// check that output is 64 hex characters
		assertEquals(secured.length(), 64);
		// check that secured password is not the same as user's actual password
		assertNotEquals(secured, password);
		
		// ensure that repeats of the password returns the same value
		String sameSecured = HashPass.makePasswordSecure(password);
		assertEquals(sameSecured, secured);
	}
}
