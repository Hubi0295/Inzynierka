package com.example.contractorservice.service;

import com.example.auth.entity.Response;
import com.example.auth.entity.User;
import com.example.auth.repository.UserRepository;
import com.example.auth.services.JwtService;
import com.example.contractorservice.entity.Contractor;
import com.example.contractorservice.entity.ContractorDTO;
import com.example.contractorservice.entity.ContractorInfo;
import com.example.contractorservice.repository.ContractorRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContractorService {
    private final ContractorRepository contractorRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public ResponseEntity<?> addContractor(ContractorDTO contractorDTO, HttpServletRequest request){
        String username = null;
        for(Cookie cookie: request.getCookies()){
            if(cookie.getName().equals("Authorization")){
                username=jwtService.getSubject(cookie.getValue());
            }
        }
        Contractor contractor = new Contractor();
        contractor.setName(contractorDTO.getName());
        contractor.setPhone(contractorDTO.getPhone());
        contractor.setEmail(contractorDTO.getEmail());
        if(username!=null){
            User user = userRepository.findUserByUsername(username).orElse(null);
            if(user!=null){
                contractor.setAccount_manager(user);
                contractorRepository.saveAndFlush(contractor);
                return ResponseEntity.ok(new Response("Utworzono kontrahenta"));
            }
            else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Nieznany uzytkownik"));
            }
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Błędny token"));
        }
    }
    public ResponseEntity<?> getContractors(){
        List<ContractorInfo> contractors = contractorRepository.findAll().stream().map(
                e->new ContractorInfo(
                        e.getId(),
                        e.getName(),
                        e.getPhone(),
                        e.getEmail(),
                        e.getAccount_manager().getUsername()
                )
        ).toList();
        if(!contractors.isEmpty()){
            return ResponseEntity.ok(contractors);
        }
        else{
            return ResponseEntity.ok(new Response("Brak kontrahentow"));
        }
    }
    public ResponseEntity<?> editContractor(ContractorDTO contractorDTO, HttpServletRequest request, UUID uuid){
        Contractor contractor = contractorRepository.findContractorByUuid(uuid).orElse(null);
        if(contractor!=null){
            String username = null;
            for(Cookie cookie: request.getCookies()){
                if(cookie.getName().equals("Authorization")){
                    username=jwtService.getSubject(cookie.getValue());
                }
            }
            if(username!=null){
                User user = userRepository.findUserByUsername(username).orElse(null);
                if(user!=null){
                    contractor.setAccount_manager(user);
                    contractor.setName(contractorDTO.getName());
                    contractor.setPhone(contractorDTO.getPhone());
                    contractor.setEmail(contractorDTO.getEmail());
                    contractorRepository.saveAndFlush(contractor);
                    return ResponseEntity.ok(new Response("Edytowano kontrahenta"));
                }
                else{
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Nieznany uzytkownik"));
                }
            }
            else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Błędny token"));
            }
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Podano zły UUID"));
        }
    }

    public ResponseEntity<?> deleteContractor(UUID uuid) {
        Contractor contractor = contractorRepository.findContractorByUuid(uuid).orElse(null);
        if(contractor!=null){
            contractorRepository.delete(contractor);
            return ResponseEntity.ok(new Response("Usunieto kontrahenta"));
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Podano zły UUID"));
        }
    }
}
