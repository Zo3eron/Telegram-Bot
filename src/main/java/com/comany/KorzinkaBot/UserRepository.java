package com.comany.KorzinkaBot;

import com.comany.KorzinkaBot.modul.Customer;
import com.comany.KorzinkaBot.modul.CustomerDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Locale;
import java.util.Optional;
import java.util.OptionalInt;

@Repository
public interface UserRepository extends JpaRepository<Customer, Long> {
        Optional<Customer> findByChatId(Long chatId);
}
