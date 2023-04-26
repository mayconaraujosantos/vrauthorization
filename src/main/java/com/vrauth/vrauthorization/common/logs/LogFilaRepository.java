package com.vrauth.vrauthorization.common.logs;

import com.vrauth.vrauthorization.domain.entities.LogFila;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LogFilaRepository extends MongoRepository<LogFila, String> {

}
