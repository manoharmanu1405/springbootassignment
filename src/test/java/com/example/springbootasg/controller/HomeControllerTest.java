package com.example.springbootasg.controller;

import com.example.springbootasg.dto.PhoneDto;
import com.example.springbootasg.service.PhoneService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class HomeControllerTest {

    @Mock
    private PhoneService phoneService;

    @InjectMocks
    private HomeController homeController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(homeController).build();
    }

    @Test
    @DisplayName("Test GET /api/v1/phones/{id}")
    void testShowPhonesById() throws Exception {
        // Prepare test data
        Long id = 1L;
        PhoneDto phoneDto = new PhoneDto("Samsung", "25k",1L);

        // Mock the phoneService to return the expected phoneDto
        when(phoneService.showPhones(id)).thenReturn(Optional.of(phoneDto));

        // Perform the GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/phones/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Samsung"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value("25k"));

        // Verify that the showPhones() method was called with the correct id
        verify(phoneService, times(1)).showPhones(id);
    }
    @Test
    @DisplayName("Verify GET Method for retrieving all phones")
    void shouldRetrieveAllPhones() throws Exception {
        // Create some dummy phone data for testing
        List<PhoneDto> expectedPhones = Arrays.asList(
                new PhoneDto("samsung", "25k",1L),
                new PhoneDto("iphone", "50k",2L)
        );

        // Mock the phoneService to return the expected phones
        when(phoneService.showPhones()).thenReturn(expectedPhones);

        // Perform the GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/phones/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[{ \"name\": \"samsung\", \"price\": \"25k\" }, { \"name\": \"iphone\", \"price\": \"50k\" }]"));

        // Verify that the showPhones() method was called
        verify(phoneService, times(1)).showPhones();
    }


    @Test
    @DisplayName("Verify POST Method for adding a phone")
    void testAddPhone() throws Exception {
        // Create the expected phone DTO
        PhoneDto expectedPhone = new PhoneDto("samsung", "25k",1L);

        // Perform the POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/phones/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(expectedPhone)))
                .andExpect(status().isCreated());

        // Create an ArgumentCaptor for PhoneDto
        ArgumentCaptor<PhoneDto> phoneDtoCaptor = ArgumentCaptor.forClass(PhoneDto.class);

        // Verify that the addPhone method was called with the expected argument
        verify(phoneService, times(1)).addPhone(phoneDtoCaptor.capture());
        PhoneDto capturedPhoneDto = phoneDtoCaptor.getValue();

        // Compare the field values of the captured PhoneDto with the expected values
        assertEquals(expectedPhone.getName(), capturedPhoneDto.getName());
        assertEquals(expectedPhone.getPrice(), capturedPhoneDto.getPrice());
    }

    private String asJsonString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Test DELETE /api/v1/phones/{id}")
    void testDeletePhone() throws Exception {
        // Prepare test data
        Long id = 1L;

        // Perform the DELETE request
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/phones/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Verify that the deletePhone() method was called with the correct id
        verify(phoneService, times(1)).deletePhone(id);
    }

    @Test
    @DisplayName("Test PUT /api/v1/phones/{id}")
    void testUpdatePhone() throws Exception {
        // Prepare test data
        Long id = 1L;
        String name = "Updated Samsung";
        String price = "30k";

        PhoneDto expectedPhone = new PhoneDto(price, name, id);

        // Create an ArgumentCaptor to capture the argument passed to the updatePhone() method
        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<PhoneDto> phoneCaptor = ArgumentCaptor.forClass(PhoneDto.class);

        // Perform the PUT request
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/phones/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(expectedPhone)))
                .andExpect(status().isOk());

        // Verify that the updatePhone() method was called with the correct parameters
        verify(phoneService, times(1)).updatePhone(idCaptor.capture(), phoneCaptor.capture());

        // Retrieve the captured arguments
        Long capturedId = idCaptor.getValue();
        PhoneDto capturedPhone = phoneCaptor.getValue();

        // Assert the captured values
        assertEquals(id, capturedId);
        assertEquals(expectedPhone.getName(), capturedPhone.getName());
        assertEquals(expectedPhone.getPrice(), capturedPhone.getPrice());
        assertEquals(expectedPhone.getId(), capturedPhone.getId());
    }


}