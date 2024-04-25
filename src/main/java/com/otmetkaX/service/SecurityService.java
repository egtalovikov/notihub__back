package com.otmetkaX.service;


import com.otmetkaX.exception.CustomException;
import com.otmetkaX.model.Notification;
import com.otmetkaX.model.Security;

import java.util.List;

public interface SecurityService {
    public enum RoleEnum {
        ADMIN,
        USER,
        EMPLOYEE
    }

    void save(Security security) throws CustomException;

    List<Security> findAll() throws CustomException;

    void deleteById(Long id) throws CustomException;

    Security findByLogin(String login) throws CustomException;
    Security findById(Long id) throws CustomException;
    Security findByToken(String token) throws CustomException;
    void isValidRole(String role) throws CustomException;
    void updateById(Long id, String password, String role, String login, Boolean status) throws CustomException;
    List<Security> findByTextContaining(String searchText);
    Security inlet(String login, String password) throws CustomException;

}
