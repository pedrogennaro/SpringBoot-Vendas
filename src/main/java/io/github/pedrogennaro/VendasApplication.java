package io.github.pedrogennaro;

import io.github.pedrogennaro.domain.entity.Cliente;
import io.github.pedrogennaro.domain.entity.Pedido;
import io.github.pedrogennaro.domain.repository.Clientes;
import io.github.pedrogennaro.domain.repository.Pedidos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class VendasApplication {

    @Bean
    public CommandLineRunner init(@Autowired Clientes clientes, @Autowired Pedidos pedidos){
        return args -> {
            System.out.println("Salvando Clientes");
            //clientes.salvar(new Cliente("Pedro De Gennaro"));
            //clientes.salvar(new Cliente("Dayanne de Queiroz"));
            //clientes.save(new Cliente("Pedro De Gennaro"));
            //clientes.save(new Cliente("Dayanne de Queiroz"));

            Cliente clienteNovo = new Cliente("Gisela De Gennaro");
            clientes.save(clienteNovo);

            Pedido pedido = new Pedido();
            pedido.setCliente(clienteNovo);
            pedido.setDataPedido(LocalDate.now());
            pedido.setTotal(BigDecimal.valueOf(100));

            pedidos.save(pedido);

            System.out.println("Buscando Clientes");
//            Cliente buscarCliente = clientes.findClienteFetchPedidos(clienteNovo.getId());
//            System.out.println(buscarCliente);
//            System.out.println(buscarCliente.getPedidos());

            pedidos.findByCliente(clienteNovo).forEach(System.out::println);

            //            System.out.println("Buscando Clientes");
//            //List<Cliente> clientesAll = clientes.obterClientes();
//            List<Cliente> clientesAll = clientes.findAll();
//            clientesAll.forEach(System.out::println);
//
//            System.out.println("Atualizando Clientes");
//            clientesAll.forEach(c -> {
//                c.setNome(c.getNome() + " atualizado.");
//                //clientes.atualizar(c);
//                clientes.save(c);
//            });
//            clientesAll = clientes.findAll();
//            clientesAll.forEach(System.out::println);
//
//            System.out.println("Buscando por nome");
//            //clientes.findByNomeLike("Day").forEach(System.out::println);
//            clientes.encontrarPorNome("Day").forEach(System.out::println);
//
//            System.out.println("Deletando clientes");
//            clientes.findAll().forEach(c -> {
//                clientes.delete(c);
//            });
//
//            clientesAll = clientes.findAll();
//            if(clientesAll.isEmpty()){
//                System.out.println("Nenhum cliente encontrado");
//            } else{
//                clientesAll.forEach(System.out::println);
//            }

        };
    }

    //atalho para classe main: psvm
    public static void main(String[] args) {
        SpringApplication.run(VendasApplication.class, args);
    }
}
