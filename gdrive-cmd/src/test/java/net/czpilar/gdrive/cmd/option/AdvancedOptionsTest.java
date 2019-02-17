package net.czpilar.gdrive.cmd.option;

import org.apache.commons.cli.Option;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author David Pilar (david@czpilar.net)
 */
public class AdvancedOptionsTest {

    private AdvancedOptions options;

    private Option option1 = new Option("1", "description of option 1");

    private Option option2 = new Option("2", "description of option 2");

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        options = new AdvancedOptions();
    }

    @Test
    public void testSetOptionsWhereInputIsNull() {
        options.setOptions(null);

        assertNotNull(options.getOptions());
        assertEquals(0, options.getOptions().size());
    }

    @Test
    public void testSetOptions() {
        options.setOptions(Arrays.asList(option1, option2));

        assertNotNull(options.getOptions());
        assertEquals(2, options.getOptions().size());
        assertEquals(option1, options.getOption("1"));
        assertEquals(option2, options.getOption("2"));
    }
}
