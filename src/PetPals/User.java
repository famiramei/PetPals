package PetPals;

public class User {
    
    private String firstname, lastname, username, email, password;
    
    void setFirstname(String uFirstName){
        this.firstname = uFirstName;
    }
    void setLastname(String uLastName){
        this.lastname = uLastName;
    }
    void setUsername(String uUsername){
        this.username = uUsername;   
    }
    void setEmail(String uEmail){
        this.email = uEmail;
    }
    void setPassword(String uPassword){
        this.password = uPassword;
    }
    public String getFirstname() {
        return firstname;
    }
    public String getLastname() {
        return lastname;
    }
    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
}