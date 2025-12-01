package com.main.seeder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final UserSeeder userSeeder;
    private final BookSeeder bookSeeder;

    public DatabaseSeeder(UserSeeder userSeeder, BookSeeder bookSeeder) {
        this.userSeeder = userSeeder;
        this.bookSeeder = bookSeeder;
    }

    @Override
    public void run(String... args) throws Exception {
       // userSeeder.seed();
        bookSeeder.seed();
    }
}
