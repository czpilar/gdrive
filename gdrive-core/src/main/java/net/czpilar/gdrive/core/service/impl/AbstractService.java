package net.czpilar.gdrive.core.service.impl;

import com.google.api.services.drive.Drive;
import net.czpilar.gdrive.core.credential.IGDriveCredential;
import net.czpilar.gdrive.core.setting.GDriveSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * Template service.
 *
 * @author David Pilar (david@czpilar.net)
 */
public abstract class AbstractService {

	private ApplicationContext applicationContext;
	private GDriveSetting gDriveSetting;
	private IGDriveCredential gDriveCredential;

	@Autowired
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

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
		return applicationContext.getBean(Drive.class);
	}

}
