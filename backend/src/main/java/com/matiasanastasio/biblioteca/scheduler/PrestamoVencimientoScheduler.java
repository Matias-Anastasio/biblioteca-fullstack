package com.matiasanastasio.biblioteca.scheduler;

import java.time.LocalDate;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.matiasanastasio.biblioteca.repository.PrestamoRepository;

import jakarta.transaction.Transactional;

@Component
public class PrestamoVencimientoScheduler {
    private final PrestamoRepository prestamoRepository;

    public PrestamoVencimientoScheduler(PrestamoRepository prestamoRepository){
        this.prestamoRepository = prestamoRepository;
    }

    @Scheduled(cron = "0 0 3 * * *") // todos los dias a las 3 am
    // @Scheduled(fixedDelay=30000) // cada 30 segundos -> para testing
    @Transactional
    public void marcarPrestamosVencidos(){
        prestamoRepository.marcarVencidos(LocalDate.now());
    }
}
