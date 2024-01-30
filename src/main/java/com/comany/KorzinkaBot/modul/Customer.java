package com.comany.KorzinkaBot.modul;


import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = ("users"))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long chatId;
    private String fullName;
    private String userName;
    private String phoneNumber;
    private String home;
    private String data;
    private String jinsi;
    private String sendMessage;

}
