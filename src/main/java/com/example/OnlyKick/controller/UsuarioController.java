package com.example.OnlyKick.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.OnlyKick.dto.UsuarioDTO; 
import com.example.OnlyKick.model.Usuario;
import com.example.OnlyKick.repository.UsuarioRepository;
import com.example.OnlyKick.security.JwtUtil;
import com.example.OnlyKick.service.DtoConverter; 
import com.example.OnlyKick.service.UsuarioService;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    // Inyectamos el convertidor que creamos
    @Autowired
    private DtoConverter dtoConverter;

    // --- OBTENER TODOS (Devuelve lista de DTOs) ---
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> getAllUsuarios() {
        List<Usuario> usuarios = usuarioService.findAll();
        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        // Convertimos la lista de Entidades a lista de DTOs
        List<UsuarioDTO> dtos = usuarios.stream()
                .map(dtoConverter::convertToDto)
                .collect(Collectors.toList());
                
        return ResponseEntity.ok(dtos);
    }

    // --- OBTENER POR ID (Devuelve un DTO) ---
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getUsuarioById(@PathVariable Integer id) {
        Usuario usuario = usuarioService.findById(id);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        // Convertimos a DTO antes de enviar
        return ResponseEntity.ok(dtoConverter.convertToDto(usuario));
    }

    // --- LOGIN (Devuelve Token + UsuarioDTO) ---
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuarioLogin) {
        try {
            // 1. Validar credenciales con Spring Security
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usuarioLogin.getEmail(), usuarioLogin.getPasswordHash())
            );

            // 2. Si es válido...
            if (authentication.isAuthenticated()) {
                // A. Generar Token
                String token = jwtUtil.generateToken(usuarioLogin.getEmail());
                
                // B. Buscar usuario completo en BD
                Usuario usuarioCompleto = usuarioRepository.findByEmail(usuarioLogin.getEmail())
                        .orElse(null);

                // C. Convertir a DTO (Aquí se limpia la recursión y se agregan los detalles de compras)
                UsuarioDTO userDto = dtoConverter.convertToDto(usuarioCompleto);

                // D. Construir respuesta
                Map<String, Object> response = new HashMap<>();
                response.put("token", token);
                response.put("user", userDto); // Enviamos el DTO seguro

                return ResponseEntity.ok(response);
            }
        
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error de autenticación");
    }

    // --- REGISTRO (Devuelve el DTO del nuevo usuario) ---
    @PostMapping("/registro")
    public ResponseEntity<UsuarioDTO> registrarUsuario(@RequestBody Usuario usuario) {
        Usuario nuevoUsuario = usuarioService.save(usuario);
        // Devolvemos el DTO para no mostrar el hash de la contraseña ni campos internos
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoConverter.convertToDto(nuevoUsuario));
    }

    // --- ACTUALIZAR COMPLETO ---
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> updateUsuario(@PathVariable Integer id, @RequestBody Usuario usuario) {
        usuario.setIdUsuario(id);
        Usuario updated = usuarioService.save(usuario); 
        return ResponseEntity.ok(dtoConverter.convertToDto(updated));
    }
    
    // --- ACTUALIZAR PARCIAL ---
    @PatchMapping("/{id}")
    public ResponseEntity<UsuarioDTO> partialUpdateUsuario(@PathVariable Integer id, @RequestBody Usuario usuario) {
        Usuario updated = usuarioService.partialUpdate(id, usuario);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dtoConverter.convertToDto(updated));
    }

    // --- ELIMINAR ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Integer id) {
        usuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}