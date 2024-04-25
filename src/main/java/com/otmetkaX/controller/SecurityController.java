package com.otmetkaX.controller;

import com.otmetkaX.config.JwtTokenProvider;
import com.otmetkaX.exception.CustomException;
import com.otmetkaX.model.Security;
import com.otmetkaX.response.ResponseMessage;
import com.otmetkaX.response.SecurityResponseMessage;
import com.otmetkaX.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class SecurityController {
    private final SecurityService service;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public SecurityController(SecurityService service, JwtTokenProvider jwtTokenProvider) {
        this.service = service;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // вывод всех пользователей
    @GetMapping("security")
    public ResponseEntity<ResponseMessage> getSecurity() throws CustomException {
        List<Security> securityList = service.findAll();
        ResponseMessage response =
                new SecurityResponseMessage("Successes", null, 200, securityList, null, null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // писковик логинов пользователей
    @GetMapping("security/search")
    public ResponseEntity<ResponseMessage> getByTextContainingLogin(@RequestParam String  searchText) throws CustomException {
        List<Security> securityList = service.findByTextContaining(searchText);
        ResponseMessage response =
                new SecurityResponseMessage("Successes", null, 200, securityList, null, null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // поиск информации пользователя по логину
    @GetMapping("security/login")
    public ResponseEntity<ResponseMessage> getByUserInfo(@RequestParam String login) throws CustomException {
        Security security = service.findByLogin(login);
        ResponseMessage response =
                new SecurityResponseMessage("Successes", null, 200, null, security, null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // поиск пользователя по id
    @GetMapping("security/id")
    public ResponseEntity<ResponseMessage> getById(@RequestParam Long id) throws CustomException {
        Security security = service.findById(id);
        ResponseMessage response =
                new SecurityResponseMessage("Successes", null, 200, null, security, null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // создание нового пользователя
    @PostMapping("security")
    public ResponseEntity<ResponseMessage> postSecurity(
            @RequestParam String login, @RequestParam String role, @RequestParam String password, @RequestParam  boolean status )
            throws CustomException {
        String token = jwtTokenProvider.generateToken(login);
        service.save(new Security(role, login, password, status, token));
        ResponseMessage response = new ResponseMessage("Successes", null, 200, token);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // войти за пользователя
    @PostMapping("security/inlet")
    public ResponseEntity<ResponseMessage> postInlet(@RequestParam String login, @RequestParam String password) throws CustomException {
        Security user = service.inlet(login, password);
        ResponseMessage response = new SecurityResponseMessage("Successes", null, 200, null, user, null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    // обновление всех дыннх, кроме id пользователя
    @PutMapping("security")
    public ResponseEntity<ResponseMessage> putSecurity(
            @RequestParam String login, @RequestParam String role, @RequestParam String password, @RequestParam boolean status,  @RequestParam Long id)
            throws CustomException {
        service.updateById(id, password, role, login, status);
        ResponseMessage response = new ResponseMessage("Successes", null, 200, null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // удаление пользователя по id
    @DeleteMapping("security")
    public ResponseEntity<ResponseMessage> deleteSecurity(@RequestParam Long id)
            throws CustomException {
        service.deleteById(id);
        ResponseMessage response = new ResponseMessage("Successes", null, 200,  null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}