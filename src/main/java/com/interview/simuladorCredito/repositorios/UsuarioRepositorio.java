package com.interview.simuladorCredito.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.interview.simuladorCredito.modelo.Usuario;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCpf(String cpf);
    Optional<Usuario> findByEmail(String email);
    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);
} 