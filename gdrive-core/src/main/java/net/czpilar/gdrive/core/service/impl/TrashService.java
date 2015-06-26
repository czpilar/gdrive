package net.czpilar.gdrive.core.service.impl;

import java.io.IOException;

import net.czpilar.gdrive.core.exception.TrashHandleException;
import net.czpilar.gdrive.core.service.ITrashService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service containing methods for work with trash.
 *
 * @author David Pilar (david@czpilar.net)
 */
public class TrashService extends AbstractService implements ITrashService {

    private static final Logger LOG = LoggerFactory.getLogger(TrashService.class);

    @Override
    public void empty() {
        try {
            LOG.info("Emptying trash.");
            getDrive().files().emptyTrash().execute();
        } catch (IOException e) {
            LOG.error("Unable to empty trash.");
            throw new TrashHandleException("Unable to empty trash.", e);
        }
    }
}
