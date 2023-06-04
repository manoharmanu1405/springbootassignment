package com.example.springbootasg.repository;

import com.example.springbootasg.model.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PhoneRepository extends JpaRepository<Phone,Long> {

}
