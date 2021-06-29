package br.com.cotiinformatica.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.cotiinformatica.dtos.ClienteGetDTO;
import br.com.cotiinformatica.dtos.ClientePostDTO;
import br.com.cotiinformatica.dtos.ClientePutDTO;
import br.com.cotiinformatica.entities.Cliente;
import br.com.cotiinformatica.interfaces.IClienteRepository;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@Controller
@Transactional
public class ClienteController  {
	private static final String ENDPOINT = "api/clientes";
	@Autowired
	private IClienteRepository clienteRepository;

	@RequestMapping(value = ENDPOINT, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> post(@RequestBody ClientePostDTO dto) {

		try {

			Cliente cliente = new Cliente();
			cliente.setNome(dto.getNome());
			cliente.setCpf(dto.getCpf());
			cliente.setEmail(dto.getEmail());
			if (clienteRepository.findByCpf(dto.getCpf()) != null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Este CPF já está cadastrado!");
			}
			clienteRepository.save(cliente);
			return ResponseEntity.status(HttpStatus.OK)
					.body("Cliente " + cliente.getNome() + " Cadastrado com sucesso!");

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro: " + e.getMessage());
		}
	}

	@RequestMapping(value = ENDPOINT, method = RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<String> put(@RequestBody ClientePutDTO dto) {

		try {
			Optional<Cliente> result = clienteRepository.findById(dto.getIdCliente());

			if (result.isEmpty() || result == null) {
				return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Cliente não encontrado!");

			}
			Cliente cliente = result.get();
			cliente.setNome(dto.getNome());
			cliente.setEmail(dto.getEmail());
			clienteRepository.save(cliente);
			return ResponseEntity.status(HttpStatus.OK)
					.body("Cliente " + cliente.getNome() + " Atualizado com sucesso!");

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro: " + e.getMessage());
		}

	}

	@RequestMapping(value = ENDPOINT + "/{idcliente}", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<String> delete(@PathVariable("idcliente") Integer idcliente) {
		try {
			Optional<Cliente> result = clienteRepository.findById(idcliente);

			if (result.isEmpty() || result == null) {
				return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Cliente não encontrado!");

			}
			Cliente cliente = result.get();
			clienteRepository.delete(cliente);
			return ResponseEntity.status(HttpStatus.OK).body("Exclusão de Cliente executado com sucesso!");

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro: " + e.getMessage());
		}

	}

	@RequestMapping(value = ENDPOINT, method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<ClienteGetDTO>> get() {
		
		try {
			List<ClienteGetDTO> result = new ArrayList<ClienteGetDTO>();
			for (Cliente cliente: clienteRepository.findAll()) {
				ClienteGetDTO dto = new ClienteGetDTO();
				dto.setIdCliente(cliente.getIdCliente());
				dto.setNome(cliente.getNome());
				dto.setCpf(cliente.getCpf());
				dto.setEmail(cliente.getEmail());
				result.add(dto);
			}
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(result);
		} catch (Exception e) {
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(null);
		}
		
	}
}
