package com.main.scheduler;

import com.main.services.PostgresBackupService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BackupScheduler {

    private final PostgresBackupService backupService;

    public BackupScheduler(PostgresBackupService backupService) {
        this.backupService = backupService;
    }

    // Every day at 2 AM
  //  @Scheduled(cron = "0 0 2 * * *")
    //  Runs EVERY MINUTE
    @Scheduled(cron = "0 * * * * *")
    public void runDailyBackup() {
        backupService.backup();
    }
}
