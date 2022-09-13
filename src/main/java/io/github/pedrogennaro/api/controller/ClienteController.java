package io.github.pedrogennaro.api.controller;

import io.github.pedrogennaro.domain.entity.Cliente;
import io.github.pedrogennaro.domain.repository.Clientes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes")
@Api("Api de Clientes")
public class ClienteController {

    private Clientes clientesRepository;

    public ClienteController(Clientes clientesRepository) {
        this.clientesRepository = clientesRepository;
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de um cliente")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Cliente encontrado"),
            @ApiResponse(code = 404, message = "Cliente não encontrado para o Id informado")
    })
    public Cliente getClienteById(@PathVariable Integer id){

        return clientesRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));

        /*Optional<Cliente> cliente = this.clientesRepository.findById(id);
        if(cliente.isPresent()){
            return cliente.get();
        }*/

        //throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado");
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Salva um novo cliente")
    public Cliente save(@RequestBody @Valid Cliente cliente){
        return clientesRepository.save(cliente);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Integer id){
        clientesRepository.findById(id)
                .map(cliente -> {
                    clientesRepository.delete(cliente);
                    return cliente;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));

        /*Optional<Cliente> cliente = this.clientesRepository.findById(id);
        if(cliente.isPresent()){
            clientesRepository.delete(cliente.get());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();*/
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable @Valid Integer id, @RequestBody Cliente cliente){
        clientesRepository.findById(id).map(clienteExists -> {
            cliente.setId(clienteExists.getId());
            clientesRepository.save(cliente);
            return clienteExists;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
    }

    @GetMapping
    public List<Cliente> find(Cliente filtroClientes){
        ExampleMatcher matcher = ExampleMatcher
                                    .matching()
                                    .withIgnoreCase()
                                    .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example example = Example.of(filtroClientes, matcher);
        return clientesRepository.findAll(example);
    }

}
