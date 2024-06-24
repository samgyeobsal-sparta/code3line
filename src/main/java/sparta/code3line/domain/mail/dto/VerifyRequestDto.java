package sparta.code3line.domain.mail.dto;

import lombok.Data;

@Data
public class VerifyRequestDto {

    private String email;
    private String code;

}
