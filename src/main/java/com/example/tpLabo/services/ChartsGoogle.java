package com.example.tpLabo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class ChartsGoogle
{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> getDatosChartBar() {
        String sql = "SELECT DATE_FORMAT(p.fecha_pedido, '%Y-%m') AS mes_anio, COUNT(p.id) AS cantidad_pedidos " +
                "FROM pedido p " +
                "GROUP BY mes_anio " +
                "ORDER BY mes_anio";

        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> getDatosChartPie() {
        String sql = "SELECT i.producto AS producto, COUNT(dp.id) AS cantidad " +
                "FROM pedido_detalle dp " +
                "JOIN producto i ON dp.id_producto = i.id " +
                "GROUP BY i.producto " +
                "ORDER BY cantidad DESC";

        return jdbcTemplate.queryForList(sql);
    }
}
