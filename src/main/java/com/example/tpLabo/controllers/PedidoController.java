package com.example.tpLabo.controllers;

import com.example.tpLabo.entities.Pedido;
import com.example.tpLabo.services.ChartsGoogle;
import com.example.tpLabo.services.ExcelManager;
import com.example.tpLabo.services.InstrumentoPDF;
import com.example.tpLabo.services.PedidoService;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@RestController
@CrossOrigin(origins = "*")
public class PedidoController {

    private final PedidoService pedidoService;

    @Autowired
    private ChartsGoogle chartGoogle;

    @Autowired
    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping("/api/pedidos")
    public ResponseEntity<Pedido> createPedido(@RequestBody Pedido pedido) {
        Pedido savedPedido = pedidoService.save(pedido);
        return new ResponseEntity<>(savedPedido, HttpStatus.CREATED);
    }

    @GetMapping("/api/pedidos")
    public List<Pedido> getPedidos() {
        return pedidoService.findAll();
    }

    @GetMapping("/api/pedidos/barchart")
    public List<List<Object>> getBarChartData() {
        List<List<Object>> data = new ArrayList<>();
        data.add(Arrays.asList("Mes/Año", "Cantidad de Pedidos"));

        List<Map<String, Object>> datos = chartGoogle.getDatosChartBar();
        for (Map<String, Object> row : datos) {
            data.add(Arrays.asList(row.get("mes_anio"), row.get("cantidad_pedidos")));
        }
        return data;
    }

    @GetMapping("/api/pedidos/piechart")
    public List<List<Object>> getPieChartData() {
        List<List<Object>> data = new ArrayList<>();
        data.add(Arrays.asList("Instrumento", "Cantidad de Pedidos"));

        List<Map<String, Object>> datos = chartGoogle.getDatosChartPie();
        for (Map<String, Object> row : datos) {
            data.add(Arrays.asList(row.get("instrumento"), row.get("cantidad")));
        }
        return data;
    }

    @GetMapping("api/pedidos/downloadExcel")
    public ResponseEntity<byte[]> downloadExcelPedidos(
            @RequestParam("fechaDesde") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaDesde,
            @RequestParam("fechaHasta") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaHasta) throws SQLException {
        try {
            ExcelManager mPrintPedido = new ExcelManager();
            SXSSFWorkbook libroExcel = mPrintPedido.imprimirExcelPedidos(fechaDesde, fechaHasta);
            // Escribir el libro de trabajo en un flujo de bytes
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            libroExcel.write(outputStream);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", "datos.xlsx");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("api/pedidos/downloadPdf/{idInstrumento}")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable String idInstrumento) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            InstrumentoPDF mPrintInstrumento = new InstrumentoPDF();
            // Crear un nuevo documento
            mPrintInstrumento.imprimirInstrumentoPdf(Long.parseLong(idInstrumento), outputStream);

            // Establecer las cabeceras de la respuesta
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/pdf"));
            headers.setContentDispositionFormData("attachment", "documento.pdf");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            // Devolver el archivo PDF como parte de la respuesta HTTP
            return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
