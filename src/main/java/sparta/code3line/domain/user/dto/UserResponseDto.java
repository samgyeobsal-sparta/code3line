package sparta.code3line.domain.user.dto;

import lombok.Data;
import sparta.code3line.domain.user.entity.User;

import java.util.List;

@Data
public class UserResponseDto {

    private String username;
    private String roleName;
    private String nickname;
    private String email;
    private List<User> allUsers;


    public UserResponseDto(User user){
        this.allUsers = null;
    }
    public UserResponseDto(List<User> users) {
        this.allUsers = users;
    }

}
