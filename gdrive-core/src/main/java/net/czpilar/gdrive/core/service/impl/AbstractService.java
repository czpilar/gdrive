package net.czpilar.gdrive.core.service.impl;

import com.google.api.services.drive.Drive;
import net.czpilar.gdrive.core.credential.IGDriveCredential;
import net.czpilar.gdrive.core.setting.GDriveSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

/**
 * Template service.
 *
 * @author David Pilar (david@czpilar.net)
 */
public abstract class AbstractService {

    private Drive drive;
    private GDriveSetting gDriveSetting;
    private IGDriveCredential gDriveCredential;

    protected GDriveSetting getGDriveSetting() {
        return gDriveSetting;
    }

    @Autowired
    public void setGDriveSetting(GDriveSetting gDriveSetting) {
        this.gDriveSetting = gDriveSetting;
    }

    protected IGDriveCredential getGDriveCredential() {
        return gDriveCredential;
    }

    @Autowired
    public void setGDriveCredential(IGDriveCredential gDriveCredential) {
        this.gDriveCredential = gDriveCredential;
    }

    protected Drive getDrive() {
        return drive;
    }

    @Lazy
    @Autowired
    public void setDrive(Drive drive) {
        this.drive = drive;
    }

}
