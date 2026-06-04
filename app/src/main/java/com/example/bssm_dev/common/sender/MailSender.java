package com.example.bssm_dev.common.sender;

public interface MailSender {
    void send(String toEmail, String title, String content);
}
