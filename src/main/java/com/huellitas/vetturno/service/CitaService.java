package com.huellitas.vetturno.service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huellitas.vetturno.dto.CitaDTO;
import com.huellitas.vetturno.dto.CitaRequest;
import com.huellitas.vetturno.exception.ReglaNegocioException;
import com.huellitas.vetturno.model.Cita;
import com.huellitas.vetturno.model.Mascota;
import com.huellitas.vetturno.model.Veterinario;
import com.huellitas.vetturno.repository.CitaRepository;
import com.huellitas.vetturno.repository.MascotaRepository;
import com.huellitas.vetturno.repository.VeterinarioRepository;

/**
 * Reglas de la agenda, la cita se guarda siempre y cuando la mascota y el veterinario existan,
 * la fecha sea futura y el veterinario no tenga otra cita en el mismo horario.
 */
@Service
public class CitaService {

    private final CitaRepository citaRepository;
    private final MascotaRepository mascotaRepository;
    private final VeterinarioRepository veterinarioRepository;
    private final Clock clock;

    public CitaService(CitaRepository citaRepository,
                       MascotaRepository mascotaRepository,
                       VeterinarioRepository veterinarioRepository,
                       Clock clock) {
        this.citaRepository = citaRepository;
        this.mascotaRepository = mascotaRepository;
        this.veterinarioRepository = veterinarioRepository;
        this.clock = clock;
    }

    @Transactional
    public CitaDTO agendar(CitaRequest request) {
        // La agenda trabaja por minutos, 10:00:30 y 10:00 se toman como el mismo horario.
        LocalDateTime fechaHora = request.getFechaHora().truncatedTo(ChronoUnit.MINUTES);

        if (!fechaHora.isAfter(LocalDateTime.now(clock))) {
            throw new ReglaNegocioException("La fecha y hora de la cita debe ser futura");
        }

        Mascota mascota = mascotaRepository.findById(request.getMascotaId())
                .orElseThrow(() -> new ReglaNegocioException(
                        "No existe una mascota con id " + request.getMascotaId()));
        Veterinario veterinario = buscarVeterinario(request.getVeterinarioId());

        if (citaRepository.existsByVeterinarioIdAndFechaHora(veterinario.getId(), fechaHora)) {
            throw new ReglaNegocioException(
                    "El veterinario " + veterinario.getNombre() + " ya tiene una cita en ese horario");
        }

        Cita cita = new Cita(fechaHora, request.getMotivo().trim(), mascota, veterinario);
        return new CitaDTO(citaRepository.save(cita));
    }

    @Transactional(readOnly = true)
    public List<CitaDTO> listar() {
        return citaRepository.findAllByOrderByFechaHoraAsc().stream()
                .map(CitaDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CitaDTO> listarPorVeterinario(Long veterinarioId) {
        buscarVeterinario(veterinarioId);
        return citaRepository.findByVeterinarioIdOrderByFechaHoraAsc(veterinarioId).stream()
                .map(CitaDTO::new)
                .toList();
    }

    private Veterinario buscarVeterinario(Long veterinarioId) {
        return veterinarioRepository.findById(veterinarioId)
                .orElseThrow(() -> new ReglaNegocioException(
                        "No existe un veterinario con id " + veterinarioId));
    }
}
