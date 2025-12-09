package com.encurtaurl.principal.api.model.entidade;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("table_url_encurtada")
public class URLEncurtada {

    @PrimaryKeyColumn(name = "hash", type = PrimaryKeyType.PARTITIONED)
    private String hash;
    @Column("url_original")
    private String urlOriginal;

    public URLEncurtada(String hash, String urlOriginal) {
        this.hash = hash;
        this.urlOriginal = urlOriginal;
    }

    public String getHash() {
        return hash;
    }

    public String getUrlOriginal() {
        return urlOriginal;
    }
}
