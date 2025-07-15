package com.isai.demowebregistrationsystem.services.impl;

import com.isai.demowebregistrationsystem.model.dtos.DashboardSummaryDTO;
import com.isai.demowebregistrationsystem.repositorys.DocenteRepository;
import com.isai.demowebregistrationsystem.repositorys.EstudianteRepository;
import com.isai.demowebregistrationsystem.repositorys.UsuarioRepository;
import com.isai.demowebregistrationsystem.repositorys.ApoderadoRepository;
import com.isai.demowebregistrationsystem.services.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final UsuarioRepository usuarioRepository;
    private final DocenteRepository docenteRepository;
    private final EstudianteRepository estudianteRepository;
    private final ApoderadoRepository apoderadoRepository;

    @Override
    @Transactional(readOnly = true)
    public DashboardSummaryDTO getDashboardSummary() {
        DashboardSummaryDTO summary = new DashboardSummaryDTO();

        summary.setTotalUsuarios(usuarioRepository.count());
        summary.setTotalDocentes(docenteRepository.count());
        summary.setTotalEstudiantes(estudianteRepository.count());
        summary.setTotalApoderados(apoderadoRepository.count());

        return summary;
    }
}