package net.czpilar.gdrive.cmd;

import net.czpilar.gdrive.cmd.context.GDriveCmdContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author David Pilar (david@czpilar.net)
 */
@ExtendWith(SpringExtension.class)
@Import(GDriveCmdContext.class)
public class ApplicationContextTest {

    @Test
    public void test() {
    }
}
