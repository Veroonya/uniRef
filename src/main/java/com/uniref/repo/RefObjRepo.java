package com.uniref.repo;

import com.uniref.bean.RefObj;
import com.uniref.bean.RefType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RefObjRepo  extends CrudRepository<RefObj, Long> {

  List<RefObj> findAllByRefType(RefType refType);
}
