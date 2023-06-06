package com.example.springbootasg.service;

import com.example.springbootasg.dto.PhoneDto;
import com.example.springbootasg.entity.Phone;
import com.example.springbootasg.repository.PhoneRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class PhoneServiceTest {
    @Test
    @DisplayName("Displays All Phones")
    void testShowAllPhones() {
        //given
        PhoneRepository phoneRepository = mock(PhoneRepository.class);
        Phone p1 = new Phone("samsung", "25k");
        Phone p2 = new Phone("apple", "75k");
        List<Phone> phones = Arrays.asList(p1, p2);
        given(phoneRepository.findAll()).willReturn(phones);
        PhoneService phoneService = new PhoneService(phoneRepository);
        //when
        List<PhoneDto> phoneslist = phoneService.showPhones().stream().map(phone -> PhoneDto.builder().name(phone.getName()).price(phone.getPrice()).id(phone.getId()).build()).toList();
        //then
        verify(phoneRepository, times(1)).findAll();
        Assertions.assertEquals(2, phoneslist.size());
    }

    @Test
    @DisplayName("Displays Single Phone")
    void testShowSinglePhones() {
        // given
        PhoneRepository phoneRepository = mock(PhoneRepository.class);
        Phone p1 = new Phone("samsung", "25k");
        given(phoneRepository.findById(1L)).willReturn(Optional.of(p1));
        PhoneService phoneService = new PhoneService(phoneRepository);

        // when
        List<PhoneDto> phonesList = phoneService.showPhones(1L)
                .stream()
                .map(phone -> PhoneDto.builder()
                        .name(phone.getName())
                        .price(phone.getPrice())
                        .id(phone.getId())
                        .build())
                .toList();
        // then
        verify(phoneRepository, times(1)).findById(1L);
        Assertions.assertEquals(1, phonesList.size());
    }

    @Test
    @DisplayName("Deletes a Phone")
    void testDeletePhone() {
        // given
        PhoneRepository phoneRepository = mock(PhoneRepository.class);
        PhoneService phoneService = new PhoneService(phoneRepository);
        // when
        phoneService.deletePhone(1L);
        // then
        verify(phoneRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Adds a Phone")
    void testAddPhone() {
        // given
        PhoneRepository phoneRepository = mock(PhoneRepository.class);
        PhoneDto newPhoneDto = new PhoneDto("samsung", "25k", 1L);
        PhoneService phoneService = new PhoneService(phoneRepository);
        // when
        phoneService.addPhone(newPhoneDto);
        // then
        verify(phoneRepository, times(1)).save(any(Phone.class));
    }

    @ParameterizedTest
    @DisplayName("Update Phone - Edge Case")
    @CsvSource({
            "'samsung', '25k'"
    })
    void testUpdatePhones1(String name, String price) {
        // Given
        PhoneRepository phoneRepository = mock(PhoneRepository.class);
        Phone existingPhone = new Phone("samsung", "25k");
        given(phoneRepository.findById(1L)).willReturn(Optional.of(existingPhone));
        PhoneService phoneService = new PhoneService(phoneRepository);
        PhoneDto phone = new PhoneDto(name, price, 1L);
        // When
        phoneService.updatePhone(1L, phone);
        // Then
        verify(phoneRepository, times(1)).findById(1L);
        verify(phoneRepository, never()).save(any(Phone.class));
    }

    @ParameterizedTest
    @DisplayName("Update Phone Test Cases-2")
    @CsvSource({
            "null, null",                   // No changes
            "null, '30k'",                  // Only price changed
            "'samsung updated', null",      // Only name changed
            "'samsung updated', '30k'",     // Both name and price changed
            "'samsung', null",              // Name same as existing, only price changed
            "null, '25k'",                  // Price same as existing, only name changed
            "'samsung updated', '25k'",     // Only name changed, price same as existing
            "'samsung', '30k'",              // Only price changed, name same as existing
            "'', '30k'",                     // Empty name, only price changed
            "'samsung updated', ''",      // Only name changed, empty price
            "'', 'null'",
            "null, ''",
            "'', ''",
            "'samsung', ''",                 // Name same as existing, empty price
            "'', '25k'",                     // Empty name, price same as existing
    })
    void testUpdatePhones2(String name, String price) {
        // given
        PhoneRepository phoneRepository = mock(PhoneRepository.class);
        Phone existingPhone = new Phone("samsung", "25k");
        Phone updatedPhone = new Phone();
        updatedPhone.setName(name);
        updatedPhone.setPrice(price);
        given(phoneRepository.findById(1L)).willReturn(Optional.of(existingPhone));
        given(phoneRepository.save(any(Phone.class))).willReturn(updatedPhone);
        System.out.println(updatedPhone.getName());
        System.out.println(existingPhone.getName());
        System.out.println(updatedPhone);
        System.out.println(existingPhone);
        PhoneService phoneService = new PhoneService(phoneRepository);
        PhoneDto phone = new PhoneDto(name, price, 1L);
        // when
        phoneService.updatePhone(1L, phone);
        System.out.println();
        // then
        verify(phoneRepository, times(1)).findById(1L);
        if (!existingPhone.equals(updatedPhone)) {
            verify(phoneRepository, times(1)).save(any(Phone.class));
        } else {
            verify(phoneRepository, never()).save(any(Phone.class));
        }
    }
}