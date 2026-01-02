package com.main.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PostgresBackupService {

    @Value("${spring.datasource.password}")
    private String dbPassword;

    public void backup() {
        try {
            String backupDir = "C:\\backup";
            String backupFile = backupDir + "\\postgres_" + System.currentTimeMillis() + ".backup";

            ProcessBuilder processBuilder = new ProcessBuilder(
                    "C:\\Program Files\\PostgreSQL\\16\\bin\\pg_dump.exe",
                    "-h", "localhost",
                    "-p", "5432",
                    "-U", "postgres",
                    "-F", "c",
                    "-b",
                    "-v",
                    "-f", backupFile,
                    "postgres"
            );

            processBuilder.environment().put("PGPASSWORD", dbPassword);

            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("PostgreSQL backup failed, exit code=" + exitCode);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error while creating PostgreSQL backup", e);
        }
    }
}
