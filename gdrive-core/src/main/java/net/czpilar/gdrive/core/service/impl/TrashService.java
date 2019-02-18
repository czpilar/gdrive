package net.czpilar.gdrive.core.service.impl;

import net.czpilar.gdrive.core.exception.TrashHandleException;
import net.czpilar.gdrive.core.service.ITrashService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Service containing methods for work with trash.
 *
 * @author David Pilar (david@czpilar.net)
 */
@Service
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
