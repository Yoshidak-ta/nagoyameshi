package com.example.nagoyameshi.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.nagoyameshi.entity.User;

public class CustomUserDetails implements UserDetails{
	private User user;
	
	public CustomUserDetails(User user) {
		this.user = user;
	}
	
	@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 権限情報を返す処理を実装する
        // 例えば、user.getRole()から権限情報を取得して返すことができます
        // 必要に応じて専用のRoleエンティティを作成することも可能です
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

}
