package com.Backend.MediConnect.clinica.domain.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Backend.MediConnect.clinica.domain.dto.AdminLocalResponseDTO;
import com.Backend.MediConnect.clinica.domain.dto.AuthResponse;
import com.Backend.MediConnect.clinica.domain.dto.EditarAdminLocalDTO;
import com.Backend.MediConnect.clinica.domain.dto.RegistroAdminLocalDTO;
import com.Backend.MediConnect.clinica.domain.interfaces.IAdminTotalService;
import com.Backend.MediConnect.clinica.domain.repository.AdminLocalRepository;
import com.Backend.MediConnect.clinica.domain.repository.SedeRepository;
import com.Backend.MediConnect.clinica.domain.repository.UsuarioRepository;
import com.Backend.MediConnect.clinica.persistance.entity.AdministadorLocal;
import com.Backend.MediConnect.clinica.persistance.entity.Sede;
import com.Backend.MediConnect.clinica.persistance.entity.Usuario;
import com.Backend.MediConnect.clinica.web.mapper.AdminLocalMapper;
import com.Backend.MediConnect.clinica.web.security.JwtUtil;

@Service
public class AdminTotalService implements IAdminTotalService {

    private final AdminLocalRepository adminLocalRepo;
    private final UsuarioRepository usuarioRepo;
    private final SedeRepository sedeRepo;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AdminTotalService(AdminLocalRepository adminLocalRepo,
            UsuarioRepository usuarioRepo,
            SedeRepository sedeRepo,
            JwtUtil jwtUtil,
            PasswordEncoder passwordEncoder) {
        this.adminLocalRepo = adminLocalRepo;
        this.usuarioRepo = usuarioRepo;
        this.sedeRepo = sedeRepo;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public AdminLocalResponseDTO crearAdminLocal(RegistroAdminLocalDTO dto) {

        if (usuarioRepo.existsByDni(dto.getDni()))
            throw new RuntimeException("DNI ya registrado");

        Sede sede = sedeRepo.findById(dto.getIdSede())
                .orElseThrow(() -> new RuntimeException("Sede no encontrada"));

        AdministadorLocal admin = new AdministadorLocal();

        admin.setPrimerNombre(dto.getPrimerNombre());
        admin.setSegundoNombre(dto.getSegundoNombre());

        admin.setPrimerApellido(dto.getPrimerApellido());
        admin.setSegundoApellido(dto.getSegundoApellido());

        admin.setDni(dto.getDni());
        admin.setSede(sede);

        AdministadorLocal adminGuardado = adminLocalRepo.save(admin);

        Usuario usuario = new Usuario();
        usuario.setDni(dto.getDni());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setRol("ADMIN_LOCAL");

        usuarioRepo.save(usuario);

        AdminLocalResponseDTO response = new AdminLocalResponseDTO();

        response.setIdAdminLocal(adminGuardado.getIdAdminLocal());
        response.setPrimerNombre(adminGuardado.getPrimerNombre());
        response.setSegundoNombre(adminGuardado.getSegundoNombre());
        response.setPrimerApellido(adminGuardado.getPrimerApellido());
        response.setSegundoApellido(adminGuardado.getSegundoApellido());
        response.setDni(adminGuardado.getDni());
        response.setNombreSede(sede.getNombreSede());

        return response;
    }

    @Override
    @Transactional
    public void eliminarAdminLocal(Integer idAdminLocal) {
        AdministadorLocal admin = adminLocalRepo.findById(idAdminLocal)
                .orElseThrow(() -> new RuntimeException("Admin Local no encontrado"));
        usuarioRepo.findByDni(admin.getDni()).ifPresent(usuarioRepo::delete);
        adminLocalRepo.delete(admin);
    }

    @Override
    @Transactional
    public AdminLocalResponseDTO editarAdminLocal(Integer idAdminLocal, EditarAdminLocalDTO dto) {
        AdministadorLocal admin = adminLocalRepo.findById(idAdminLocal)
                .orElseThrow(() -> new RuntimeException("Admin Local no encontrado"));

        Sede sede = sedeRepo.findById(dto.getIdSede())
                .orElseThrow(() -> new RuntimeException("Sede no encontrada"));

        admin.setPrimerNombre(dto.getPrimerNombre());
        admin.setSegundoNombre(dto.getSegundoNombre());
        admin.setPrimerApellido(dto.getPrimerApellido());
        admin.setSegundoApellido(dto.getSegundoApellido());
        admin.setSede(sede);

       return AdminLocalMapper.toResponse(adminLocalRepo.save(admin));
    }

public List<AdminLocalResponseDTO> consultarAdminLocales() {
    return adminLocalRepo.findAll()
            .stream()
            .map(AdminLocalMapper::toResponse) // 👈 Cambia esta línea aquí
            .collect(Collectors.toList());
}
}