package jin.security5;

import javax.persistence.*;

@Entity
@Table(name = "user_auth")
public class UserAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auth_no")
    private Integer authNo;

    @Column(name = "user_id", nullable = false, length = 100)
    private String userId;

    @Column(name = "auth", nullable = false, length = 100)
    private String auth;

    // Getters and Setters
}