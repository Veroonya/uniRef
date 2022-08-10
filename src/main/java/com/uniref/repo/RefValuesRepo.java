package com.uniref.repo;

import com.uniref.bean.RefField;
import com.uniref.bean.RefObj;
import com.uniref.bean.RefValue;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RefValuesRepo extends CrudRepository<RefValue, Long> {
  List<RefValue> findAllByRefObj(RefObj refObj);
  List<RefValue> findAllByRefField(RefField refField);
  List<RefValue> findAllByValueLikeIgnoreCaseAndRefFieldIn(String value, List<RefField> refFields);
}
