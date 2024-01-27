package com.comany.KorzinkaBot.modul;

import lombok.*;
import org.springframework.objenesis.SpringObjenesis;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {

    private Long chatId;
    private String fullName;
    private String userName;
    private String home;
    private String data;
    private String jinsi;
}
