package com.main.core.repository;

import com.main.core.entity.BaseEntity;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@NoRepositoryBean
@RepositoryRestResource(exported = false)
public abstract interface BaseRepository<T extends BaseEntity>
        extends BaseRepositoryParent<T>
      //  , RevisionRepository<T, Long, Long>
{

}
