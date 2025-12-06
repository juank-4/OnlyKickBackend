package com.example.OnlyKick.controller;

import com.example.OnlyKick.model.Usuario;
import com.example.OnlyKick.service.UsuarioService;
import com.example.OnlyKick.security.JwtUtil; // <--- Importante
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager; // <--- Importante
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // <--- Importante
import org.springframework.security.core.Authentication; // <--- Importante
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AuthenticationManager authenticationManager; // <--- Nuevo

    @Autowired
    private JwtUtil jwtUtil; // <--- Nuevo

    @GetMapping
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        List<Usuario> usuarios = usuarioService.findAll();
        if (usuarios.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Integer id) {
        Usuario usuario = usuarioService.findById(id);
        if (usuario == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(usuario);
    }

    // --- LOGIN MODIFICADO PARA DEVOLVER TOKEN ---
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuarioLogin) {
        try {
            // 1. Spring Security intenta validar usuario y contraseña
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usuarioLogin.getEmail(), usuarioLogin.getPasswordHash())
            );

            // 2. Si las credenciales son válidas, generamos el Token
            if (authentication.isAuthenticated()) {
                String token = jwtUtil.generateToken(usuarioLogin.getEmail());
                // Devolvemos el token en un JSON: { "token": "eyJhbGciOi..." }
                return ResponseEntity.ok(Collections.singletonMap("token", token));
            }
        
        } catch (Exception e) {
            // Si falla, entra aquí
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error de autenticación");
    }
    // ---------------------------------------------

    @PostMapping("/registro")
    public ResponseEntity<Usuario> registrarUsuario(@RequestBody Usuario usuario) {
        Usuario nuevoUsuario = usuarioService.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable Integer id, @RequestBody Usuario usuario) {
        usuario.setIdUsuario(id);
        Usuario updated = usuarioService.save(usuario); 
        return ResponseEntity.ok(updated);
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<Usuario> partialUpdateUsuario(@PathVariable Integer id, @RequestBody Usuario usuario) {
        Usuario updated = usuarioService.partialUpdate(id, usuario);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Integer id) {
        usuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}