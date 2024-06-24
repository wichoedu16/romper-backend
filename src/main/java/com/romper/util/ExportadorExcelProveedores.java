package com.romper.util;

import com.romper.model.Proveedor;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.List;

public class ExportadorExcelProveedores {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<Proveedor> proveedor;
    public ExportadorExcelProveedores(List<Proveedor> proveedores){
        this.proveedor = proveedores;
        workbook = new XSSFWorkbook();
    }

    private void escribirCabecera(){
        sheet = workbook.createSheet("Proveedores");
        Row fila = sheet.createRow(0);
        CellStyle estilo = workbook.createCellStyle();

        XSSFFont fuente = workbook.createFont();
        fuente.setBold(true);
        fuente.setFontHeight(15);
        estilo.setFont(fuente);

        crearCelda(fila, 0, "Codigo", estilo);
        crearCelda(fila, 1, "NombreProveedor", estilo);
        crearCelda(fila, 2, "NumeroContacto", estilo);


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

        for (Proveedor item: proveedor) {
            Row fila = sheet.createRow(contadorFilas++);
            int contadorColumna = 0;
            crearCelda(fila, contadorColumna++, String.valueOf(item.getId()), estilo);
            crearCelda(fila, contadorColumna++, item.getNombreProveedor(), estilo);
            crearCelda(fila, contadorColumna++, item.getTelefonoProveedor(), estilo);
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
