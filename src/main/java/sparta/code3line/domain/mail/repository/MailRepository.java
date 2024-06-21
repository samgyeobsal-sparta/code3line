package sparta.code3line.domain.mail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sparta.code3line.domain.mail.entity.Mail;

import java.util.Optional;

public interface MailRepository extends JpaRepository<Mail, Long> {
        Optional<Mail> findByEmail(String email);
    }

