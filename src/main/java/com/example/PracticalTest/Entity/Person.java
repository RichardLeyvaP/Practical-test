package com.example.PracticalTest.Entity;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
@Data
@Entity
@Table(name = "tbl_person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID personId;

    @Column(nullable = false,length = 60)
    private String name;

    @Column(nullable = false,length = 60)
    private String lastName;

    @Column(nullable = false,length = 11,unique = true)
    private String cpf;

    @Column(nullable = false)
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate birthDate;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    private String sex;

    @Column(nullable = false)
    private LocalDateTime registrationDateInitial;

    @Column(nullable = false)
    private LocalDateTime registrationDateUpdate;

    private String phone;//optional

    private String description;//optional

}