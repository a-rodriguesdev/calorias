package br.com.evelyn.calorias.repository;

import br.com.evelyn.calorias.model.Alimento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlimentoRepository extends JpaRepository<Alimento, Long> {


}
