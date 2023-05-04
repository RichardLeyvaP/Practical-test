package com.example.PracticalTest.Repository;
import com.example.PracticalTest.Entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

@Repository
public interface IPersonRepository extends JpaRepository<Person, UUID> {
    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);

    @Query("SELECT p FROM Person p where p.cpf = ?1 and p.personId <> ?2")
    Person existByCpf_Id(String cpf , UUID id);
    @Query("SELECT p FROM Person p where p.email = ?1 and p.personId <> ?2")
    Person existByEmail_Id(String email , UUID id);
    ArrayList<Person> getBySex(String sex);
    ArrayList<Person> getByBirthDate(LocalDate birthDate);
}