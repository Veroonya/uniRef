package com.uniref.repo;

import com.uniref.bean.RefType;
import org.springframework.data.repository.CrudRepository;

public interface RefTypeRepo extends CrudRepository<RefType, Long> {

  RefType findByName(String name);
}
