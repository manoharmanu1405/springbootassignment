package com.example.springbootasg.controller;

import com.example.springbootasg.dto.PhoneDto;
import com.example.springbootasg.service.PhoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/phones")
@RequiredArgsConstructor
public class HomeController {
    @Autowired
    private final PhoneService phoneService;
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<PhoneDto> showPhones(@PathVariable Long id) {
        return phoneService.showPhones(id);
    }
    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<PhoneDto> showPhones() {
        return phoneService.showPhones();
    }
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public String addPhone(@RequestBody PhoneDto newPhone) {
        phoneService.addPhone(newPhone);
        return "Phone Added";
    }
    @DeleteMapping("/{id}")
    public String deletePhone(@PathVariable Long id){
        phoneService.deletePhone(id);
        return "Phone Deleted";
    }
    @PutMapping("/{id}")
    public String updateStudent(@PathVariable Long id,@RequestParam(required = false) String name,@RequestParam(required = false) String price
    ){

        phoneService.updatePhone(id,name,price);
        return "Phone Data Updated";
    }

}
