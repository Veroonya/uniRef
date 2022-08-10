package com.uniref.service;

import com.uniref.bean.FieldType;
import com.uniref.bean.RefField;
import com.uniref.bean.RefType;
import com.uniref.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для работы со справочниками
 */
@Service
public class RefTypeService {

  /**
   * Репозиторий для работы с типами полей
   */
  @Autowired
  private FieldTypeRepo fieldTypeRepo;
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
   * Создание справочника
   * @param name Наименование справочника
   * @return Справочник
   */
  public RefType createRefType(String name) {
    RefType refType = refTypeRepo.findByName(name);
    if (refType != null) return refType;
    refType = new RefType();
    refType.setName(name);
    refTypeRepo.save(refType);
    return refType;
  }

  /**
   * Обновление справочника
   * @param refType Справочник
   * @return Справочник
   */
  public RefType updateRefType(RefType refType) {
    //TODO: Полезнее был бы метод, чтобы менять наименование, заменять одно на другое
    refTypeRepo.save(refType);
    return refType;
  }

  /**
   * Удаление справочника со всеми значениями
   * @param refType Справочник
   */
  public void deleteRefType(RefType refType) {
    List<RefField> refFields = refFieldRepo.findAllByRefType(refType);
    refFields.forEach(refField ->  refValuesRepo.deleteAll(refValuesRepo.findAllByRefField(refField)));
    refObjRepo.deleteAll(refObjRepo.findAllByRefType(refType));
    refFieldRepo.deleteAll(refFields);
    refTypeRepo.delete(refType);
  }

  /**
   * Добавляет поле в справочник
   * @param refTypeName Наименование справочника
   * @param nameField Ниаменование поля
   * @param fieldTypeName Ниаменование типа поля
   * @param order порядок поля в справочнике
   * @return поле справочника
   */
  public RefField addRefField(String refTypeName, String nameField, String fieldTypeName, Integer order) {
    RefType refType = refTypeRepo.findByName(refTypeName);
    if (refType == null) return null;
    FieldType fieldType = fieldTypeRepo.findByName(fieldTypeName);
    if (fieldType == null) return null;
    RefField refField = refFieldRepo.findByRefTypeAndName(refType, nameField);
    if (refField != null)
      return refField;       //TODO: Здесь не учитывается вариант, что делать, если поле с таким же именем, но другого типа

    return addRefField(refType, nameField, fieldType, order);
  }

  /**
   *  Добавляет поле в справочник
   * @param refType Справочника
   * @param nameField Наименование поля
   * @param fieldType Тип поля
   * @param order порядок поля в справочнике
   * @return поле справочника
   */
  private RefField addRefField(RefType refType, String nameField, FieldType fieldType, Integer order) {
    RefField refField = new RefField();
    refField.setRefType(refType);
    refField.setName(nameField);
    refField.setFieldType(fieldType);
    refField.setOrdr(order);

    if (order == null) {
      RefField refFieldMaxOrder = refFieldRepo.findRefFieldByRefTypeOrderByOrdrDesc(refType);
      order = refFieldMaxOrder == null ? 0 : refFieldMaxOrder.getOrdr() + 1;
    } else {
      refFieldRepo.findAllByRefTypeAndOrdrGreaterThanEqual(refType, order).forEach(refField1 -> {
        refField1.setOrdr(refField1.getOrdr() + 1);
        refFieldRepo.save(refField1);
      });
    }
    refField.setOrdr(order);
    refFieldRepo.save(refField);
    return refField;
  }

  /**
   * Обновляет поле справочника
   * @param refField поле справочника
   * @return поле справочника
   */
  public RefField updateRefField(RefField refField) {
    refFieldRepo.save(refField);
    return refField;
  }

  /**
   * Удаляет поле справочника
   * @param refField поле справочника
   */
  public void deleteRefField(RefField refField) {
    //TODO: лучше реализовать метод для удаления по наименованию справочника и наименованию поля
    refFieldRepo.delete(refField);
  }

}
