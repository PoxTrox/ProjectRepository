package org.example.projectRepository.author.repository;

import jakarta.persistence.OneToMany;
import org.example.projectRepository.author.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthorRepository extends JpaRepository <Author, Long> {

    Optional<Author>findByFirstNameAndLastName(String firstName, String lastName);

    Optional<Author>findById(UUID id);
}
