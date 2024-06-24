package com.romper.service;

import com.romper.dao.IPlatoDao;
import com.romper.dao.IVentaDao;
import com.romper.model.Plato;
import com.romper.model.Venta;
import com.romper.response.VentaResponseRest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VentaServiceImpl implements IVentaService {

    public static final String RESPUESTA_OK = "Respuesta ok";
    private final IVentaDao ventaDao;

    private final IPlatoDao platoDao;

    private static final Logger logger = LoggerFactory.getLogger(VentaServiceImpl.class);

    public VentaServiceImpl(IVentaDao ventaDao, IPlatoDao platoDao) {
        this.ventaDao = ventaDao;
        this.platoDao = platoDao;
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<VentaResponseRest> buscarTodos() {
        VentaResponseRest response = new VentaResponseRest();
        try{
            List<Venta> ventas = (List<Venta>) ventaDao.findAll();
            response.getVentaResponse().setVentas(ventas);
            response.setMetadata(RESPUESTA_OK, "00", "Respuesta exitosa");
        }
        catch (Exception e){
            logger.error("Error al consultar todas las ventas", e);
            response.setMetadata("Respuesta nok", "-1", "Error al consultar");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<VentaResponseRest> buscarPorId(Long id) {
        VentaResponseRest response = new VentaResponseRest();
        try {
            Venta venta = ventaDao.findById(id)
                    .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
            response.getVentaResponse().setVentas(List.of(venta));
            response.setMetadata(RESPUESTA_OK, "00", "Venta encontrada");
        } catch (RuntimeException e) {
            logger.error("Error al consultar la venta por id", e);
            response.setMetadata("Respuesta nok", "-1", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error al consultar la venta por id", e);
            response.setMetadata("Respuesta nok", "-1", "Error al consultar por id");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<VentaResponseRest> buscarPorFechas(LocalDateTime inicio, LocalDateTime fin) {

        VentaResponseRest response = new VentaResponseRest();
        try {
            List<Venta> ventas = ventaDao.findByFechaVentaBetween(inicio, fin);
            if (!ventas.isEmpty()) {
                response.getVentaResponse().setVentas(ventas);
                response.setMetadata(RESPUESTA_OK, "00", "Ventas encontradas en ese rango de fechas");
            } else {
                response.setMetadata("Respuesta nok", "-1", "No se encontraron ventas en ese rango de fechas");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("Error al consultar la venta en ese rango de fechas ", e);
            response.setMetadata("Respuesta nok", "-1", "Error al consultar por ese rango de fechas");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<VentaResponseRest> crear(Venta venta) {
        VentaResponseRest response = new VentaResponseRest();
        try{
            Plato plato = platoDao.findById(venta.getPlatoId())
                    .orElseThrow(() -> new RuntimeException("Plato no encontrado"));
            venta.setPlato(plato);
            venta.setFechaVenta(LocalDateTime.now());
            venta = validarTotal(venta);
            Venta ventaGuardada = ventaDao.save(venta);
            response.getVentaResponse().setVentas(List.of(ventaGuardada));
            response.setMetadata(RESPUESTA_OK, "00", "Venta guardada");
        }catch (RuntimeException e) {
            logger.error("Error al crear la venta", e);
            response.setMetadata("Respuesta nok", "-1", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error al crear la venta", e);
            response.setMetadata("Respuesta nok", "-1", "Error al crear venta");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    private Venta validarTotal(Venta venta) {
        venta.setPrecio(venta.getPlato().getPvp());
        BigDecimal total = venta.getPlato().getPvp().multiply(BigDecimal.valueOf(venta.getCantidad()));
        venta.setTotal(total);
        return venta;
    }
}