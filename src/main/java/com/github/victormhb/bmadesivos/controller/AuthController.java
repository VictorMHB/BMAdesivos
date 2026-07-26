package com.github.victormhb.bmadesivos.controller;

import com.github.victormhb.bmadesivos.dto.auth.login.LoginRequest;
import com.github.victormhb.bmadesivos.dto.auth.login.LoginResponse;
import com.github.victormhb.bmadesivos.dto.auth.RegisterRequest;
import com.github.victormhb.bmadesivos.dto.auth.senha.RecuperarSenhaDTO;
import com.github.victormhb.bmadesivos.dto.auth.senha.RedefinirSenhaDTO;
import com.github.victormhb.bmadesivos.entity.Funcionario;
import com.github.victormhb.bmadesivos.repository.FuncionarioRepository;
import com.github.victormhb.bmadesivos.repository.RecuperacaoSenhaRepository;
import com.github.victormhb.bmadesivos.security.AutenticacaoService;
import com.github.victormhb.bmadesivos.security.JwtUtil;
import com.github.victormhb.bmadesivos.service.RecuperacaoSenhaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final FuncionarioRepository funcionarioRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AutenticacaoService autenticacaoService;
    private final RecuperacaoSenhaRepository recuperacaoSenhaRepository;
    private final RecuperacaoSenhaService recuperacaoSenhaService;

    public AuthController(AuthenticationManager authenticationManager, FuncionarioRepository funcionarioRepository, RecuperacaoSenhaRepository recuperacaoRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, AutenticacaoService autenticacaoService, RecuperacaoSenhaService recuperacaoSenhaService) {
        this.authenticationManager = authenticationManager;
        this.funcionarioRepository = funcionarioRepository;
        this.recuperacaoSenhaRepository = recuperacaoRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.autenticacaoService = autenticacaoService;
        this.recuperacaoSenhaService = recuperacaoSenhaService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        try {
            System.out.println("Tentando logar o usuário: " + request.getEmail());

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getSenha()
                    )
            );
        } catch (BadCredentialsException e) {
            System.out.println("Aviso: Credenciais inválidas para o email " + request.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas.");
        } catch (Exception e) {
            System.out.println("Erro inesperado durante o login: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno.");
        }

        Funcionario funcionario = funcionarioRepository.findByEmail(request.getEmail()).orElseThrow();

        String token = jwtUtil.generateToken(request.getEmail());

        return ResponseEntity.ok(
                new LoginResponse(
                        funcionario.getId(),
                        token,
                        funcionario.getNome(),
                        funcionario.getCargo().name(),
                        funcionario.isTrocarSenha(),
                        funcionario.getEmail(),
                        funcionario.getTelefone()
                )
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
                request.getCpf(),
                passwordEncoder.encode(request.getSenha()),
                request.getTelefone(),
                request.getCargo()
        );

        funcionarioRepository.save(novoFunc);

        return ResponseEntity.ok("Funcionário cadastrado com sucesso!");
    }

    @PostMapping("/recuperar-senha")
    public ResponseEntity<?> recuperarSenha(@RequestBody RecuperarSenhaDTO dto) {
        recuperacaoSenhaService.solicitarRecuperacao(dto.email());
        return  ResponseEntity.ok("Se o email existir em nosso sistema, enviaremos um link de recuperação.");
    }

    public ResponseEntity<?> redefinirSenha(@RequestBody RedefinirSenhaDTO dto) {
        try {
            recuperacaoSenhaService.redefinirSenha(dto.token(), dto.novaSenha());
            return ResponseEntity.ok("Senha redefinida com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
