package com.uniovi.sdi.grademanager.repositories;

import com.uniovi.sdi.grademanager.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UsersRepository extends CrudRepository<User, Long> {
    User findByDni(String dni);
}
