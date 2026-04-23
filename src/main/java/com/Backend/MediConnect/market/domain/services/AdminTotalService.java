package com.Backend.MediConnect.market.domain.services;

import com.Backend.MediConnect.market.domain.dto.*;
import com.Backend.MediConnect.market.domain.interfaces.IAdminTotalService;
import com.Backend.MediConnect.market.web.mapper.EntityMapper;
import com.Backend.MediConnect.market.domain.repository.*;
import com.Backend.MediConnect.market.persistance.entity.*;
import com.Backend.MediConnect.market.web.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

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
    public AuthResponse crearAdminLocal(RegistroAdminLocalDTO dto) {
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
        adminLocalRepo.save(admin);

        Usuario usuario = new Usuario();
        usuario.setDni(dto.getDni());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setRol("ADMIN_LOCAL");
        usuarioRepo.save(usuario);

        String token = jwtUtil.generarToken(dto.getDni(), "ADMIN_LOCAL");
        return new AuthResponse(token, "ADMIN_LOCAL",
                dto.getPrimerNombre() + " " + dto.getPrimerApellido());
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

        return EntityMapper.toAdminLocalResponse(adminLocalRepo.save(admin));
    }

    @Override
    public List<AdminLocalResponseDTO> consultarAdminLocales() {
        return adminLocalRepo.findAll()
                .stream()
                .map(EntityMapper::toAdminLocalResponse)
                .collect(Collectors.toList());
    }
}