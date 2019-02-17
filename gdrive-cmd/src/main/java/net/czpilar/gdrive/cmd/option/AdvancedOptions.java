package net.czpilar.gdrive.cmd.option;

import org.apache.commons.cli.Option;

import java.util.List;

/**
 * Extension of {@link org.apache.commons.cli.Options}.
 *
 * @author David Pilar (david@czpilar.net)
 */
public class AdvancedOptions extends org.apache.commons.cli.Options {

    public void setOptions(List<Option> options) {
        if (options != null) {
            for (Option option : options) {
                addOption(option);
            }
        }
    }
}
