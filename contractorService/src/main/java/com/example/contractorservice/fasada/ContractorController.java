package com.example.contractorservice.fasada;

import com.example.contractorservice.entity.ContractorDTO;
import com.example.contractorservice.service.ContractorService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/contractors")
@RequiredArgsConstructor
public class ContractorController {
    private final ContractorService contractorService;

    @RequestMapping(path = "/", method = RequestMethod.POST)
    public ResponseEntity<?> addContractor(@Valid @RequestBody ContractorDTO contractorDTO, HttpServletRequest request){
        return contractorService.addContractor(contractorDTO, request);
    }
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<?> getContractors(){
        return contractorService.getContractors();
    }
    @RequestMapping(value="/{uuid}", method = RequestMethod.PUT)
    public ResponseEntity<?> editContractor(@Valid @RequestBody ContractorDTO contractorDTO, HttpServletRequest request, @PathVariable("uuid") UUID uuid){
        return contractorService.editContractor(contractorDTO,request,uuid);
    }
    @RequestMapping(value="/{uuid}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteContractor(@PathVariable("uuid") UUID uuid){
        return contractorService.deleteContractor(uuid);
    }
}
