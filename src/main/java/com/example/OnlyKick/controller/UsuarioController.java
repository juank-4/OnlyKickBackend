package com.example.OnlyKick.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map; // <--- Importante: Importamos el repositorio

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping; // <--- Necesario para el Map
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.OnlyKick.model.Usuario;
import com.example.OnlyKick.repository.UsuarioRepository;
import com.example.OnlyKick.security.JwtUtil;
import com.example.OnlyKick.service.UsuarioService;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository; // <--- Inyectamos esto para buscar al usuario en el login

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

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

    // --- LOGIN MODIFICADO PARA DEVOLVER TOKEN Y USUARIO ---
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuarioLogin) {
        try {
            // 1. Spring Security intenta validar usuario y contraseña
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usuarioLogin.getEmail(), usuarioLogin.getPasswordHash())
            );

            // 2. Si las credenciales son válidas...
            if (authentication.isAuthenticated()) {
                // A. Generamos el Token
                String token = jwtUtil.generateToken(usuarioLogin.getEmail());
                
                // B. Buscamos el objeto Usuario completo en la base de datos
                Usuario usuarioCompleto = usuarioRepository.findByEmail(usuarioLogin.getEmail())
                        .orElse(null);

                // C. Creamos una respuesta con ambos datos
                Map<String, Object> response = new HashMap<>();
                response.put("token", token);
                response.put("user", usuarioCompleto); // Enviamos el usuario para que React pueda leer el rol

                return ResponseEntity.ok(response);
            }
        
        } catch (Exception e) {
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