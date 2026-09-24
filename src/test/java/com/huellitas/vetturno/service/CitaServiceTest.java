package com.huellitas.vetturno.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.huellitas.vetturno.dto.CitaDTO;
import com.huellitas.vetturno.dto.CitaRequest;
import com.huellitas.vetturno.exception.ReglaNegocioException;
import com.huellitas.vetturno.model.Cita;
import com.huellitas.vetturno.model.Mascota;
import com.huellitas.vetturno.model.Propietario;
import com.huellitas.vetturno.model.Veterinario;
import com.huellitas.vetturno.repository.CitaRepository;
import com.huellitas.vetturno.repository.MascotaRepository;
import com.huellitas.vetturno.repository.VeterinarioRepository;

@ExtendWith(MockitoExtension.class)
class CitaServiceTest {

    private static final ZoneId ZONA = ZoneId.of("America/Mexico_City");
    private static final LocalDateTime AHORA = LocalDateTime.of(2026, 3, 10, 9, 0);

    @Mock
    private CitaRepository citaRepository;
    @Mock
    private MascotaRepository mascotaRepository;
    @Mock
    private VeterinarioRepository veterinarioRepository;

    private CitaService citaService;
    private Mascota firulais;
    private Veterinario andres;

    @BeforeEach
    void setUp() {
        Clock reloj = Clock.fixed(AHORA.atZone(ZONA).toInstant(), ZONA);
        citaService = new CitaService(citaRepository, mascotaRepository, veterinarioRepository, reloj);

        Propietario laura = new Propietario("Laura Gómez", "6181234567", "laura@correo.com");
        ReflectionTestUtils.setField(laura, "id", 1L);
        firulais = new Mascota("Firulais", "Perro", "Criollo", laura);
        ReflectionTestUtils.setField(firulais, "id", 2L);
        andres = new Veterinario("Andrés Ruiz", "Medicina general");
        ReflectionTestUtils.setField(andres, "id", 3L);
    }

    private CitaRequest solicitud(LocalDateTime fechaHora) {
        CitaRequest request = new CitaRequest();
        request.setFechaHora(fechaHora);
        request.setMotivo("  Vacunación anual ");
        request.setMascotaId(2L);
        request.setVeterinarioId(3L);
        return request;
    }

    @Test
    void agendaCitaFuturaConReferenciasValidas() {
        LocalDateTime manana = AHORA.plusDays(1).withSecond(45);
        when(mascotaRepository.findById(2L)).thenReturn(Optional.of(firulais));
        when(veterinarioRepository.findById(3L)).thenReturn(Optional.of(andres));
        when(citaRepository.existsByVeterinarioIdAndFechaHora(3L, manana.withSecond(0))).thenReturn(false);
        when(citaRepository.save(any(Cita.class))).thenAnswer(inv -> inv.getArgument(0));

        CitaDTO dto = citaService.agendar(solicitud(manana));

        assertThat(dto.getFechaHora()).isEqualTo(manana.withSecond(0));
        assertThat(dto.getMotivo()).isEqualTo("Vacunación anual");
        assertThat(dto.getMascota()).isEqualTo("Firulais");
        assertThat(dto.getPropietario()).isEqualTo("Laura Gómez");
        assertThat(dto.getVeterinario()).isEqualTo("Andrés Ruiz");
    }

    @Test
    void rechazaFechaPasadaSinConsultarNiGuardar() {
        assertThatThrownBy(() -> citaService.agendar(solicitud(AHORA.minusHours(1))))
                .isInstanceOf(ReglaNegocioException.class)
                .hasMessageContaining("futura");

        verify(mascotaRepository, never()).findById(any());
        verify(citaRepository, never()).save(any());
    }

    @Test
    void rechazaFechaIgualAlMomentoActual() {
        assertThatThrownBy(() -> citaService.agendar(solicitud(AHORA)))
                .isInstanceOf(ReglaNegocioException.class);
    }

    @Test
    void rechazaHorarioOcupadoDelMismoVeterinario() {
        LocalDateTime manana = AHORA.plusDays(1);
        when(mascotaRepository.findById(2L)).thenReturn(Optional.of(firulais));
        when(veterinarioRepository.findById(3L)).thenReturn(Optional.of(andres));
        when(citaRepository.existsByVeterinarioIdAndFechaHora(3L, manana)).thenReturn(true);

        assertThatThrownBy(() -> citaService.agendar(solicitud(manana)))
                .isInstanceOf(ReglaNegocioException.class)
                .hasMessageContaining("ya tiene una cita");

        verify(citaRepository, never()).save(any());
    }

    @Test
    void rechazaMascotaInexistente() {
        when(mascotaRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> citaService.agendar(solicitud(AHORA.plusDays(1))))
                .isInstanceOf(ReglaNegocioException.class)
                .hasMessageContaining("mascota");

        verify(citaRepository, never()).save(any());
    }

    @Test
    void rechazaVeterinarioInexistente() {
        when(mascotaRepository.findById(2L)).thenReturn(Optional.of(firulais));
        when(veterinarioRepository.findById(3L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> citaService.agendar(solicitud(AHORA.plusDays(1))))
                .isInstanceOf(ReglaNegocioException.class)
                .hasMessageContaining("veterinario");

        verify(citaRepository, never()).save(any());
    }

    @Test
    void filtraAgendaPorVeterinarioExistente() {
        Cita cita = new Cita(AHORA.plusDays(2), "Control", firulais, andres);
        when(veterinarioRepository.findById(3L)).thenReturn(Optional.of(andres));
        when(citaRepository.findByVeterinarioIdOrderByFechaHoraAsc(3L)).thenReturn(List.of(cita));

        List<CitaDTO> agenda = citaService.listarPorVeterinario(3L);

        assertThat(agenda).hasSize(1);
        assertThat(agenda.get(0).getVeterinarioId()).isEqualTo(3L);
    }

    @Test
    void filtroPorVeterinarioInexistenteEsErrorDeNegocio() {
        when(veterinarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> citaService.listarPorVeterinario(99L))
                .isInstanceOf(ReglaNegocioException.class);
    }
}
