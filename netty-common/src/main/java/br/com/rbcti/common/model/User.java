package br.com.rbcti.common.model;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 *
 * @author Renato Cunha
 * @version 1.0
 */
public class User implements Serializable {

    private static final long serialVersionUID = 9067932010027428391L;

    private Long userId;
    private String name;
    private String password;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        return Objects.equals(userId, other.userId);
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", name=" + name + ", password=" + password + "]";
    }

}
