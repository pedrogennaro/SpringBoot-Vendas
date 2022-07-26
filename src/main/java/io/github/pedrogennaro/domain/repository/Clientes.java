package io.github.pedrogennaro.domain.repository;

import io.github.pedrogennaro.domain.entity.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

//@Repository
//public class Clientes {
public interface Clientes extends JpaRepository<Cliente, Integer> {

    //@Query(" select * from cliente c where c.nome like '%:nome%' ", nativeQuery = true)
    @Query("select c from Cliente c where c.nome like :nome")
    List<Cliente> encontrarPorNome(@Param("nome") String name);

    @Query("delete from Cliente c where c.nome = :nome")
    @Modifying // para fazer modificações na tabela
    void deleteByNome(String nome);

    @Query("select c from Cliente c left join fetch c.pedidos where c.id = :id")
    Cliente findClienteFetchPedidos(@Param("id") Integer id);

    List<Cliente> findByNomeLike(String name);

//    private static String INSERT = "insert into Clientes (nome) values (?)";
//    private static String SELECT_ALL = "select * from Clientes";
//    private static String UPDATE = "update Clientes set nome = ? where id = ?";
//    private static String DELETE = "delete from Clientes where id = ?";

//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    @Autowired
//    private EntityManager entityManager;
//
//    @Transactional
//    public Cliente salvar(Cliente cliente){
//        //jdbcTemplate.update(INSERT, new Object[]{cliente.getNome()});
//        entityManager.persist(cliente);
//        return cliente;
//    }
//
//    @Transactional
//    public Cliente atualizar(Cliente cliente){
//        //jdbcTemplate.update(UPDATE, new Object[]{cliente.getNome(), cliente.getId()});
//        entityManager.merge(cliente);
//        return cliente;
//    }
//
//    @Transactional
//    public void deletar(Cliente cliente){
//        //deletar(cliente.getId());
//        if(!entityManager.contains(cliente)){
//            cliente = entityManager.merge(cliente);
//        }
//        entityManager.remove(cliente);
//    }
//
//    @Transactional
//    public void deletar(Integer id){
//        //jdbcTemplate.update(DELETE, new Object[]{id});
//        Cliente cliente = entityManager.find(Cliente.class, id);
//        deletar(cliente);
//    }
//
//    @Transactional(readOnly = true)
//    public List<Cliente> buscarByName(String nome){
//        //return jdbcTemplate.query(SELECT_ALL.concat(" where nome like ?"), new Object[]{"%" + nome + "%"}, obterMapper());
//        String jpql = " select c from Cliente c where c.nome like :nome ";
//        TypedQuery<Cliente> query = entityManager.createQuery(jpql, Cliente.class);
//        query.setParameter("nome", "%" + nome + "%");
//        return query.getResultList();
//    }
//
//    @Transactional(readOnly = true)
//    public List<Cliente> obterClientes(){
//        //return jdbcTemplate.query(SELECT_ALL, obterMapper());
//        return entityManager.createQuery("from Cliente", Cliente.class).getResultList();
//    }
//
////    private RowMapper<Cliente> obterMapper() {
////        return new RowMapper<Cliente>() {
////            @Override
////            public Cliente mapRow(ResultSet resultSet, int i) throws SQLException {
////                Integer id = resultSet.getInt("id");
////                String nome = resultSet.getString("nome");
////                return new Cliente(id, nome);
////            }
////        };
////    }

}
