package com.encurtaurl.principal.api.repository;


import com.encurtaurl.principal.api.model.entidade.URLEncurtada;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EncurtaRepository extends CassandraRepository<URLEncurtada, String> {

    @Query("SELECT url_original from table_url_encurtada WHERE hash = ?0")
    Optional<String> findURLOriginal(String hash);
}
