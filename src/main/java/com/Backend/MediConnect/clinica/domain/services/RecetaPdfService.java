package com.Backend.MediConnect.clinica.domain.services;

import com.Backend.MediConnect.clinica.domain.exception.BusinessException;
import com.Backend.MediConnect.clinica.persistance.entity.DetalleReceta;
import com.Backend.MediConnect.clinica.persistance.entity.Receta;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class RecetaPdfService {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public byte[] generarPdf(Receta receta, List<DetalleReceta> detalles) {
        try {
            ByteArrayOutputStream salida = new ByteArrayOutputStream();
            Document documento = new Document(PageSize.A4, 40, 40, 40, 40);
            PdfWriter.getInstance(documento, salida);
            documento.open();

            Font fuenteTitulo = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, new BaseColor(37, 99, 235));
            Font fuenteSubtitulo = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.GRAY);
            Font fuenteEtiqueta = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
            Font fuenteTexto = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);

            Paragraph titulo = new Paragraph("MediConnect", fuenteTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            documento.add(titulo);

            Paragraph subtitulo = new Paragraph("Receta Médica", fuenteSubtitulo);
            subtitulo.setAlignment(Element.ALIGN_CENTER);
            subtitulo.setSpacingAfter(20);
            documento.add(subtitulo);

            documento.add(crearLineaDato("Código de receta:", receta.getCodigoReceta(), fuenteEtiqueta, fuenteTexto));
            documento.add(crearLineaDato("Fecha de emisión:", receta.getFechaEmision().format(FORMATO_FECHA), fuenteEtiqueta, fuenteTexto));
            documento.add(crearLineaDato("Paciente:", construirNombre(
                    receta.getAtencionMedica().getCita().getPaciente().getPersona().getNombres(),
                    receta.getAtencionMedica().getCita().getPaciente().getPersona().getApellidoPaterno(),
                    receta.getAtencionMedica().getCita().getPaciente().getPersona().getApellidoMaterno()), fuenteEtiqueta, fuenteTexto));
            documento.add(crearLineaDato("Médico:", construirNombre(
                    receta.getAtencionMedica().getCita().getMedico().getPersona().getNombres(),
                    receta.getAtencionMedica().getCita().getMedico().getPersona().getApellidoPaterno(),
                    receta.getAtencionMedica().getCita().getMedico().getPersona().getApellidoMaterno()), fuenteEtiqueta, fuenteTexto));
            documento.add(crearLineaDato("Especialidad:", receta.getAtencionMedica().getCita().getMedico().getEspecialidad().getNombre(), fuenteEtiqueta, fuenteTexto));

            documento.add(new Paragraph(" "));

            PdfPTable tabla = new PdfPTable(4);
            tabla.setWidthPercentage(100);
            tabla.setWidths(new float[]{3, 2, 2, 2});

            agregarCeldaEncabezado(tabla, "Medicamento");
            agregarCeldaEncabezado(tabla, "Dosis");
            agregarCeldaEncabezado(tabla, "Frecuencia");
            agregarCeldaEncabezado(tabla, "Duración");

            for (DetalleReceta detalle : detalles) {
                tabla.addCell(new PdfPCell(new Phrase(detalle.getMedicamento(), fuenteTexto)));
                tabla.addCell(new PdfPCell(new Phrase(detalle.getDosis(), fuenteTexto)));
                tabla.addCell(new PdfPCell(new Phrase(detalle.getFrecuencia(), fuenteTexto)));
                tabla.addCell(new PdfPCell(new Phrase(detalle.getDuracion() != null ? detalle.getDuracion() : "-", fuenteTexto)));
            }

            documento.add(tabla);

            if (receta.getObservaciones() != null && !receta.getObservaciones().isBlank()) {
                Paragraph observaciones = new Paragraph();
                observaciones.setSpacingBefore(20);
                observaciones.add(new Chunk("Observaciones: ", fuenteEtiqueta));
                observaciones.add(new Chunk(receta.getObservaciones(), fuenteTexto));
                documento.add(observaciones);
            }

            documento.close();
            return salida.toByteArray();
        } catch (DocumentException e) {
            throw new BusinessException("Error al generar el PDF de la receta.");
        }
    }

    private Paragraph crearLineaDato(String etiqueta, String valor, Font fuenteEtiqueta, Font fuenteTexto) {
        Paragraph parrafo = new Paragraph();
        parrafo.add(new Chunk(etiqueta + " ", fuenteEtiqueta));
        parrafo.add(new Chunk(valor != null ? valor : "-", fuenteTexto));
        parrafo.setSpacingAfter(6);
        return parrafo;
    }

    private void agregarCeldaEncabezado(PdfPTable tabla, String texto) {
        Font fuente = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);
        PdfPCell celda = new PdfPCell(new Phrase(texto, fuente));
        celda.setBackgroundColor(new BaseColor(37, 99, 235));
        celda.setPadding(6);
        tabla.addCell(celda);
    }

    private String construirNombre(String nombres, String apellidoPaterno, String apellidoMaterno) {
        return String.join(" ",
                nombres != null ? nombres : "",
                apellidoPaterno != null ? apellidoPaterno : "",
                apellidoMaterno != null ? apellidoMaterno : "").trim();
    }
}