package com.example.OnlyKick.service;

import com.example.OnlyKick.model.Direcciones;
import com.example.OnlyKick.model.Usuario;
import com.example.OnlyKick.model.Venta;
import com.example.OnlyKick.repository.DireccionesRepository;
import com.example.OnlyKick.repository.UsuarioRepository;
import com.example.OnlyKick.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@SuppressWarnings("null")
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DireccionesRepository direccionesRepository;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //Obtiene a todos los usuarios
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    //Obtiene un usuario por su ID
    public Usuario findById(Integer id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    // Proceso de login de usuario
    public Usuario login(Usuario usuarioLogin) {
        // Buscamos al usuario por el email que viene en el objeto
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(usuarioLogin.getEmail());

        if (usuarioOpt.isPresent()) {
            Usuario usuarioEncontrado = usuarioOpt.get();
            // Comparamos la contraseña con la de la BD
            if (passwordEncoder.matches(usuarioLogin.getPasswordHash(), usuarioEncontrado.getPasswordHash())) {
                return usuarioEncontrado;
            }
        }
        return null;
    }

    //Guarda un nuevo usuario cuando se registra
    public Usuario save(Usuario usuario) {
        // Nos aseguramos de que sea un ID nulo para que sea una creación
        usuario.setId_usuario(null);
        
        // Hasheamos la contraseña
        String passwordHasheada = passwordEncoder.encode(usuario.getPasswordHash());
        usuario.setPasswordHash(passwordHasheada);
        
        return usuarioRepository.save(usuario);
    }

    //Actualiza un usuario parcialmente para el endpoint PATCH
    public Usuario partialUpdate(Integer id, Usuario usuarioData) {
        Usuario existingUsuario = usuarioRepository.findById(id).orElse(null);
        
        if (existingUsuario != null) {
            // Actualiza solo los campos presentes en el JSON de entrada
            if (usuarioData.getNombreUsuario() != null) {
                existingUsuario.setNombreUsuario(usuarioData.getNombreUsuario());
            }
            if (usuarioData.getEmail() != null) {
                existingUsuario.setEmail(usuarioData.getEmail());
            }
            // Si el usuario envió una nueva contraseña, la hasheamos
            if (usuarioData.getPasswordHash() != null) {
                existingUsuario.setPasswordHash(passwordEncoder.encode(usuarioData.getPasswordHash()));
            }
            
            return usuarioRepository.save(existingUsuario);
        }
        return null; // No se encontró el usuario
    }

    // Eliminacion por cascada de un usuario
    public void deleteById(Integer id) {
        //Buscamos todas las ventas asociadas a este usuario
        List<Venta> ventas = ventaRepository.findByUsuarioIdUsuario(id);
        for (Venta venta : ventas) {
            venta.setUsuario(null); // Quitamos la referencia al usuario
            ventaRepository.save(venta);
        }
        // Buscamos todas las direcciones asociadas, luego las borramos
        List<Direcciones> direcciones = direccionesRepository.findByUsuarioIdUsuario(id);
        direccionesRepository.deleteAll(direcciones);
        //Borra al usuario por completo
        usuarioRepository.deleteById(id);
    }
}