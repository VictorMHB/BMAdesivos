package com.github.victormhb.bmadesivos.security;

import com.github.victormhb.bmadesivos.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return funcionarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Funcionário não encontrado com o email: " + username));
    }
}