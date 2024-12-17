package com.example.attendance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.attendance.model.Institute;
import com.example.attendance.repository.InstituteRepository;

@Service
public class InstituteService {
    
    @Autowired
    private InstituteRepository instituteRepository;

    public long getInstituteId(String instituteName){
        Institute institute = instituteRepository.findByInstituteName(instituteName);
        return(institute != null) ? institute.getId() : null;
    }
}
