package com.uniref.service;

import com.uniref.bean.FieldType;
import com.uniref.bean.RefField;
import com.uniref.bean.RefType;
import com.uniref.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RefTypeService {

  @Autowired
  private RefTypeRepo refTypeRepo;

  @Autowired
  private RefFieldRepo refFieldRepo;

  @Autowired
  private FieldTypeRepo fieldTypeRepo;

  @Autowired
  private RefValuesRepo refValuesRepo;

  @Autowired
  private RefObjRepo refObjRepo;

  public RefType createRefType(String name) {
    RefType refType = refTypeRepo.findByName(name);
    if (refType != null) return refType;
    refType = new RefType();
    refType.setName(name);
    refTypeRepo.save(refType);
    return refType;
  }

  public RefType updateRefType(RefType refType) {
    refTypeRepo.save(refType);
    return refType;
  }

  public void deleteRefType(RefType refType) {
    List<RefField> refFields = refFieldRepo.findAllByRefType(refType);
    refFields.forEach(refField ->  refValuesRepo.deleteAll(refValuesRepo.findAllByRefField(refField)));
    refObjRepo.deleteAll(refObjRepo.findAllByRefType(refType));
    refFieldRepo.deleteAll(refFields);
    refTypeRepo.delete(refType);
  }
  
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

  public RefField addRefField(RefType refType, String nameField, FieldType fieldType, Integer order) {
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

  public RefField updateRefField(RefField refField) {
    refFieldRepo.save(refField);
    return refField;
  }

  public void deleteRefField(RefField refField) {
    refFieldRepo.delete(refField);
  }

}
