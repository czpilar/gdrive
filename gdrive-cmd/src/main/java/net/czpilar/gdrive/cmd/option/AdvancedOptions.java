package net.czpilar.gdrive.cmd.option;

import java.util.List;

import org.apache.commons.cli.Option;

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
