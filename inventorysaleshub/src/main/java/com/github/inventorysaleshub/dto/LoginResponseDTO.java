package com.github.inventorysaleshub.dto;

public class LoginResponseDTO {

    private String token;
    private String tokenType = "Bearer";
    private Long userId;
    private String userName;
    private String userEmail;
    private String roleName;


    // --- Getters and Setters ---
    public String getToken() { 
        return token; 
    }
    public void setToken(String token) { 
        this.token = token; 
    }

    public String getTokenType() { 
        return tokenType; 
    }
    public void setTokenType(String tokenType) { 
        this.tokenType = tokenType; 
    }

    public Long getUserId() { 
        return userId; 
    }
    public void setUserId(Long userId) { 
        this.userId = userId; 
    }

    public String getUserName() { 
        return userName; 
    }
    public void setUserName(String userName) { 
        this.userName = userName; 
    }

    public String getUserEmail() { 
        return userEmail; 
    }
    public void setUserEmail(String userEmail) { 
        this.userEmail = userEmail; 
    }

    public String getRoleName() { 
        return roleName; 
    }
    public void setRoleName(String roleName) { 
        this.roleName = roleName; 
    }
}


