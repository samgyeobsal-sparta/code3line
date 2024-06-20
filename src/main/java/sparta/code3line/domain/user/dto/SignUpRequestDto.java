package sparta.code3line.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;


@Getter
public class SignUpRequestDto {

    private Long id;

    //이름
    @NotBlank(message = " username 비워둘 수 없습니다.")
    @Size(min = 4, max = 10, message = "username 는 최소 4글자 이상, 최대 10글자 이하여야 합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "username은 대소문자 포함 영문과 숫자만 허용됩니다.")
    private String username = "username";

    // 비밀번호
    @NotBlank(message = " password는 비워둘 수 없습니다.")
    @Size(min = 8, max = 15, message = "username 는 최소 8글자 이상, 최대 15글자 이하여야 합니다.")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[~!@#$%^&*()_+])[A-Za-z\\d~!@#$%^&*()_+]{10,}$"
            , message = "password는 대소문자 포함 영문, 숫자, 특수문자를 최소 1글자씩 포함해야 합니다.")
    private String password;

    private String nickname;
}
