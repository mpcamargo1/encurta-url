package com.encurtaurl.principal.api.repository;


import com.encurtaurl.principal.api.model.entidade.URLEncurtada;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EncurtaRepository extends CassandraRepository<URLEncurtada, String> {


}
