package com.Backend.MediConnect.clinica;

import java.time.LocalDate;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.Backend.MediConnect.clinica.domain.repository.AdminLocalRepository;
import com.Backend.MediConnect.clinica.domain.repository.AdminTotalRepository;
import com.Backend.MediConnect.clinica.domain.repository.EspecialidadRepository;
import com.Backend.MediConnect.clinica.domain.repository.HistoriaClinicaRepository;
import com.Backend.MediConnect.clinica.domain.repository.MedicoRepository;
import com.Backend.MediConnect.clinica.domain.repository.PacienteRepository;
import com.Backend.MediConnect.clinica.domain.repository.SedeRepository;
import com.Backend.MediConnect.clinica.domain.repository.UsuarioRepository;
import com.Backend.MediConnect.clinica.persistance.entity.AdministadorLocal;
import com.Backend.MediConnect.clinica.persistance.entity.AdministradorTotal;
import com.Backend.MediConnect.clinica.persistance.entity.Especialidad;
import com.Backend.MediConnect.clinica.persistance.entity.HistoriaClinica;
import com.Backend.MediConnect.clinica.persistance.entity.Medico;
import com.Backend.MediConnect.clinica.persistance.entity.Paciente;
import com.Backend.MediConnect.clinica.persistance.entity.Sede;
import com.Backend.MediConnect.clinica.persistance.entity.Usuario;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepo;
    private final PacienteRepository pacienteRepo;
    private final MedicoRepository medicoRepo;
    private final AdminLocalRepository adminLocalRepo;
    private final AdminTotalRepository adminTotalRepo;
    private final SedeRepository sedeRepo;
    private final EspecialidadRepository especialidadRepo;
    private final HistoriaClinicaRepository historiaClinicaRepo;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepo,
                           PacienteRepository pacienteRepo,
                           MedicoRepository medicoRepo,
                           AdminLocalRepository adminLocalRepo,
                           AdminTotalRepository adminTotalRepo,
                           SedeRepository sedeRepo,
                           EspecialidadRepository especialidadRepo,
                           HistoriaClinicaRepository historiaClinicaRepo,
                           PasswordEncoder passwordEncoder) {
        this.usuarioRepo = usuarioRepo;
        this.pacienteRepo = pacienteRepo;
        this.medicoRepo = medicoRepo;
        this.adminLocalRepo = adminLocalRepo;
        this.adminTotalRepo = adminTotalRepo;
        this.sedeRepo = sedeRepo;
        this.especialidadRepo = especialidadRepo;
        this.historiaClinicaRepo = historiaClinicaRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        cargarSedes();
        cargarEspecialidades();
        cargarAdminTotal();
        cargarAdminLocal();
        cargarMedicos();
        cargarPacientes();
    }

    private void cargarSedes() {
        if (sedeRepo.count() > 0)
            return;

        Sede sede1 = new Sede();
        sede1.setNombreSede("Sede Comas");
        sede1.setUbicacion("Av. Túpac Amaru 5421, Comas, Lima");
        sedeRepo.save(sede1);

        Sede sede2 = new Sede();
        sede2.setNombreSede("Sede Independencia");
        sede2.setUbicacion("Av. Carlos Izaguirre 125, Independencia, Lima");
        sedeRepo.save(sede2);

        Sede sede3 = new Sede();
        sede3.setNombreSede("Sede Puente Piedra");
        sede3.setUbicacion("Av. Sáenz Peña 310, Puente Piedra, Lima");
        sedeRepo.save(sede3);
    }

    private void cargarEspecialidades() {
        if (especialidadRepo.count() > 0)
            return;

        String[] nombres = {
                "Cardiología", "Neurología", "Pediatría", "Ginecología",
                "Traumatología", "Dermatología", "Oftalmología", "Psiquiatría",
                "Oncología", "Medicina General"
        };

        for (String nombre : nombres) {
            Especialidad e = new Especialidad();
            e.setNombreEspecialidad(nombre);
            especialidadRepo.save(e);
        }
    }

    private void cargarAdminTotal() {
        if (usuarioRepo.existsByDni("00000001") || adminTotalRepo.existsByDni("00000001"))
            return;

        AdministradorTotal admin = new AdministradorTotal();
        admin.setPrimerNombre("Carlos");
        admin.setSegundoNombre("Alberto");
        admin.setPrimerApellido("Ramirez");
        admin.setSegundoApellido("Torres");
        admin.setDni("00000001");
        admin.setEstado("ACTIVO");
        adminTotalRepo.save(admin);

        Usuario usuario = new Usuario();
        usuario.setDni("00000001");
        usuario.setPassword(passwordEncoder.encode("admin123"));
        usuario.setRol("ADMIN_TOTAL");
        usuario.setActivo(true);
        usuarioRepo.save(usuario);
    }

    private void cargarAdminLocal() {
        if (usuarioRepo.existsByDni("00000002") || adminLocalRepo.existsByDni("00000002"))
            return;

        Sede sede = sedeRepo.findAll().get(0);

        AdministadorLocal admin = new AdministadorLocal();
        admin.setPrimerNombre("Maria");
        admin.setSegundoNombre("Elena");
        admin.setPrimerApellido("Lopez");
        admin.setSegundoApellido("Garcia");
        admin.setDni("00000002");
        admin.setEstado("ACTIVO");
        admin.setSede(sede);
        adminLocalRepo.save(admin);

        Usuario usuario = new Usuario();
        usuario.setDni("00000002");
        usuario.setPassword(passwordEncoder.encode("local123"));
        usuario.setRol("ADMIN_LOCAL");
        usuario.setActivo(true);
        usuarioRepo.save(usuario);
    }

    private void cargarMedicos() {
        if (usuarioRepo.existsByDni("00000003") || medicoRepo.existsByDni("00000003"))
            return;

        Sede sede = sedeRepo.findAll().get(0);
        List<Especialidad> especialidades = especialidadRepo.findAllById(List.of(1, 10));

        Medico medico = new Medico();
        medico.setPrimerNombre("Jorge");
        medico.setSegundoNombre("Luis");
        medico.setPrimerApellido("Mendoza");
        medico.setSegundoApellido("Vega");
        medico.setDni("00000003");
        medico.setEdad(45);
        medico.setDisponible(true);
        medico.setEspecialidades(especialidades);
        medico.setSedes(List.of(sede));
        medicoRepo.save(medico);

        Usuario usuario = new Usuario();
        usuario.setDni("00000003");
        usuario.setPassword(passwordEncoder.encode("medico123"));
        usuario.setRol("MEDICO");
        usuario.setActivo(true);
        usuarioRepo.save(usuario);
    }

    private void cargarPacientes() {
        if (usuarioRepo.existsByDni("00000004") || pacienteRepo.existsByDni("00000004"))
            return;

        Paciente paciente = new Paciente();
        paciente.setPrimerNombre("Ana");
        paciente.setSegundoNombre("Sofia");
        paciente.setPrimerApellido("Gutierrez");
        paciente.setSegundoApellido("Rios");
        paciente.setDni("00000004");
        paciente.setCorreo("ana.gutierrez@gmail.com");
        paciente.setTelefono("987654321");
        paciente.setFechaNacimiento(LocalDate.of(1995, 3, 15));
        paciente.setUbigeo("150101");
        paciente = pacienteRepo.save(paciente);

        HistoriaClinica historia = new HistoriaClinica();
        historia.setPaciente(paciente);
        historia.setFecha(LocalDate.now());
        historia.setMotivoIngreso("Registro inicial de paciente");
        historia.setHistoriaEnfermedadActual("");
        historia.setEnfermedadesPasadas("");
        historia.setCodigoUnico("HCL-00000004-TEST");
        historiaClinicaRepo.save(historia);

        Usuario usuario = new Usuario();
        usuario.setDni("00000004");
        usuario.setPassword(passwordEncoder.encode("paciente123"));
        usuario.setRol("PACIENTE");
        usuario.setActivo(true);
        usuarioRepo.save(usuario);
    }
}