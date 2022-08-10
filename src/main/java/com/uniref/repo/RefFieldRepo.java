package com.uniref.repo;

import com.uniref.bean.RefField;
import com.uniref.bean.RefType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RefFieldRepo extends CrudRepository<RefField, Long> {

  RefField findRefFieldByRefTypeOrderByOrdrDesc(RefType refType);

  List<RefField> findAllByRefTypeAndOrdrGreaterThanEqual(RefType refType, Integer ordr);
  List<RefField> findAllByRefType(RefType refType);

  RefField findByRefTypeAndName(RefType refType, String name);

}
