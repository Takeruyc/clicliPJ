package com.example.demo.model;

import java.util.Date;

public class User {
    private String id;
    private String nickname;
    private String email;
    private String password;
    private String role;
    private Date createdAt;
    private String reserved1;
    private String reserved2;
    private String reserved3;

    public String getId() { 
        return id; 
    }
    
    public String getNickname() { 
        return nickname; 
    }
    
    public String getEmail() { 
        return email; 
    }
    
    public String getPassword() { 
        return password; 
    }
    
    public String getRole() { 
        return role; 
    }
    
    public Date getCreatedAt() { 
        return createdAt; 
    }
    
    public String getReserved1() { 
        return reserved1; 
    }
    
    public String getReserved2() { 
        return reserved2; 
    }
    
    public String getReserved3() { 
        return reserved3; 
    }

    public void setId(String id) { 
        this.id = id; 
    }
    
    public void setNickname(String nickname) { 
        this.nickname = nickname; 
    }
    
    public void setEmail(String email) { 
        this.email = email; 
    }
    
    public void setPassword(String password) { 
        this.password = password; 
    }
    
    public void setRole(String role) { 
        this.role = role; 
    }
    
    public void setCreatedAt(Date createdAt) { 
        this.createdAt = createdAt; 
    }
    
    public void setReserved1(String reserved1) { 
        this.reserved1 = reserved1; 
    }
    
    public void setReserved2(String reserved2) { 
        this.reserved2 = reserved2; 
    }
    
    public void setReserved3(String reserved3) { 
        this.reserved3 = reserved3; 
    }
}