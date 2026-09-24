package com.huellitas.vetturno.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huellitas.vetturno.dto.MascotaDTO;
import com.huellitas.vetturno.dto.MascotaRequest;
import com.huellitas.vetturno.exception.ReglaNegocioException;
import com.huellitas.vetturno.model.Mascota;
import com.huellitas.vetturno.model.Propietario;
import com.huellitas.vetturno.repository.MascotaRepository;
import com.huellitas.vetturno.repository.PropietarioRepository;

@Service
public class MascotaService {

    private final MascotaRepository mascotaRepository;
    private final PropietarioRepository propietarioRepository;

    public MascotaService(MascotaRepository mascotaRepository, PropietarioRepository propietarioRepository) {
        this.mascotaRepository = mascotaRepository;
        this.propietarioRepository = propietarioRepository;
    }

    /** Crea la mascota siempre y cuando el propietario exista. */
    @Transactional
    public MascotaDTO crear(MascotaRequest request) {
        Propietario propietario = propietarioRepository.findById(request.getPropietarioId())
                .orElseThrow(() -> new ReglaNegocioException(
                        "No existe un propietario con id " + request.getPropietarioId()));

        Mascota mascota = new Mascota(
                request.getNombre().trim(),
                request.getEspecie().trim(),
                limpiar(request.getRaza()),
                propietario);
        return new MascotaDTO(mascotaRepository.save(mascota));
    }

    @Transactional(readOnly = true)
    public List<MascotaDTO> listar() {
        return mascotaRepository.findAllByOrderByNombreAsc().stream()
                .map(MascotaDTO::new)
                .toList();
    }

    private String limpiar(String texto) {
        return (texto == null || texto.isBlank()) ? null : texto.trim();
    }
}
