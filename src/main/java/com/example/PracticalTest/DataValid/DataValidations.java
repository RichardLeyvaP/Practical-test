package com.example.PracticalTest.DataValid;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DataValidations {

    @NotBlank(message = "Name is mandatory")
    @NotNull
    @Size(max = 60,message = "Maxim 60 characters")
    private String name;

    @NotBlank(message = "Last Name is mandatory")
    @NotNull
    @Size(max = 60,message = "Maxim 60 characters")
    private String lastName;

    @NotBlank(message = "Last Name is mandatory")
    @NotNull
    @Size(max = 11,min = 11,message = "11 characters")
    @Pattern(regexp = "[0-9]+",message = "CPF:Numeric field only")
    private String cpf;

    @NotBlank(message = "Last Name is mandatory")
    @NotNull
    @Size(max = 10,min = 10,message = "Format: yyyy-MM-dd")
    private String birthDate;

    @NotBlank(message = "Email is mandatory")
    @NotNull
    @Email(message = "This email has invalid format")
    private String email;

    @NotBlank(message = "Sex is mandatory")
    @NotNull
    private String sex;


    @Size(max = 13,min = 10,message = "10-13 characters")
    @Pattern(regexp = "[0-9]+",message = "Phone:Numeric field only")
    private String phone;//optional

    private String description;//optional
    private LocalDateTime registrationDateInitial;
    private LocalDateTime registrationDateUpdate;


}