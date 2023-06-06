package com.example.springbootasg.service;

import com.example.springbootasg.dto.PhoneDto;
import com.example.springbootasg.model.Phone;
import com.example.springbootasg.repository.PhoneRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class PhoneServiceTest {

    @Test
    void Test() {
        assertTrue(true);
    }

    @Test
    @DisplayName("Displays All Phones")
    void testShowAllPhones() {
        //given
        PhoneRepository phoneRepository = mock(PhoneRepository.class);
        Phone p1 = new Phone("samsung", "25k");
        Phone p2 = new Phone("apple", "75k");
        List<Phone> phones = Arrays.asList(p1, p2);
        given(phoneRepository.findAll()).willReturn(phones);
        PhoneService phoneService=new PhoneService(phoneRepository);
        //when
        List<PhoneDto> phoneslist=phoneService.showPhones().stream().map(phone -> PhoneDto.builder().name(phone.getName()).price(phone.getPrice()).id(phone.getId()).build()).toList();
        //then
        verify(phoneRepository, times(1)).findAll();
        Assertions.assertEquals(2,phoneslist.size());
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
    @DisplayName("Successfully Updates a Phone")
    void testUpdatePhone() {
        // given
        PhoneRepository phoneRepository = mock(PhoneRepository.class);
        Phone existingPhone = new Phone("samsung", "25k");
        Phone updatedPhone = new Phone("samsung updated", "30k");
        given(phoneRepository.findById(1L)).willReturn(Optional.of(existingPhone));
        given(phoneRepository.save(any(Phone.class))).willReturn(updatedPhone);
        PhoneService phoneService = new PhoneService(phoneRepository);
        // when
        phoneService.updatePhone(1L, "samsung updated", "30k");
        //then
        verify(phoneRepository, times(1)).findById(1L);
        verify(phoneRepository, times(1)).save(any(Phone.class));
    }
    @Test
    @DisplayName("Adds a Phone")
    void testAddPhone() {
        // given
        PhoneRepository phoneRepository = mock(PhoneRepository.class);
        PhoneDto newPhoneDto = new PhoneDto("samsung", "25k",1L);
        PhoneService phoneService = new PhoneService(phoneRepository);
        // when
        phoneService.addPhone(newPhoneDto);
        // then
        verify(phoneRepository, times(1)).save(any(Phone.class));
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
    @DisplayName("Updates Phone Name and Price")
    void testsUpdatePhone() {
        // given
        PhoneRepository phoneRepository = mock(PhoneRepository.class);
        Phone existingPhone = new Phone("samsung", "25k");
        Phone updatedPhone = new Phone("samsung updated", "30k");
        given(phoneRepository.findById(1L)).willReturn(Optional.of(existingPhone));
        given(phoneRepository.save(any(Phone.class))).willReturn(updatedPhone);
        PhoneService phoneService = new PhoneService(phoneRepository);

        // when
        phoneService.updatePhone(1L, "samsung updated", "30k");

        // then
        verify(phoneRepository, times(1)).findById(1L);
        verify(phoneRepository, times(1)).save(any(Phone.class));
    }

    @Test
    @DisplayName("Updates Phone Name Only")
    void testUpdatePhoneWithNameOnly() {
        // given
        PhoneRepository phoneRepository = mock(PhoneRepository.class);
        Phone existingPhone = new Phone("samsung", "25k");
        Phone updatedPhone = new Phone("samsung updated", "25k"); // Same price as existing
        given(phoneRepository.findById(1L)).willReturn(Optional.of(existingPhone));
        given(phoneRepository.save(any(Phone.class))).willReturn(updatedPhone);
        PhoneService phoneService = new PhoneService(phoneRepository);

        // when
        phoneService.updatePhone(1L, "samsung updated", "30k"); // Price parameter not used

        // then
        verify(phoneRepository, times(1)).findById(1L);
        verify(phoneRepository, times(1)).save(any(Phone.class));
    }

    @Test
    @DisplayName("Updates Phone Price Only")
    void testUpdatePhoneWithPriceOnly() {
        // given
        PhoneRepository phoneRepository = mock(PhoneRepository.class);
        Phone existingPhone = new Phone("samsung", "25k");
        Phone updatedPhone = new Phone("samsung", "30k"); // Same name as existing
        given(phoneRepository.findById(1L)).willReturn(Optional.of(existingPhone));
        given(phoneRepository.save(any(Phone.class))).willReturn(updatedPhone);
        PhoneService phoneService = new PhoneService(phoneRepository);

        // when
        phoneService.updatePhone(1L, "samsung updated", "30k"); // Name parameter not used

        // then
        verify(phoneRepository, times(1)).findById(1L);
        verify(phoneRepository, times(1)).save(any(Phone.class));
    }

    @Test
    @DisplayName("Does Not Update Phone Name or Price")
    void testUpdatePhoneWithNoChanges() {
        // given
        PhoneRepository phoneRepository = mock(PhoneRepository.class);
        Phone existingPhone = new Phone("samsung", "25k");
        given(phoneRepository.findById(1L)).willReturn(Optional.of(existingPhone));
        PhoneService phoneService = new PhoneService(phoneRepository);

        // when
        phoneService.updatePhone(1L, "samsung", "25k"); // Same name and price as existing

        // then
        verify(phoneRepository, times(1)).findById(1L);
        verify(phoneRepository, never()).save(any(Phone.class)); // Expect no save call
    }

    @Test
    @DisplayName("Throws Exception When Phone Not Found")
    void testUpdatePhoneWithNonexistentPhone() {
        // given
        PhoneRepository phoneRepository = mock(PhoneRepository.class);
        given(phoneRepository.findById(1L)).willReturn(Optional.empty()); // Phone not found
        PhoneService phoneService = new PhoneService(phoneRepository);

        // when & then
        assertThrows(IllegalStateException.class,
                () -> phoneService.updatePhone(1L, "samsung updated", "30k"));
        verify(phoneRepository, times(1)).findById(1L);
        verify(phoneRepository, never()).save(any(Phone.class)); // Expect no save call
    }

}