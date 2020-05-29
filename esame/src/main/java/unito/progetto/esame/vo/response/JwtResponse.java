package unito.progetto.esame.vo.response;

import lombok.Data;


@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String account;
    private String name;
    private String role;
    private Long id;
    private String password;
    private String phone;

    public JwtResponse(String token, String account, String name, String role, Long id, String password, String phone) {
        this.account = account;
        this.name = name;
        this.token = token;
        this.role = role;
        this.id= id;
        this.password= password;
        this.phone= phone;
    }

}
