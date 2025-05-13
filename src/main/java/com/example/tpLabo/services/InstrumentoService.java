package com.example.tpLabo.services;

import com.example.tpLabo.entities.Instrumento;
import com.example.tpLabo.repositories.InstrumentoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InstrumentoService {

    private final InstrumentoRepository instrumentoRepository;

    @Autowired
    public InstrumentoService(InstrumentoRepository instrumentoRepository) {
        this.instrumentoRepository = instrumentoRepository;
    }

    public Instrumento findById(Integer id) {
        return instrumentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Instrumento no encontrado con id: " + id));
    }

    public List<Instrumento> findAll() {
        return instrumentoRepository.findAll();
    }

    public List<Instrumento> findAllDeleted() {
        return instrumentoRepository.findAll().stream()
                .filter(Instrumento::getIsDeleted)
                .collect(Collectors.toList());
    }

    public Instrumento save(Instrumento instrumento) {
        return instrumentoRepository.save(instrumento);
    }

    public Instrumento update(Integer id, Instrumento instrumento) {
        Instrumento existingInstrumento = findById(id);
        BeanUtils.copyProperties(instrumento, existingInstrumento, "id");
        return instrumentoRepository.save(existingInstrumento);
    }

    public void delete(Integer id) {
        instrumentoRepository.deleteById(id);
    }

    public List<Instrumento> findByCategoria(int idCategoria) {
        return instrumentoRepository.findByCategoriaId(idCategoria);
    }
}
