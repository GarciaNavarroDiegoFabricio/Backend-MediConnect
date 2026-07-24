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

    public byte[] generarPdf(
            Receta receta,
            List<DetalleReceta> detalles) {

        try {

            ByteArrayOutputStream salida = new ByteArrayOutputStream();

            Document documento = new Document(PageSize.A4, 40, 40, 40, 40);

            PdfWriter.getInstance(documento, salida);

            documento.open();

            Font titulo = new Font(
                    Font.FontFamily.HELVETICA,
                    18,
                    Font.BOLD);

            Font texto = new Font(
                    Font.FontFamily.HELVETICA,
                    10);

            Paragraph encabezado = new Paragraph(
                    "MediConnect - Receta Médica",
                    titulo);

            encabezado.setAlignment(Element.ALIGN_CENTER);

            documento.add(encabezado);

            documento.add(new Paragraph(" "));

            documento.add(
                    new Paragraph(
                            "Fecha: "
                                    + receta.getFecha()
                                            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                            texto));

            documento.add(
                    new Paragraph(
                            "Paciente: "
                                    + construirNombre(
                                            receta.getPaciente()
                                                    .getPersona()
                                                    .getNombres(),

                                            receta.getPaciente()
                                                    .getPersona()
                                                    .getApellidoPaterno(),

                                            receta.getPaciente()
                                                    .getPersona()
                                                    .getApellidoMaterno()),
                            texto));

            documento.add(
                    new Paragraph(
                            "Médico: "
                                    + construirNombre(
                                            receta.getMedico()
                                                    .getPersona()
                                                    .getNombres(),

                                            receta.getMedico()
                                                    .getPersona()
                                                    .getApellidoPaterno(),

                                            receta.getMedico()
                                                    .getPersona()
                                                    .getApellidoMaterno()),
                            texto));

            documento.add(new Paragraph(" "));

            PdfPTable tabla = new PdfPTable(5);

            tabla.setWidthPercentage(100);

            tabla.addCell("Medicamento");
            tabla.addCell("Dosis");
            tabla.addCell("Frecuencia");
            tabla.addCell("Duración");
            tabla.addCell("Indicaciones");

            for (DetalleReceta detalle : detalles) {

                tabla.addCell(detalle.getMedicamento());
                tabla.addCell(detalle.getDosis());
                tabla.addCell(detalle.getFrecuencia());

                tabla.addCell(
                        detalle.getDuracion() != null
                                ? detalle.getDuracion()
                                : "-");

                tabla.addCell(
                        detalle.getIndicaciones() != null
                                ? detalle.getIndicaciones()
                                : "-");
            }

            documento.add(tabla);

            if (receta.getPrescripcion() != null) {

                documento.add(new Paragraph(" "));

                documento.add(
                        new Paragraph(
                                "Prescripción: "
                                        + receta.getPrescripcion(),
                                texto));
            }

            documento.close();

            return salida.toByteArray();

        } catch (DocumentException e) {

            throw new BusinessException(
                    "Error al generar PDF de receta.");
        }

    }

    private String construirNombre(
            String nombres,
            String apellidoPaterno,
            String apellidoMaterno) {

        return String.join(" ",
                nombres != null ? nombres : "",
                apellidoPaterno != null ? apellidoPaterno : "",
                apellidoMaterno != null ? apellidoMaterno : "")
                .trim();
    }
}