package com.example.springbootasg.service;

import com.example.springbootasg.dto.PhoneDto;
import com.example.springbootasg.model.Phone;
import com.example.springbootasg.repository.PhoneRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PhoneService {
    @Autowired
    private final PhoneRepository phoneRepository;

    public Optional<PhoneDto> showPhones(Long id) {
        return phoneRepository.findById(id).stream().map(phones -> PhoneDto.builder()
                .name(phones.getName())
                .price(phones.getPrice())
                .id(phones.getId())
                .build()
        ).findFirst();
    }

    public List<PhoneDto> showPhones() {
        return phoneRepository.findAll().stream().map(phones -> PhoneDto.builder()
                .name(phones.getName())
                .price(phones.getPrice())
                .id(phones.getId())
                .build()
        ).toList();
    }

    @Transactional
    public void addPhone(PhoneDto newPhone) {
        Phone phone = new Phone();
        phone.setName(newPhone.getName());
        phone.setPrice(newPhone.getPrice());
        log.info("Saving Student Data....");
        phoneRepository.save(phone);
    }

    public void deletePhone(Long id) {
        phoneRepository.deleteById(id);
    }

    @Transactional
    public void updatePhone(Long phoneId, PhoneDto phonedto) {
        String name= phonedto.getName();
        String price= phonedto.getPrice();
        Phone phone = phoneRepository.findById(phoneId)
                .orElseThrow(() -> new IllegalStateException("No such phone found"));

        boolean isNameChanged =   name.length() > 0 && !Objects.equals(name, phone.getName());
        boolean isPriceChanged =  price.length() > 0 && !Objects.equals(price, phone.getPrice());

        if (isNameChanged) {
            phone.setName(name);
        }
        if (isPriceChanged) {
            phone.setPrice(price);
        }

        if (isNameChanged || isPriceChanged) {
            log.info("Updating...........");
            phoneRepository.save(phone);
            log.info("Updated successfully.........");
        }
    }


}
