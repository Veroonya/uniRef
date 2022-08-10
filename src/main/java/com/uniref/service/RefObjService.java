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

/**
 * Сервис для работы с объектами справочников
 */
@Service
public class RefObjService {

  /**
   * Репозиторий для работы со значениями объектов справочника
   */
  @Autowired
  private RefValuesRepo refValuesRepo;

  /**
   * Репозиторий для работы с объектами справочник
   */
  @Autowired
  private RefObjRepo refObjRepo;

  /**
   * Репозиторий для работы со справочниками
   */
  @Autowired
  private RefTypeRepo refTypeRepo;

  /**
   * Репозиторий для работы с полями справочника
   */
  @Autowired
  private RefFieldRepo refFieldRepo;

  /**
   * Создает объект справочника (запись) - без добавления полей
   * @param refTypeName тип объекта ( в какой справочник добавляем объект)
   * @return объект
   */
  private RefObj createRefObj(String refTypeName) {
    RefType refType = refTypeRepo.findByName(refTypeName);

    if (refType == null) return null;
    RefObj refObj = new RefObj();
    refObj.setRefType(refType);
    refObjRepo.save(refObj);
    return refObj;
  }

  /**
   * Создает запись справочника (объект + значения полей)
   * @param refTypeName тип объекта ( в какой справочник добавляем объект)
   * @param refValues Значения поля справочника
   * @return объект (запись) справочника
   */
  public RefObj createRefObj(String refTypeName, List<RefValue> refValues) {
    RefObj refObj = createRefObj(refTypeName);
    if (refObj != null && refValues != null) {
      refValues.forEach(value -> createRefValue(refObj, value));
      refObj.setRefValues(refValues);
    }
    return refObj;
  }

  /**
   * Создает Значение поля объекта справочника
   * @param refObj Объект справочника
   * @param value значение объекта справочника
   */
  private void createRefValue(RefObj refObj, RefValue value) {
    value.setRefObj(refObj);
    refValuesRepo.save(value);
  }

  /**
   * Удаляет объект (запись) справочника вместе со своими значениями
   * @param refObj Объект для удаления
   */
  private void deleteRefObj(RefObj refObj) {
    refValuesRepo.deleteAll(refValuesRepo.findAllByRefObj(refObj));
    refObjRepo.delete(refObj);
  }

  /**
   * Поиск по значениям справочника
   * @param refTypeName наименование справочника, в котором ищем
   * @param value значение, которое ищем
   * @return Список объектов справочника, содержащих значение
   */
  public Set<RefObj> searchByValue(String refTypeName, String value) {
    RefType refType = refTypeRepo.findByName(refTypeName);
    if (refType == null) return null;
    return refValuesRepo.findAllByValueLikeIgnoreCaseAndRefFieldIn(value, refFieldRepo.findAllByRefType(refType))
        .stream().map(RefValue::getRefObj).collect(Collectors.toSet());
  }


}


