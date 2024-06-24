package com.romper.util;

import com.romper.model.Ingrediente;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class ExportadorExcelIngredientes {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<Ingrediente> ingrediente;
    public ExportadorExcelIngredientes(List<Ingrediente> ingredientes){
        this.ingrediente = ingredientes;
        workbook = new XSSFWorkbook();
    }

    private void escribirCabecera(){
        sheet = workbook.createSheet("Ingredientes");
        Row fila = sheet.createRow(0);
        CellStyle estilo = workbook.createCellStyle();

        XSSFFont fuente = workbook.createFont();
        fuente.setBold(true);
        fuente.setFontHeight(15);
        estilo.setFont(fuente);

        crearCelda(fila, 0, "Codigo", estilo);
        crearCelda(fila, 1, "Proveedor", estilo);
        crearCelda(fila, 2, "Nombre", estilo);
        crearCelda(fila, 3, "Cantidad", estilo);
        crearCelda(fila, 4, "Costo", estilo);

    }

    private void crearCelda(Row fila, int numeroColumna, Object valor, CellStyle estilo){
        sheet.autoSizeColumn(numeroColumna);
        Cell celda = fila.createCell(numeroColumna);

        if (valor instanceof Integer){
            celda.setCellValue((Integer) valor);
        } else if (valor instanceof Boolean){
            celda.setCellValue((Boolean) valor);
        } else if (valor instanceof Double){
            celda.setCellValue((Double) valor);
        } else if (valor instanceof BigDecimal){
            celda.setCellValue(((BigDecimal) valor).longValue());
        } else {
            celda.setCellValue((String) valor);
        }

        celda.setCellStyle(estilo);
    }

    private void escribirContenido(){
        int contadorFilas = 1;
        CellStyle estilo = workbook.createCellStyle();
        XSSFFont fuente = workbook.createFont();
        fuente.setBold(false);
        fuente.setFontHeight(12);
        estilo.setFont(fuente);

        for (Ingrediente item: ingrediente) {
            Row fila = sheet.createRow(contadorFilas++);
            int contadorColumna = 0;
            crearCelda(fila, contadorColumna++, String.valueOf(item.getId()), estilo);
            crearCelda(fila, contadorColumna++, item.getProveedor().getNombreProveedor(), estilo);
            crearCelda(fila, contadorColumna++, item.getNombre(), estilo);
            crearCelda(fila, contadorColumna++, item.getCantidad(), estilo);
            crearCelda(fila, contadorColumna++, item.getCosto(), estilo);
        }
    }

    public void exportar(HttpServletResponse response) throws IOException {
        escribirCabecera();
        escribirContenido();

        ServletOutputStream servletOutputStream = response.getOutputStream();
        workbook.write(servletOutputStream);
        workbook.close();

        servletOutputStream.close();
    }
}
