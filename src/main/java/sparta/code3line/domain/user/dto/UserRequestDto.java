package sparta.code3line.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequestDto {

    private String username;
    private String password;
    private String email;
    private String nickname;
    private String profileImg;

    //새로운 비밀번호
    @NotBlank(message = " newpassword는 비워둘 수 없습니다.")
    @Size(min = 8, max = 15, message = "password 는 최소 8글자 이상, 최대 15글자 이하여야 합니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[~!@#$%^&*()_+]).{8,15}$"
            , message = "newpassword는 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자를 포함해야 합니다.")
    private String newpassword;

}
