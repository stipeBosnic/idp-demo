package com.example.backendexampleapp.repository;

import com.example.backendexampleapp.model.UnprotectedData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnprotectedDataRepository extends JpaRepository<UnprotectedData, Long> {
}
