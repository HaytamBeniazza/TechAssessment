package com.haytam.miniprject.util;

import com.github.javafaker.Faker;
import com.haytam.miniprject.entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class UserDataGenerator {

    private static final Faker faker = new Faker();
    private static final PasswordEncoder passwordEncoder = new PasswordEncoder();

    public static List<User> generateUsers(int count) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            User user = new User();
            user.setFirstName(faker.name().firstName());
            user.setLastName(faker.name().lastName());
            user.setBirthDate(faker.date().past(365 * 30, TimeUnit.DAYS)
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            user.setCity(faker.address().city());
            user.setCountry(faker.address().countryCode());
            user.setAvatar(faker.internet().avatar());
            user.setCompany(faker.company().name());
            user.setJobPosition(faker.job().position());
            user.setMobile(faker.phoneNumber().cellPhone());
            user.setUsername(faker.name().username());
            user.setEmail(faker.internet().emailAddress());

            String rawPassword = "password123";
            user.setPassword(passwordEncoder.encode(rawPassword));

            user.setRole(faker.bool().bool() ? "admin" : "user");
            users.add(user);
        }
        return users;
    }
}
