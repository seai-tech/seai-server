package com.seai.security.service;

import com.seai.domain.model.SeaiUser;
import com.seai.security.model.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SecurityUserService implements UserDetailsService {

    private static final String FIND_USER_BY_ID_QUERY = "SELECT id, email, password FROM users WHERE email= ?";

    private static final String SAVE_USER_QUERY = "INSERT INTO users (id, email, password, first_name, last_name) VALUES (?, ?, ?, ?, ?)";

    private final PasswordEncoder encoder;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        try {
            return jdbcTemplate.queryForObject(FIND_USER_BY_ID_QUERY, (rs, rowNum) -> new SecurityUser(UUID.fromString(rs.getString("id")), rs.getString("email"), rs.getString("password"), Collections.emptyList()), userId);
        } catch (EmptyResultDataAccessException ex) {
            throw new UsernameNotFoundException("User not found : " + userId);
        }
    }

    public void save(SeaiUser seaiUser) {
        jdbcTemplate.update(SAVE_USER_QUERY, UUID.randomUUID().toString(), seaiUser.getEmail(),
                encoder.encode(seaiUser.getPassword()), seaiUser.getFirstName(), seaiUser.getFirstName());
    }
}
