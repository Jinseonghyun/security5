package jin.security5;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_NO")
    private Integer userNo;

    @Column(name = "USER_ID", nullable = false, length = 100)
    private String userId;

    @Column(name = "USER_PW", nullable = false, length = 200)
    private String userPw;

    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    @Column(name = "EMAIL", length = 200)
    private String email;

    @Column(name = "ENABLED")
    private Integer enabled = 1;

}