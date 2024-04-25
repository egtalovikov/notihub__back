package com.otmetkaX.service.impl;

import com.otmetkaX.exception.CustomException;
import com.otmetkaX.model.Security;
import com.otmetkaX.repository.SecurityRepository;
import com.otmetkaX.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SecurityServiceImpl implements SecurityService {
    private final SecurityRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityServiceImpl(SecurityRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<Security> findAll() throws CustomException {
        List<Security> securityList = repository.findAll();
        if (securityList.isEmpty()) {
            throw new CustomException("USER_NOT_FOUND", 404);
        }
        return securityList;
    }

    @Override
    public Security findByLogin(String login) throws CustomException {
        Optional<Security> security = repository.findByLogin(login);
        if (!security.isPresent()) {
            throw new CustomException("USER_NOT_FOUND", 404);
        }
        return security.get();
    }

    public Security inlet(String login, String password) throws CustomException {
        Optional<Security> security = repository.findByLogin(login);
        if (!security.isPresent()) {
            throw new CustomException("USER_NOT_FOUND", 404);
        }
        if (!comparePassword(password, security.get().getPassword())) {
            throw new CustomException("PASSWORD_NOT_FOUND", 404);
        }
        return security.get();
    }

    @Override
    public Security findByToken(String token) throws CustomException {
        Optional<Security> security = repository.findByToken(token);
        if (!security.isPresent()) {
            throw new CustomException("TOKEN_NOT_FOUND", 404);
        }
        return security.get();
    }



    @Override
    public void save(Security security) throws CustomException {
        validateLogin(security.getLogin());
        validatePassword(security.getPassword());
        isValidRole(security.getRole());
        repository.save(security);
    }

    @Override
    public void updateById(Long id, String password, String role, String login, Boolean status) throws CustomException {
        Security user = findById(id);

        validateLogin(login);
        validatePassword(password);
        isValidRole(role);

        user.setLogin(login);
        user.setPassword(password);
        user.setRole(role);
        user.setStatus(status);

        repository.save(user);
    }

    @Override
    public void isValidRole(String role) throws CustomException {
        try {
            RoleEnum.valueOf(role);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new CustomException("ROLE_NOT_VALID", 404);
        }
    }

    @Override
    public List<Security> findByTextContaining(String searchText) {
        if (searchText.isEmpty()) return null;
        return repository.findByTextContaining(searchText);
    }

    @Override
    public Security findById(Long id) throws CustomException {
        Optional<Security> security = repository.findById(id);
        if (!security.isPresent()) {
            throw new CustomException("USER_NOT_FOUND", 404);
        }
        return security.get();
    }

    @Override
    public void deleteById(Long id) throws CustomException {
        findById(id);
        repository.deleteById(id);
    }

    private void validatePassword(String password) throws CustomException {
        // Проверка на длину пароля (минимальная длина, например, 8 символов)
        if (password.length() < 8) {
            throw new CustomException("PASSWORD_TOO_SHORT", 404);
        }
        // Проверка на наличие как минимум одной цифры
        if (!password.matches(".*\\d.*")) {
            throw new CustomException("PASSWORD_NO_DIGIT", 404);
        }
        // Проверка на наличие как минимум одной строчной буквы
        if (!password.matches(".*[a-z].*")) {
            throw new CustomException("PASSWORD_NO_LOWER_CASE", 404);
        }
        // Проверка на наличие как минимум одной прописной буквы
        if (!password.matches(".*[A-Z].*")) {
            throw new CustomException("PASSWORD_NO_UPPER_CASE", 404);
        }
    }

    private void validateLogin(String login) throws CustomException {
        // Проверка на длину логина (например, минимальная длина 5 символов)
        if (login.length() < 5) {
            throw new CustomException("LOGIN_TOO_SHORT", 404);
        }
        // Проверка что логин не занят
        Optional<Security> optionalUser = repository.findByLogin(login);
        if (optionalUser.isPresent()) {
            throw new CustomException("LOGIN_ALREADY_EXISTS", 404);
        }
        // Проверка на наличие только буквенно-цифровых символов
        if (!login.matches("[a-zA-Z0-9]+")) {
            throw new CustomException("LOGIN_INVALID_CHARACTERS", 404);
        }
    }

    private boolean comparePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
