package com.swoqe.newsstand.model.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CrudPageableService<T, ID> extends CrudService<T, ID>{
    Page<T> findAll(Pageable paging);
}
