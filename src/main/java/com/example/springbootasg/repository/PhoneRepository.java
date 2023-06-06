package com.example.springbootasg.repository;

import com.example.springbootasg.entity.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PhoneRepository extends JpaRepository<Phone,Long> {

}
