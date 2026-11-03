
package com.chemigram.service;

import com.chemigram.model.Gremio;
import com.chemigram.model.MiembroGremio;
import com.chemigram.model.Usuario;
import com.chemigram.repository.GremioRepository;
import com.chemigram.repository.MiembroGremioRepository;
import com.chemigram.repository.UsuarioRepository;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GremioService {

    private final GremioRepository gremioRepository;
    private final MiembroGremioRepository miembroRepository;
    private final UsuarioRepository usuarioRepository;

    public GremioService(
            GremioRepository gremioRepository,
            MiembroGremioRepository miembroRepository,
            UsuarioRepository usuarioRepository) {

        this.gremioRepository = gremioRepository;
        this.miembroRepository = miembroRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Gremio crearGremio(
            String nombre,
            String descripcion,
            String icono,
            String email) {

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre del gremio es obligatorio."
            );
        }

        Usuario creador = obtenerUsuario(email);
        String nombreNormalizado = nombre.trim();

        if (gremioRepository.existsByNombreIgnoreCase(
                nombreNormalizado)) {
            throw new IllegalArgumentException(
                    "Ya existe un gremio con ese nombre."
            );
        }

        Gremio gremio = new Gremio(
                nombreNormalizado,
                descripcion,
                icono,
                creador
        );

        Gremio gremioGuardado = gremioRepository.save(gremio);

        MiembroGremio miembroFundador = new MiembroGremio(
                creador,
                gremioGuardado
        );

        miembroRepository.save(miembroFundador);

        return gremioGuardado;
    }

    @Transactional
    public MiembroGremio unirUsuario(
            Long gremioId,
            String email) {

        Usuario usuario = obtenerUsuario(email);

        if (!gremioRepository.existsById(gremioId)) {
            throw new IllegalArgumentException(
                    "El gremio no existe."
            );
        }

        if (miembroRepository.existsByUsuarioIdAndGremioId(
                usuario.getId(), gremioId)) {
            throw new IllegalArgumentException(
                    "Ya perteneces a este gremio."
            );
        }

        Gremio gremio = gremioRepository.findById(gremioId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "El gremio no existe."
                ));

        MiembroGremio miembro = new MiembroGremio(
                usuario,
                gremio
        );

        return miembroRepository.save(miembro);
    }

    @Transactional(readOnly = true)
    public List<Gremio> listarGremios() {
        return gremioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<MiembroGremio> listarMiembros(
            Long gremioId,
            String email) {

        Usuario usuario = obtenerUsuario(email);

        if (!gremioRepository.existsById(gremioId)) {
            throw new IllegalArgumentException(
                    "El gremio no existe."
            );
        }

        comprobarMembresia(usuario.getId(), gremioId);

        return miembroRepository.findByGremioId(gremioId);
    }

    @Transactional
    public MiembroGremio asignarCatador(
            Long gremioId,
            Long usuarioId,
            String email) {

        Usuario solicitante = obtenerUsuario(email);

        comprobarCreadorGremio(solicitante.getId(), gremioId);

        MiembroGremio nuevoCatador = miembroRepository
                .findByUsuarioIdAndGremioId(usuarioId, gremioId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "El usuario no pertenece a este gremio."
                ));

        miembroRepository.findByGremioIdAndEsCatadorTrue(gremioId)
                .ifPresent(anterior -> {
                    anterior.quitarRolCatador();
                    miembroRepository.save(anterior);
                });

        nuevoCatador.convertirEnCatador();

        return miembroRepository.save(nuevoCatador);
    }

    @Transactional
    public void retirarCatador(
            Long gremioId,
            String email) {

        Usuario solicitante = obtenerUsuario(email);

        comprobarCreadorGremio(solicitante.getId(), gremioId);

        miembroRepository.findByGremioIdAndEsCatadorTrue(gremioId)
                .ifPresentOrElse(
                        catador -> {
                            catador.quitarRolCatador();
                            miembroRepository.save(catador);
                        },
                        () -> {
                            throw new IllegalArgumentException(
                                    "Este gremio no tiene un catador oficial."
                            );
                        }
                );
    }

    private Usuario obtenerUsuario(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            throw new IllegalArgumentException(
                    "El usuario autenticado no existe."
            );
        }

        return usuario;
    }

    private void comprobarMembresia(
            Long usuarioId,
            Long gremioId) {

        boolean esMiembro = miembroRepository
                .existsByUsuarioIdAndGremioId(usuarioId, gremioId);

        if (!esMiembro) {
            throw new AccessDeniedException(
                    "Debes pertenecer al gremio para realizar esta acción."
            );
        }
    }

    private void comprobarCreadorGremio(
            Long usuarioId,
            Long gremioId) {

        Gremio gremio = gremioRepository.findById(gremioId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "El gremio no existe."
                ));

        if (!gremio.getCreador().getId().equals(usuarioId)) {
            throw new AccessDeniedException(
                    "Solo el creador del gremio puede gestionar al catador."
            );
        }
}
}

