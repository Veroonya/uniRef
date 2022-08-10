package com.uniref.service;

import com.uniref.bean.*;
import com.uniref.repo.RefFieldRepo;
import com.uniref.repo.RefObjRepo;
import com.uniref.repo.RefTypeRepo;
import com.uniref.repo.RefValuesRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class RefObjServiceTest {

  private static final String refTypeName = "Справочник фамилий";

  @Autowired
  private RefObjService refObjService;

  @Autowired
  private RefFieldRepo refFieldRepo;

  @MockBean
  private RefTypeRepo refTypeRepo;

  @MockBean
  private RefObjRepo refObjRepo;

  @MockBean
  private RefValuesRepo refValuesRepo;

  @Test
  void createRefObjRefNotExists() {

    RefObj refObj = refObjService.createRefObj(refTypeName);

    Assertions.assertNull(refObj);
    Mockito.verify(refTypeRepo, Mockito.times(1)).findByName(refTypeName);
    Mockito.verify(refObjRepo, Mockito.never()).save(ArgumentMatchers.any(RefObj.class));

  }

  @Test
  void createRefObj() {
    Mockito.doReturn(new RefType())
        .when(refTypeRepo)
        .findByName(refTypeName);

    RefObj refObj = refObjService.createRefObj(refTypeName);

    Assertions.assertNotNull(refObj);
    Assertions.assertNotNull(refObj.getRefType());
    Mockito.verify(refTypeRepo, Mockito.times(1)).findByName(refTypeName);
    Mockito.verify(refObjRepo, Mockito.times(1)).save(refObj);
  }

  @Test
  void createRefObjWithValues() {
    Mockito.doReturn(new RefType())
        .when(refTypeRepo)
        .findByName(refTypeName);

    RefValue refValue = new RefValue();

    RefObj refObj = refObjService.createRefObj(refTypeName, Collections.singletonList(refValue));

    Assertions.assertNotNull(refObj);
    Assertions.assertNotNull(refObj.getRefType());
    Assertions.assertEquals(refObj.getRefValues().size(), 1);
    Assertions.assertNotNull(refObj.getRefValues().get(0));
    Assertions.assertEquals(refObj.getRefValues().get(0).getRefObj(), refObj);

    Mockito.verify(refTypeRepo, Mockito.times(1)).findByName(refTypeName);
    Mockito.verify(refObjRepo, Mockito.times(1)).save(refObj);
    Mockito.verify(refValuesRepo, Mockito.times(refObj.getRefValues().size())).save(ArgumentMatchers.any(RefValue.class));
  }

  @Test
  void createRefObjWithValuesRefNotExists() {

    RefValue refValue = new RefValue();

    RefObj refObj = refObjService.createRefObj(refTypeName, Collections.singletonList(refValue));

    Assertions.assertNull(refObj);

    Mockito.verify(refTypeRepo, Mockito.times(1)).findByName(refTypeName);
    Mockito.verify(refObjRepo, Mockito.never()).save(ArgumentMatchers.any(RefObj.class));
    Mockito.verify(refValuesRepo, Mockito.never()).save(ArgumentMatchers.any(RefValue.class));

  }

}