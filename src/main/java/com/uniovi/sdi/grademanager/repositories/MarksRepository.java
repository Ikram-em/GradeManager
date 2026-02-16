package com.uniovi.sdi.grademanager.repositories;

import com.uniovi.sdi.grademanager.entities.Mark;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MarksRepository extends CrudRepository<Mark, Long> {
    List<Mark> findAllByUserDni(String dni);
}
