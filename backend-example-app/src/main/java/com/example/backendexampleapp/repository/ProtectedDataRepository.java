package com.example.backendexampleapp.repository;

import com.example.backendexampleapp.model.ProtectedData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProtectedDataRepository extends JpaRepository<ProtectedData, String> {
}
