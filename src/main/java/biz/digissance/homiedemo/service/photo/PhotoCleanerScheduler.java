package biz.digissance.homiedemo.service.photo;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
class PhotoCleanerScheduler {

    private final PhotoService photoService;

    PhotoCleanerScheduler(final PhotoService photoService) {
        this.photoService = photoService;
    }

    @Scheduled(cron = "0/15 * * * * ?")
    @SchedulerLock(name = "TaskScheduler_scheduledTask",
            lockAtLeastForString = "PT5S", lockAtMostForString = "PT5M")
    public void scheduledTask() {
        log.info("Running photo cleaner");
        photoService.removeOrphanPhotos();
        log.info("photo cleaner ended");
    }
}