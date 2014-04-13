package net.czpilar.gdrive.core.service.impl;

import net.czpilar.gdrive.core.credential.IGDriveCredential;
import net.czpilar.gdrive.core.setting.GDriveSetting;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Template service.
 *
 * @author David Pilař (david@czpilar.net)
 */
public abstract class AbstractService {

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

}
