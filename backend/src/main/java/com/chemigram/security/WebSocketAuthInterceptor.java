
package com.chemigram.security;

import com.chemigram.model.Usuario;
import com.chemigram.repository.MiembroGremioRepository;
import com.chemigram.repository.UsuarioRepository;

import io.jsonwebtoken.JwtException;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;
    private final MiembroGremioRepository miembroRepository;

    private static final Pattern DESTINO_GREMIO =
            Pattern.compile("^/topic/gremio/(\\d+)$");

    public WebSocketAuthInterceptor(
            JwtService jwtService,
            UsuarioRepository usuarioRepository,
            MiembroGremioRepository miembroRepository) {

        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
        this.miembroRepository = miembroRepository;
    }

    @Override
    public Message<?> preSend(
            Message<?> message,
            MessageChannel channel) {

        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(
                        message,
                        StompHeaderAccessor.class
                );

        if (accessor == null || accessor.getCommand() == null) {
            return message;
        }

        // Autenticar la conexión STOMP.
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            String authorization =
                    accessor.getFirstNativeHeader("Authorization");

            if (authorization == null ||
                    !authorization.startsWith("Bearer ")) {
                throw new BadCredentialsException(
                        "Debes proporcionar un token JWT."
                );
            }

            String token = authorization.substring(7);

            try {
                if (!jwtService.validarToken(token)) {
                    throw new BadCredentialsException(
                            "El token JWT no es válido."
                    );
                }

                String email = jwtService.obtenerEmail(token);

                UsernamePasswordAuthenticationToken autenticacion =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                AuthorityUtils.NO_AUTHORITIES
                        );

                accessor.setUser(autenticacion);

            } catch (JwtException | IllegalArgumentException excepcion) {
                throw new BadCredentialsException(
                        "No fue posible autenticar la conexión WebSocket.",
                        excepcion
                );
            }
        }

        // Comprobar que el usuario pertenece al gremio.
        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {

            String destino = accessor.getDestination();

            if (destino != null &&
                    destino.startsWith("/topic/gremio/")) {

                if (accessor.getUser() == null) {
                    throw new AccessDeniedException(
                            "Debes estar autenticado."
                    );
                }

                Matcher matcher = DESTINO_GREMIO.matcher(destino);

                if (!matcher.matches()) {
                    throw new AccessDeniedException(
                            "El canal solicitado no es válido."
                    );
                }

                Long gremioId = Long.parseLong(matcher.group(1));

                String email = accessor.getUser().getName();

                Usuario usuario = usuarioRepository.findByEmail(email);

                if (usuario == null ||
                        !miembroRepository.existsByUsuarioIdAndGremioId(
                                usuario.getId(),
                                gremioId
                        )) {

                    throw new AccessDeniedException(
                            "No perteneces a este gremio."
                    );
                }
            }
        }

        return message;
    }
}
