package com.otmetkaX.service.impl;


import com.otmetkaX.exception.CustomException;
import com.otmetkaX.model.Notification;
import com.otmetkaX.model.Security;
import com.otmetkaX.repository.NotificationRepository;
import com.otmetkaX.service.NotificationService;
import com.otmetkaX.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final SecurityService securityService;
    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository, SecurityService securityService) {
        this.notificationRepository = notificationRepository;
        this.securityService = securityService;

    }

    @Override
    public void save(String text, Long senderId)throws CustomException {
        Security user = securityService.findById(senderId);
        notificationRepository.save(new Notification(text, user));
    }

    @Override
    public List<Notification> findByTextContaining(String searchText) {
        if(searchText.isEmpty()) return null;
        return notificationRepository.findByTextContaining(searchText);
    }

    @Override
    public List<Notification> findAll() {
        return notificationRepository.findAll();
    }

    @Override
    public Notification findById(Long id) throws CustomException {
        Optional<Notification> notificationOptional = notificationRepository.findById(id);
        if (!notificationOptional.isPresent()) {
            throw new CustomException("NOTIFICATION_NOT_FOUND", 404);
        }
        return notificationOptional.get();
    }

    @Override
    public void update(Long id, String text, Long senderId) throws CustomException {
        Security user = securityService.findById(senderId);
        Notification notification = findById(id);
        notification.setText(text);
        notification.setSender(user);
        notificationRepository.save(notification);
    }

    @Override
    public void deleteById(Long id)throws CustomException {
        findById(id);
        notificationRepository.deleteById(id);
    }

}
