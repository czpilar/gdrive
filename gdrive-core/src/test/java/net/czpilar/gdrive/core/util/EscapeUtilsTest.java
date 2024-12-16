package net.czpilar.gdrive.core.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author David Pilar (david@czpilar.net)
 */
public class EscapeUtilsTest {

    @Test
    public void testEscapeSingleQuoteWhereInputIsNull() {
        assertNull(EscapeUtils.escapeSingleQuote(null));
    }

    @Test
    public void testEscapeSingleQuote() {
        String value = "test ' test \\' test \\\\' test \\\\\\' test \\\\\\\\' test \\\\\\\\\\' test";

        String result = EscapeUtils.escapeSingleQuote(value);

        assertNotNull(result);
        assertEquals("test \\' test \\' test \\\\\\' test \\\\\\' test \\\\\\\\\\' test \\\\\\\\\\' test", result);
    }
}
