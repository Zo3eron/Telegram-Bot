package com.comany.KorzinkaBot;

import com.comany.KorzinkaBot.modul.Customer;
import com.comany.KorzinkaBot.modul.CustomerDto;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {
    public CustomerDto toDto(Customer customer){
        return CustomerDto.builder()
                .chatId(customer.getChatId())
                .home(customer.getHome())
                .fullName(customer.getFullName())
                .userName(customer.getUserName())
                .data(customer.getData())
                .jinsi(customer.getJinsi())
                .build();
    }
    public Customer toEntity(CustomerDto dto){
        return Customer.builder()
                .chatId(dto.getChatId())
                .fullName(dto.getFullName())
                .userName(dto.getUserName())
                .home(dto.getHome())
                .data(dto.getData())
                .jinsi(dto.getJinsi())
                .build();
    }
}
