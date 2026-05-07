package br.com.evelyn.calorias.repository;

import br.com.evelyn.calorias.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
