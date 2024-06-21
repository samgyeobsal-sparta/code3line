package sparta.code3line.domain.user.dto;

import lombok.Data;

@Data
public class UserRequestDto {

    private final String username;
    private final String password;
    private final String nickname;
    private final String newpassword;

}
