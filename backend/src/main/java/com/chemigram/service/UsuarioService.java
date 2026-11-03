package com.chemigram.service;

import com.chemigram.dto.RegistroUsuarioRequest;
import com.chemigram.dto.UsuarioResponse;
import com.chemigram.model.Usuario;
import com.chemigram.repository.UsuarioRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.chemigram.dto.LoginRequest;
import com.chemigram.dto.LoginResponse;
import com.chemigram.security.JwtService;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

 public UsuarioService(
        UsuarioRepository usuarioRepository,
        PasswordEncoder passwordEncoder,
        JwtService jwtService) {

    this.usuarioRepository = usuarioRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
}
public LoginResponse iniciarSesion(LoginRequest request) {

    Usuario usuario = usuarioRepository.findByEmail(request.getEmail());

    if (usuario == null ||
            !passwordEncoder.matches(
                    request.getPassword(),
                    usuario.getPasswordHash())) {

        throw new IllegalArgumentException(
                "Correo o contraseña incorrectos");
    }

    String token = jwtService.generarToken(usuario.getEmail());

    UsuarioResponse usuarioResponse = convertirAResponse(usuario);

    return new LoginResponse(token, usuarioResponse);
}

    public UsuarioResponse registrarUsuario(RegistroUsuarioRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException(
                "Este correo ya está registrado"
            );
        }

        Usuario usuario = new Usuario();

        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setPasswordHash(
            passwordEncoder.encode(request.getPassword())
        );
        usuario.setEspecialidad(request.getEspecialidad());

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        return convertirAResponse(usuarioGuardado);
    }

    public UsuarioResponse buscarPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
            throw new IllegalArgumentException(
                "No existe un usuario con ese correo"
            );
        }

        return convertirAResponse(usuario);
    }

    private UsuarioResponse convertirAResponse(Usuario usuario) {

        return new UsuarioResponse(
            usuario.getId(),
            usuario.getNombre(),
            usuario.getEmail(),
            usuario.getEspecialidad(),
            usuario.getPuntosAlquimia(),
            usuario.getRarezaTotal()
        );
    }
}

