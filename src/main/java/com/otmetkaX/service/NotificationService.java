package com.otmetkaX.service;

import com.otmetkaX.exception.CustomException;
import com.otmetkaX.model.Notification;
import com.otmetkaX.model.Security;

import java.util.List;

public interface NotificationService {
    void save(String text, Long senderId)throws CustomException;
    void update(Long id, String text, Long senderId) throws CustomException;
    List<Notification> findByTextContaining(String searchText);
    List<Notification> findAll();
    Notification findById(Long id) throws CustomException;

    void deleteById(Long id)throws CustomException;
}
