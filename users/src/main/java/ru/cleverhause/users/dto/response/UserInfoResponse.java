package ru.cleverhause.users.dto.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfoResponse implements Serializable {
    private Long userId;
    private String username;
    private String email;

    @Builder
    @JsonCreator
    public UserInfoResponse(@JsonProperty("userId") Long userId,
                            @JsonProperty("username") String username,
                            @JsonProperty("email") String email) {
        this.userId = userId;
        this.username = username;
        this.email = email;
    }
}
