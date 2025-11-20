package com.github.victormhb.bmadesivos.controller;

import com.github.victormhb.bmadesivos.dto.LoginRequest;
import com.github.victormhb.bmadesivos.dto.LoginResponse;
import com.github.victormhb.bmadesivos.dto.RegisterRequest;
import com.github.victormhb.bmadesivos.entity.Funcionario;
import com.github.victormhb.bmadesivos.repository.FuncionarioRepository;
import com.github.victormhb.bmadesivos.security.AutenticacaoService;
import com.github.victormhb.bmadesivos.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final FuncionarioRepository funcionarioRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AutenticacaoService autenticacaoService;

    public AuthController(AuthenticationManager authenticationManager, FuncionarioRepository funcionarioRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, AutenticacaoService autenticacaoService) {
        this.authenticationManager = authenticationManager;
        this.funcionarioRepository = funcionarioRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.autenticacaoService = autenticacaoService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getSenha()
                    )
            );
        } catch (BadCredentialsException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas.");
        }

        Funcionario funcionario = funcionarioRepository.findByEmail(request.getEmail()).orElseThrow();

        String token = jwtUtil.generateToken(request.getEmail());

        return ResponseEntity.ok(
                new LoginResponse(token, funcionario.getNome(),  funcionario.getCargo().name())
        );
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (funcionarioRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email já está em uso.");
        }

        Funcionario novoFunc = new Funcionario(
                request.getNome(),
                request.getEmail(),
                passwordEncoder.encode(request.getSenha()),
                request.getCargo()
        );

        funcionarioRepository.save(novoFunc);

        return ResponseEntity.ok("Funcionário cadastrado com sucesso!");
    }
}
