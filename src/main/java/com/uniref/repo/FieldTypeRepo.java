package com.uniref.repo;

import com.uniref.bean.FieldType;
import com.uniref.bean.RefType;
import org.springframework.data.repository.CrudRepository;

public interface FieldTypeRepo extends CrudRepository<FieldType, Long> {

  FieldType findByName(String name);
  FieldType findByPattern(String pattern);

}
