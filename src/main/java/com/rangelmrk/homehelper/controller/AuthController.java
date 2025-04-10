package com.rangelmrk.homehelper.controller;

import com.rangelmrk.homehelper.dto.AuthDTO;
import com.rangelmrk.homehelper.dto.UsuarioDTO;
import com.rangelmrk.homehelper.model.Usuario;
import com.rangelmrk.homehelper.service.JwtService;
import com.rangelmrk.homehelper.service.UsuarioService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Value("${api.security.token.secret}")
    private String secret;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UsuarioService usuarioService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDTO.LoginRequest creds) throws ExecutionException, InterruptedException {
        Usuario usuario = usuarioService.buscarPorUsername(creds.getUsername());

        if (usuario == null || !passwordEncoder.matches(creds.getSenha(), usuario.getSenha())) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body("Usuário ou senha inválidos");
        }

        String token = jwtService.gerarToken(usuario.getUsername());
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UsuarioDTO.RegistroRequest dto) throws ExecutionException, InterruptedException {
        usuarioService.registrar(dto);
        return ResponseEntity.ok("Usuário registrado com sucesso");
    }
}
