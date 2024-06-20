package sparta.code3line.domain.follow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FollowRequestDto {

    @NotBlank
    @JsonProperty("following_user_id")
    private Long followingUserId;

}
