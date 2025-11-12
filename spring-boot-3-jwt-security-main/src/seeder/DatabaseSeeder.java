package library.seeder;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final UserSeeder userSeeder;
    private final BookSeeder bookSeeder;
    @Override
    public void run(String... args) throws Exception {

                userSeeder.seed();
                bookSeeder.seed();
    }
}
