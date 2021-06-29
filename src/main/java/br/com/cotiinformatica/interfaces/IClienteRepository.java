package br.com.cotiinformatica.interfaces;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import br.com.cotiinformatica.entities.Cliente;

public interface IClienteRepository extends CrudRepository<Cliente, Integer>{

	@Query("from Cliente c where c.cpf = :param") //JPQL
	Cliente findByCpf(@Param("param") String cpf);
}
