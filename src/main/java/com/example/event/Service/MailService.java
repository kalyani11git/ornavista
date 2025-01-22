package com.example.event.Service;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Data
@NoArgsConstructor
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;


    //for sending mail to signup person
    public void sendMail(String to, String body, String subject){
        if (to == null || to.isEmpty()) {
            throw new IllegalArgumentException("Email address must not be null or empty");
        }

        System.out.println(to);

        try{
            SimpleMailMessage mail = new SimpleMailMessage();

            mail.setTo(to);
            mail.setSubject(subject);
            mail.setText(body);

            javaMailSender.send(mail);
        }catch (Exception e){
            log.error("Failed to send email to: ",to);
            log.error(String.valueOf(e));
        }

    }

}
