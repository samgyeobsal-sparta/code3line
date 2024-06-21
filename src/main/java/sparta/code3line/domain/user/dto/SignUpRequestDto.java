package sparta.code3line.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class SignUpRequestDto {

    // 아이디
    @NotBlank(message = " username은 비워둘 수 없습니다.")
    @Size(min = 4, max = 10, message = "username 는 최소 4글자 이상, 최대 10글자 이하여야 합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "username은 대소문자 포함 영문과 숫자만 허용됩니다.")
    private String username;

    // 비밀번호
    @NotBlank(message = " password는 비워둘 수 없습니다.")
    @Size(min = 8, max = 15, message = "password 는 최소 8글자 이상, 최대 15글자 이하여야 합니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[~!@#$%^&*()_+]).{8,15}$"
            , message = "password는 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자를 포함해야 합니다.")
    private String password;

    @NotBlank(message = " email은 비워둘 수 없습니다.")
    @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$",
                    message = "잘못된 이메일 형식입니다.")
    private String email;

    @NotBlank(message = " nickname은 비워둘 수 없습니다.")
    private String nickname;

    private String admin;
}
