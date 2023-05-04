package com.example.PracticalTest.Service;
import com.example.PracticalTest.Entity.Person;
import com.example.PracticalTest.Repository.IPersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public class PersonService {
    final IPersonRepository personRepository; //creating injection point

    public PersonService(IPersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Page<Person> getPersons(Pageable pageable) {
        return personRepository.findAll(pageable);
    }

    @Transactional
    public Person save(Person person) {
        return personRepository.save(person);
    }

    public boolean existByCpf(String cpf) {
        return personRepository.existsByCpf(cpf);
    }

    public boolean existByCpf_Id(String cpf,UUID id) {
        return personRepository.existByCpf_Id(cpf, id) != null;
    }
    public boolean existByEmail_Id(String email,UUID id) {
        return personRepository.existByEmail_Id(email, id) != null;
    }

    public boolean existByEmail(String email) {
        return personRepository.existsByEmail(email);
    }

    public Optional<Person> findByPersonId(UUID personId) {
        return personRepository.findById(personId);
    }

    public ArrayList<Person> findBySex(String sex) {
        return personRepository.getBySex(sex);
    }

    public ArrayList<Person> findByBirthDate(LocalDate birthDate) {
        return personRepository.getByBirthDate(birthDate);
    }

    @Transactional
    public void delete(Person person) {
        personRepository.delete(person);
    }
}
