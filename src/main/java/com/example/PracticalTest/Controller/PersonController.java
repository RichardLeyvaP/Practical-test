package com.example.PracticalTest.Controller;
import com.example.PracticalTest.DataValid.DataValidations;
import com.example.PracticalTest.Entity.Person;
import com.example.PracticalTest.Service.PersonService;

import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@RestController
@CrossOrigin(origins = "*",maxAge = 3600)
@RequestMapping(path ="api/v1/person")
public class PersonController {
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping        /*Insert*/
    public ResponseEntity savePerson(@Valid @RequestBody  DataValidations a_dataValidation)
    {
        /*Verifications and validations*/
        if (personService.existByCpf(a_dataValidation.getCpf()))
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: CPF already exists ");
        }
        if (personService.existByEmail(a_dataValidation.getEmail()))
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Email already exists ");
        }

        var person = new Person();
        BeanUtils.copyProperties(a_dataValidation,person);
        LocalDate birthDate;
        try {
            birthDate = ConvertDate(a_dataValidation);
            LocalDate localDate = LocalDate.now();//HERE I GOT THE CURRENT DATE
            if(birthDate.isAfter(localDate)){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Inserted date is greater than current date");
            }
        }catch (DateTimeParseException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: Date Invalid(Format:yyyy-MM-dd)");
        }


        person.setRegistrationDateInitial(LocalDateTime.now(ZoneId.of("UTC")));
        person.setRegistrationDateUpdate(LocalDateTime.now(ZoneId.of("UTC")));
        person.setBirthDate(birthDate);
        return ResponseEntity.status(HttpStatus.CREATED).body(personService.save(person));
    }

    @GetMapping         /*Show all*/
    public ResponseEntity<Page<Person>> getAllPerson(@PageableDefault(page =0,size = 10,sort = "personId",direction = Sort.Direction.ASC)Pageable pageable)
    {
        return ResponseEntity.status(HttpStatus.OK).body(personService.getPersons(pageable));
    }
    @GetMapping("/id/{personId}")   /*Search by gender*/
    public ResponseEntity<Object> getPersonId(@PathVariable(value = "personId") UUID personId)
    {
        Optional<Person> a_personOptional = personService.findByPersonId(personId);
        if(a_personOptional.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There are NO people with that sex");
        }
        return ResponseEntity.status(HttpStatus.OK).body(a_personOptional);

    }

    @GetMapping("/sex/{sex}")   /*Search by gender*/
    public ResponseEntity<Object> getPersonSex(@PathVariable(value = "sex") String sex)
    {
        ArrayList<Person> a_personOptional = personService.findBySex(sex);
        if(a_personOptional.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There are NO people with that sex");
        }
        return ResponseEntity.status(HttpStatus.OK).body(a_personOptional);

    }

    @GetMapping("/birthDate/{birthDate}")  /*Search by Birth Date*/
    public ResponseEntity<Object> getPersonBirthDate(@PathVariable(value = "birthDate") LocalDate birthDate)
    {
        ArrayList<Person> a_personOptional = personService.findByBirthDate(birthDate);
        if(a_personOptional.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There are NO people with that date of birth");
        }
        return ResponseEntity.status(HttpStatus.OK).body(a_personOptional);

    }

    @PutMapping("/{personId}")       /*Update*/
    public ResponseEntity<Object> updatePerson(@PathVariable(value = "personId") UUID personId, @RequestBody @Valid DataValidations a_dataValidation)
    {
        Optional<Person> a_personOptional = personService.findByPersonId(personId);
        if(a_personOptional.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person not found");
        }
        var person = new Person();
        BeanUtils.copyProperties(a_dataValidation,person);
        /*Verifications and validations*/
        if (personService.existByCpf_Id(a_dataValidation.getCpf(),personId))
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Cpf already exits ");
        }

        if (personService.existByEmail_Id(a_dataValidation.getEmail(),personId))
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exits ");

        }

        LocalDate birthDate;
        try {
            birthDate = ConvertDate(a_dataValidation);
            LocalDate localDate = LocalDate.now();//HERE I GOT THE CURRENT DATE
            if(birthDate.isAfter(localDate)){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Inserted date is greater than current date");
            }
        }catch (DateTimeParseException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: Date Invalid(Format:yyyy-MM-dd)");
        }

        person.setBirthDate(birthDate);
        person.setPersonId(personId);
        person.setRegistrationDateInitial(a_personOptional.get().getRegistrationDateInitial());
        person.setRegistrationDateUpdate(LocalDateTime.now(ZoneId.of("UTC")));
        return ResponseEntity.status(HttpStatus.OK).body(personService.save(person));
    }

    @DeleteMapping("/{personId}")    /*Delete*/
    public ResponseEntity<Object> getPersonBirthDate(@PathVariable(value = "personId") UUID personId)
    {
        Optional<Person> a_personOptional = personService.findByPersonId(personId);
        if(!a_personOptional.isPresent())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("People NOT found");
        }
        personService.delete(a_personOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("People deleted successfully");

    }

    /*Interspting error*/

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String,String> ValidationExeption(MethodArgumentNotValidException ex)
    {
        Map<String,String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error)->{
            String fieldName = ((FieldError) error).getField();
            String errorMenssage = error.getDefaultMessage();
            errors.put(fieldName,errorMenssage);
        });
        return errors;
    }

    public LocalDate ConvertDate(DataValidations a_dataValidation)
    {
        String date_envio = a_dataValidation.getBirthDate();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthDate = LocalDate.parse(date_envio, formato);
        return  birthDate;
    }

}
