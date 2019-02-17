package net.czpilar.gdrive.core.listener;

import com.google.api.client.googleapis.media.MediaHttpUploader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;

import static com.google.api.client.googleapis.media.MediaHttpUploader.UploadState.*;
import static org.mockito.Mockito.*;

/**
 * @author David Pilar (david@czpilar.net)
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(MediaHttpUploader.class)
public class FileUploadProgressListenerTest {

    private FileUploadProgressListener listener = new FileUploadProgressListener("test-file-name");

    @Mock
    private MediaHttpUploader uploader;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
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
