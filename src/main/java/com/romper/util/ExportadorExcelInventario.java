package com.romper.util;

import com.romper.model.Ingrediente;
import com.romper.model.Inventario;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

public class ExportadorExcelInventario {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<Inventario> inventario;

    public ExportadorExcelInventario(List<Inventario> inventarios) {
        this.inventario = inventarios;
        workbook = new XSSFWorkbook();
    }

    // Method to write the header row in the Excel sheet
    private void escribirCabecera() {
        sheet = workbook.createSheet("Inventario");
        Row fila = sheet.createRow(0);
        CellStyle estilo = crearEstiloCabecera();

        crearCelda(fila, 0, "Ingrediente", estilo);
        crearCelda(fila, 1, "Fecha", estilo);
        crearCelda(fila, 2, "Tipo", estilo);
        crearCelda(fila, 3, "Cantidad", estilo);
        crearCelda(fila, 4, "Total", estilo);
    }

    // Method to create cells in a row with a specific style
    private void crearCelda(Row fila, int numeroColumna, Object valor, CellStyle estilo) {
        sheet.autoSizeColumn(numeroColumna);
        Cell celda = fila.createCell(numeroColumna);

        if (valor instanceof Integer) {
            celda.setCellValue((Integer) valor);
        } else if (valor instanceof Boolean) {
            celda.setCellValue((Boolean) valor);
        } else if (valor instanceof Double) {
            celda.setCellValue((Double) valor);
        } else if (valor instanceof BigDecimal) {
            celda.setCellValue(((BigDecimal) valor).doubleValue());
        } else if (valor instanceof String) {
            celda.setCellValue((String) valor);
        } else if (valor instanceof java.util.Date) {
            celda.setCellValue(new SimpleDateFormat("dd-MM-yyyy HH:mm").format((java.util.Date) valor));
        } else {
            celda.setCellValue(valor.toString());
        }

        celda.setCellStyle(estilo);
    }

    // Method to write the content rows in the Excel sheet
    private void escribirContenido() {
        int contadorFilas = 1;
        CellStyle estilo = crearEstiloContenido();

        for (Inventario item : inventario) {
            Row fila = sheet.createRow(contadorFilas++);
            int contadorColumna = 0;
            crearCelda(fila, contadorColumna++, item.getIngrediente().getNombre(), estilo);
            crearCelda(fila, contadorColumna++, item.getFechaMovimiento(), estilo);
            crearCelda(fila, contadorColumna++, item.getTipo(), estilo);
            crearCelda(fila, contadorColumna++, item.getCantidad(), estilo);
            crearCelda(fila, contadorColumna++, item.getTotal(), estilo);
        }
    }

    // Method to create the header style
    private CellStyle crearEstiloCabecera() {
        CellStyle estilo = workbook.createCellStyle();
        XSSFFont fuente = workbook.createFont();
        fuente.setBold(true);
        fuente.setFontHeight(15);
        estilo.setFont(fuente);
        return estilo;
    }

    // Method to create the content style
    private CellStyle crearEstiloContenido() {
        CellStyle estilo = workbook.createCellStyle();
        XSSFFont fuente = workbook.createFont();
        fuente.setBold(false);
        fuente.setFontHeight(12);
        estilo.setFont(fuente);
        return estilo;
    }

    // Method to export the Excel file
    public void exportar(HttpServletResponse response) throws IOException {
        escribirCabecera();
        escribirContenido();

        try (ServletOutputStream servletOutputStream = response.getOutputStream()) {
            workbook.write(servletOutputStream);
            workbook.close();
        } catch (IOException e) {
            throw new IOException("Error al exportar el archivo Excel", e);
        }
    }
}
