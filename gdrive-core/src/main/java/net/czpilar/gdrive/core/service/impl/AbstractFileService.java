package net.czpilar.gdrive.core.service.impl;

import com.google.api.services.drive.model.File;
import net.czpilar.gdrive.core.exception.FileHandleException;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.List;

import static net.czpilar.gdrive.core.util.EscapeUtils.escapeSingleQuote;

/**
 * Base service for file and directory common functions.
 *
 * @author David Pilar (david@czpilar.net)
 */
public abstract class AbstractFileService extends AbstractService {

    public static final String DIRECTORY_MIME_TYPE = "application/vnd.google-apps.folder";

    private static final Logger LOG = LoggerFactory.getLogger(AbstractFileService.class);

    protected String buildQuery(String filename, File parent, boolean directory) {
        Assert.notNull(filename, "Filename must not be null.");

        StringBuilder sb = new StringBuilder();
        sb.append("name = '").append(escapeSingleQuote(filename)).append("'");
        sb.append(" and '").append(parent == null ? "root" : parent.getId()).append("' in parents");
        sb.append(" and trashed = false");
        sb.append(" and mimeType ").append(directory ? "=" : "!=").append(" '").append(DIRECTORY_MIME_TYPE).append("'");
        return sb.toString();
    }

    protected File findFile(String filename, File parent, boolean directory) {
        Assert.notNull(filename);

        File file = null;
        try {
            List<File> items = getDrive().files().list().setQ(buildQuery(filename, parent, directory)).execute().getFiles();
            if (CollectionUtils.isNotEmpty(items)) {
                if (items.size() > 1) {
                    throw new FileHandleException("Too many items found for filename " + filename + ".");
                }
                file = items.get(0);
            }
        } catch (IOException e) {
            LOG.error("Unable to find {}.", filename);
            throw new FileHandleException("Unable to find file.", e);
        }
        return file;
    }

}
