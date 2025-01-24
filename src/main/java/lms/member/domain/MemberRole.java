package lms.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MemberRole {

    USER("ROLE_USER"),         // 일반 사용자
    ADMIN("ROLE_ADMIN"),       // 관리자
    STUDENT("ROLE_STUDENT"),       // 학
    PROFESSOR("ROLE_PROFESSOR"); // 교수

    private String value;

}
