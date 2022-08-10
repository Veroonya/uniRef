package com.uniref.service;

import com.uniref.bean.RefField;
import com.uniref.bean.RefObj;
import com.uniref.bean.RefType;
import com.uniref.bean.RefValue;
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
import java.util.List;
import java.util.Set;

/**
 * Тестирование сервиса по работе с объектами (записями справочника)
 */
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

  /**
   * Создание записи справочника
   */
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

  /**
   * Создание записи справочника, когда справочника не существует
   */
  @Test
  void createRefObjWithValuesRefNotExists() {

    RefValue refValue = new RefValue();

    RefObj refObj = refObjService.createRefObj(refTypeName, Collections.singletonList(refValue));

    Assertions.assertNull(refObj);

    Mockito.verify(refTypeRepo, Mockito.times(1)).findByName(refTypeName);
    Mockito.verify(refObjRepo, Mockito.never()).save(ArgumentMatchers.any(RefObj.class));
    Mockito.verify(refValuesRepo, Mockito.never()).save(ArgumentMatchers.any(RefValue.class));

  }

  /**
   * Поиск по справочнику, когда справочника с таким наименованием не существует
   */
  @Test
  void searchByValueNoRefType() {
    Set<RefObj> objs = refObjService.searchByValue(refTypeName, "Скряжевский");

    Assertions.assertNull(objs);

    Mockito.verify(refTypeRepo, Mockito.times(1)).findByName(refTypeName);
    Mockito.verify(refValuesRepo, Mockito.never())
        .findAllByValueLikeIgnoreCaseAndRefFieldIn(ArgumentMatchers.any(String.class), ArgumentMatchers.any(List.class));
  }

  /**
   * Поиск по справочнику
   */
  @Test
  void searchByValue() {
    String searchVal = "Скряжевский";

    Set<RefObj> objs = refObjService.searchByValue(refTypeName, searchVal);

    Assertions.assertNotNull(objs);

    Mockito.verify(refTypeRepo, Mockito.times(1)).findByName(refTypeName);
    Mockito.verify(refValuesRepo, Mockito.times(1))
        .findAllByValueLikeIgnoreCaseAndRefFieldIn(searchVal, ArgumentMatchers.any(List.class));


  }

}