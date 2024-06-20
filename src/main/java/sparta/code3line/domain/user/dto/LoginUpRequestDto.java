package sparta.code3line.domain.user.dto;

import lombok.Data;

@Data
public class LoginUpRequestDto {

    //아이디

    private String username;

    // 비밀번호

    private String password;

}
