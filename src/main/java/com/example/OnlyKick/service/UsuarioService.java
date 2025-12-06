package com.example.OnlyKick.service;

import com.example.OnlyKick.model.Direcciones;
import com.example.OnlyKick.model.Usuario;
import com.example.OnlyKick.model.Venta;
import com.example.OnlyKick.repository.DireccionesRepository;
import com.example.OnlyKick.repository.UsuarioRepository;
import com.example.OnlyKick.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService implements UserDetailsService { // <--- 1. Implementamos esta interfaz

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DireccionesRepository direccionesRepository;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    @Lazy // Usamos Lazy para evitar ciclos de dependencia con SecurityConfig
    private PasswordEncoder passwordEncoder;

    // --- NUEVO MÉTODO OBLIGATORIO DE SEGURIDAD ---
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        // Convertimos tu entidad Usuario a un objeto que Spring Security entienda
        return new org.springframework.security.core.userdetails.User(
                usuario.getEmail(),
                usuario.getPasswordHash(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getRol())) // Asignamos el rol (ej: ROLE_ADMIN)
        );
    }
    // ---------------------------------------------

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Usuario findById(Integer id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    // Este login manual ya no es estrictamente necesario con JWT, pero lo dejamos por compatibilidad si quieres
    public Usuario login(Usuario usuarioLogin) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(usuarioLogin.getEmail());
        if (usuarioOpt.isPresent()) {
            Usuario usuarioEncontrado = usuarioOpt.get();
            if (passwordEncoder.matches(usuarioLogin.getPasswordHash(), usuarioEncontrado.getPasswordHash())) {
                return usuarioEncontrado;
            }
        }
        return null;
    }

    public Usuario save(Usuario usuario) {
        if (usuario.getIdUsuario() == null) { // Solo si es nuevo
            usuario.setIdUsuario(null); // Asegurar creación
            
            // Asignar rol por defecto si no viene
            if (usuario.getRol() == null || usuario.getRol().isEmpty()) {
                usuario.setRol("user");
            }
            // Hashear contraseña
            usuario.setPasswordHash(passwordEncoder.encode(usuario.getPasswordHash()));
        }
        return usuarioRepository.save(usuario);
    }
    
    // Método para actualizar (si ya viene con ID, no hasheamos de nuevo a menos que cambie la password)
    public Usuario update(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario partialUpdate(Integer id, Usuario usuarioData) {
        Usuario existingUsuario = usuarioRepository.findById(id).orElse(null);
        if (existingUsuario != null) {
            if (usuarioData.getNombreUsuario() != null) existingUsuario.setNombreUsuario(usuarioData.getNombreUsuario());
            if (usuarioData.getEmail() != null) existingUsuario.setEmail(usuarioData.getEmail());
            if (usuarioData.getRol() != null) existingUsuario.setRol(usuarioData.getRol());
            
            // Si viene contraseña nueva, la hasheamos
            if (usuarioData.getPasswordHash() != null && !usuarioData.getPasswordHash().isEmpty()) {
                existingUsuario.setPasswordHash(passwordEncoder.encode(usuarioData.getPasswordHash()));
            }
            return usuarioRepository.save(existingUsuario);
        }
        return null;
    }

    public void deleteById(Integer id) {
        List<Venta> ventas = ventaRepository.findByUsuarioIdUsuario(id);
        for (Venta venta : ventas) {
            venta.setUsuario(null);
            ventaRepository.save(venta);
        }
        List<Direcciones> direcciones = direccionesRepository.findByUsuarioIdUsuario(id);
        direccionesRepository.deleteAll(direcciones);
        usuarioRepository.deleteById(id);
    }
}