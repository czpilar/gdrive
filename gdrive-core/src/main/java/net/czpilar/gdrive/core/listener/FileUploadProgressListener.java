package net.czpilar.gdrive.core.listener;

import java.io.IOException;
import java.text.NumberFormat;

import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Listener used for printing progress of uploading file.
 *
 * @author David Pilar (david@czpilar.net)
 */
public class FileUploadProgressListener implements MediaHttpUploaderProgressListener {

	private static final Logger LOG = LoggerFactory.getLogger(FileUploadProgressListener.class);

	private final String filename;

	public FileUploadProgressListener(String filename) {
		this.filename = filename;
	}

	@Override
	public void progressChanged(MediaHttpUploader uploader) throws IOException {
		switch (uploader.getUploadState()) {
			case INITIATION_STARTED:
				LOG.info("Started initiation uploading file {}", filename);
				break;
			case INITIATION_COMPLETE:
				LOG.info("Finished initiation uploading file {}", filename);
				break;
			case MEDIA_IN_PROGRESS:
				LOG.info("Uploaded {} of file {}", NumberFormat.getPercentInstance().format(uploader.getProgress()), filename);
				break;
			case MEDIA_COMPLETE:
				LOG.info("Finished uploading file {}", filename);
				break;
		}
	}
}