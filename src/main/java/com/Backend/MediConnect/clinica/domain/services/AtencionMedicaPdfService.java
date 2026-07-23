package com.Backend.MediConnect.clinica.domain.services;

import com.Backend.MediConnect.clinica.domain.exception.BusinessException;
import com.Backend.MediConnect.clinica.persistance.entity.AtencionMedica;
import com.Backend.MediConnect.clinica.persistance.entity.Diagnostico;
import com.Backend.MediConnect.clinica.persistance.entity.Tratamiento;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AtencionMedicaPdfService {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public byte[] generarPdf(AtencionMedica atencion, List<Diagnostico> diagnosticos, List<Tratamiento> tratamientos) {
        try {
            ByteArrayOutputStream salida = new ByteArrayOutputStream();
            Document documento = new Document(PageSize.A4, 40, 40, 40, 40);
            PdfWriter.getInstance(documento, salida);
            documento.open();

            Font fuenteTitulo = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, new BaseColor(37, 99, 235));
            Font fuenteSubtitulo = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.GRAY);
            Font fuenteEtiqueta = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
            Font fuenteTexto = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
            Font fuenteSeccion = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, new BaseColor(37, 99, 235));

            Paragraph titulo = new Paragraph("MediConnect", fuenteTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            documento.add(titulo);

            Paragraph subtitulo = new Paragraph("Constancia de Atención Médica", fuenteSubtitulo);
            subtitulo.setAlignment(Element.ALIGN_CENTER);
            subtitulo.setSpacingAfter(20);
            documento.add(subtitulo);

            documento.add(crearLineaDato("Fecha de atención:", atencion.getFechaAtencion().format(FORMATO_FECHA), fuenteEtiqueta, fuenteTexto));
            documento.add(crearLineaDato("Paciente:", construirNombre(
                    atencion.getCita().getPaciente().getPersona().getNombres(),
                    atencion.getCita().getPaciente().getPersona().getApellidoPaterno(),
                    atencion.getCita().getPaciente().getPersona().getApellidoMaterno()), fuenteEtiqueta, fuenteTexto));
            documento.add(crearLineaDato("Médico:", construirNombre(
                    atencion.getCita().getMedico().getPersona().getNombres(),
                    atencion.getCita().getMedico().getPersona().getApellidoPaterno(),
                    atencion.getCita().getMedico().getPersona().getApellidoMaterno()), fuenteEtiqueta, fuenteTexto));
            documento.add(crearLineaDato("Especialidad:", atencion.getCita().getMedico().getEspecialidad().getNombre(), fuenteEtiqueta, fuenteTexto));
            documento.add(crearLineaDato("Motivo de consulta:", atencion.getMotivoConsulta(), fuenteEtiqueta, fuenteTexto));

            documento.add(new Paragraph(" "));

            Paragraph seccionDiagnosticos = new Paragraph("Diagnósticos", fuenteSeccion);
            seccionDiagnosticos.setSpacingAfter(8);
            documento.add(seccionDiagnosticos);

            for (Diagnostico diagnostico : diagnosticos) {
                String texto = (diagnostico.getCodigoCie10() != null ? diagnostico.getCodigoCie10() + " - " : "") + diagnostico.getDescripcion();
                Paragraph item = new Paragraph("• " + texto, fuenteTexto);
                item.setSpacingAfter(4);
                documento.add(item);
            }

            documento.add(new Paragraph(" "));

            Paragraph seccionTratamientos = new Paragraph("Tratamientos", fuenteSeccion);
            seccionTratamientos.setSpacingAfter(8);
            documento.add(seccionTratamientos);

            for (Tratamiento tratamiento : tratamientos) {
                Paragraph item = new Paragraph("• " + tratamiento.getIndicaciones(), fuenteTexto);
                item.setSpacingAfter(4);
                documento.add(item);
            }

            if (atencion.getObservaciones() != null && !atencion.getObservaciones().isBlank()) {
                Paragraph observaciones = new Paragraph();
                observaciones.setSpacingBefore(16);
                observaciones.add(new Chunk("Observaciones: ", fuenteEtiqueta));
                observaciones.add(new Chunk(atencion.getObservaciones(), fuenteTexto));
                documento.add(observaciones);
            }

            documento.close();
            return salida.toByteArray();
        } catch (DocumentException e) {
            throw new BusinessException("Error al generar el PDF de la constancia de atención.");
        }
    }

    private Paragraph crearLineaDato(String etiqueta, String valor, Font fuenteEtiqueta, Font fuenteTexto) {
        Paragraph parrafo = new Paragraph();
        parrafo.add(new Chunk(etiqueta + " ", fuenteEtiqueta));
        parrafo.add(new Chunk(valor != null ? valor : "-", fuenteTexto));
        parrafo.setSpacingAfter(6);
        return parrafo;
    }

    private String construirNombre(String nombres, String apellidoPaterno, String apellidoMaterno) {
        return String.join(" ",
                nombres != null ? nombres : "",
                apellidoPaterno != null ? apellidoPaterno : "",
                apellidoMaterno != null ? apellidoMaterno : "").trim();
    }
}