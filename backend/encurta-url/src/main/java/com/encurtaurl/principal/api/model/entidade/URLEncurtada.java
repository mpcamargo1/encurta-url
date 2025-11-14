package com.encurtaurl.principal.api.model.entidade;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("url_encurtada")
public class URLEncurtada {

    @PrimaryKeyColumn(name = "url_curta", type = PrimaryKeyType.PARTITIONED)
    private String urlCurta;

    @Column("url_original")
    private String urlOriginal;
}
