package com.uniovi.sdi.grademanager.repositories;

import com.uniovi.sdi.grademanager.entities.Mark;
import org.springframework.data.repository.CrudRepository;

public interface MarksRepository extends CrudRepository<Mark, Long> {
}
