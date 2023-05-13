package biz.digissance.homiedemo.service.photo;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
public class PhotoCleanerScheduler {

    private final PhotoService photoService;

    public PhotoCleanerScheduler(final PhotoService photoService) {
        this.photoService = photoService;
    }

    @Scheduled(cron = "0/15 * * * * ?")
    @SchedulerLock(name = "TaskScheduler_scheduledTask",
            lockAtLeastFor = "PT5S", lockAtMostFor = "PT5M")
    public void scheduledTask() {
        log.info("Running photo cleaner");
        photoService.removeOrphanPhotos();
        log.info("photo cleaner ended");
    }
}