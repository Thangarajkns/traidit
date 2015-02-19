/**
 * @since 07-Jan-2015
 */
package org.kns.traidit.common.service;

import javax.annotation.Resource;

import org.kns.traidit.frontend.notification.service.NotificationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Thangaraj(KNSTEK)
 *
 */
@Component
public class ScheduledJobs {

	@Resource(name = "notificationService")
	private NotificationService notificationService;
	
	@Scheduled(fixedDelayString = "1000000")
	public void removeInactiveDevicesFromDb(){
		this.notificationService.removeInActiveDevicesFromDB();
	}

}
