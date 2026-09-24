package com.huellitas.vetturno.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huellitas.vetturno.dto.VeterinarioDTO;
import com.huellitas.vetturno.dto.VeterinarioRequest;
import com.huellitas.vetturno.model.Veterinario;
import com.huellitas.vetturno.repository.VeterinarioRepository;

@Service
public class VeterinarioService {

    private final VeterinarioRepository veterinarioRepository;

    public VeterinarioService(VeterinarioRepository veterinarioRepository) {
        this.veterinarioRepository = veterinarioRepository;
    }

    @Transactional
    public VeterinarioDTO crear(VeterinarioRequest request) {
        Veterinario veterinario = new Veterinario(
                request.getNombre().trim(),
                request.getEspecialidad().trim());
        return new VeterinarioDTO(veterinarioRepository.save(veterinario));
    }

    @Transactional(readOnly = true)
    public List<VeterinarioDTO> listar() {
        return veterinarioRepository.findAll().stream()
                .map(VeterinarioDTO::new)
                .toList();
    }
}
