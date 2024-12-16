package net.czpilar.gdrive.core.listener;

import com.google.api.client.googleapis.media.MediaHttpUploader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static com.google.api.client.googleapis.media.MediaHttpUploader.UploadState.*;
import static org.mockito.Mockito.*;

/**
 * @author David Pilar (david@czpilar.net)
 */
public class FileUploadProgressListenerTest {

    private final FileUploadProgressListener listener = new FileUploadProgressListener("test-file-name");

    @Mock
    private MediaHttpUploader uploader;

    private AutoCloseable autoCloseable;

    @BeforeEach
    public void before() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void after() throws Exception {
        autoCloseable.close();
    }

    @Test
    public void testProgressChangedInitiationStarted() throws IOException {
        when(uploader.getUploadState()).thenReturn(INITIATION_STARTED);

        listener.progressChanged(uploader);

        verify(uploader).getUploadState();

        verifyNoMoreInteractions(uploader);
    }

    @Test
    public void testProgressChangedInitiationComplete() throws IOException {
        when(uploader.getUploadState()).thenReturn(INITIATION_COMPLETE);

        listener.progressChanged(uploader);

        verify(uploader).getUploadState();

        verifyNoMoreInteractions(uploader);
    }

    @Test
    public void testProgressChangedMediaInComplete() throws IOException {
        when(uploader.getUploadState()).thenReturn(MEDIA_IN_PROGRESS);
        when(uploader.getProgress()).thenReturn(0.23);

        listener.progressChanged(uploader);

        verify(uploader).getUploadState();
        verify(uploader).getProgress();

        verifyNoMoreInteractions(uploader);
    }

    @Test
    public void testProgressChangedMediaComplete() throws IOException {
        when(uploader.getUploadState()).thenReturn(MEDIA_COMPLETE);

        listener.progressChanged(uploader);

        verify(uploader).getUploadState();

        verifyNoMoreInteractions(uploader);
    }

    @Test
    public void testProgressChangedNotStarted() throws IOException {
        when(uploader.getUploadState()).thenReturn(NOT_STARTED);

        listener.progressChanged(uploader);

        verify(uploader).getUploadState();

        verifyNoMoreInteractions(uploader);
    }
}
