package com.uniref.service;

import com.uniref.bean.RefField;
import com.uniref.bean.RefObj;
import com.uniref.bean.RefType;
import com.uniref.bean.RefValue;
import com.uniref.repo.RefFieldRepo;
import com.uniref.repo.RefObjRepo;
import com.uniref.repo.RefTypeRepo;
import com.uniref.repo.RefValuesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Ref;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RefObjService {

  @Autowired
  private RefValuesRepo refValuesRepo;

  @Autowired
  private RefObjRepo refObjRepo;

  @Autowired
  private RefTypeRepo refTypeRepo;

  @Autowired
  private RefFieldRepo refFieldRepo;

  public RefObj createRefObj(String refTypeName) {
    RefType refType = refTypeRepo.findByName(refTypeName);

    if (refType == null) return null;
    RefObj refObj = new RefObj();
    refObj.setRefType(refType);
    refObjRepo.save(refObj);
    return refObj;
  }

  public RefObj createRefObj(String refTypeName, List<RefValue> refValues) {
    RefObj refObj = createRefObj(refTypeName);
    if (refObj != null) {
      refValues.forEach(value -> createRefValue(refObj, value));
      refObj.setRefValues(refValues);
    }
    return refObj;
  }

  private void createRefValue(RefObj refObj, RefValue value) {
    value.setRefObj(refObj);
    refValuesRepo.save(value);
  }

  private void deleteRefObj(RefObj refObj) {
    refValuesRepo.deleteAll(refValuesRepo.findAllByRefObj(refObj));
    refObjRepo.delete(refObj);
  }

  public Set<RefObj> searchByValue(RefType refType, String value) {
    return refValuesRepo.findAllByValueLikeIgnoreCaseAndRefFieldIn(value, refFieldRepo.findAllByRefType(refType))
        .stream().map(RefValue::getRefObj).collect(Collectors.toSet());
  }


}


