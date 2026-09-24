package com.huellitas.vetturno.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huellitas.vetturno.dto.PropietarioDTO;
import com.huellitas.vetturno.dto.PropietarioRequest;
import com.huellitas.vetturno.model.Propietario;
import com.huellitas.vetturno.repository.PropietarioRepository;

@Service
public class PropietarioService {

    private final PropietarioRepository propietarioRepository;

    public PropietarioService(PropietarioRepository propietarioRepository) {
        this.propietarioRepository = propietarioRepository;
    }

    @Transactional
    public PropietarioDTO crear(PropietarioRequest request) {
        Propietario propietario = new Propietario(
                request.getNombre().trim(),
                request.getTelefono().trim(),
                normalizarEmail(request.getEmail()));
        return new PropietarioDTO(propietarioRepository.save(propietario));
    }

    @Transactional(readOnly = true)
    public List<PropietarioDTO> listar() {
        return propietarioRepository.findAll().stream()
                .map(PropietarioDTO::new)
                .toList();
    }

    private String normalizarEmail(String email) {
        if (email == null || email.isBlank()) {
            return null;
        }
        return email.trim().toLowerCase();
    }
}
