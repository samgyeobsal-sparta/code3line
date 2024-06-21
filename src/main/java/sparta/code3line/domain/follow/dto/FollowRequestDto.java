package sparta.code3line.domain.follow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
public class FollowRequestDto {

    //@NotBlank
    @NotNull
    @JsonProperty("following_user_id")
    private Long followingUserId;

}
