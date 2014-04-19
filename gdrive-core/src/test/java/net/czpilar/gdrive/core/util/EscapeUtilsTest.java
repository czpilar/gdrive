package net.czpilar.gdrive.core.util;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author David Pilar (david@czpilar.net)
 */
public class EscapeUtilsTest {

	@Test
	public void testEscapeSingleQuoteWhereInputIsNull() {
		String result = EscapeUtils.escapeSingleQuote(null);

		assertNull(result);
	}

	@Test
	public void testEscapeSingleQuote() {
		String value = "test ' test \\' test \\\\' test \\\\\\' test \\\\\\\\' test \\\\\\\\\\' test";

		String result = EscapeUtils.escapeSingleQuote(value);

		assertNotNull(result);
		assertEquals("test \\' test \\' test \\\\\\' test \\\\\\' test \\\\\\\\\\' test \\\\\\\\\\' test", result);
	}
}
