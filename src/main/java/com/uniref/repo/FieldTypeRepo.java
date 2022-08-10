package com.uniref.repo;

import com.uniref.bean.Pattern;
import com.uniref.bean.RefField;
import com.uniref.bean.RefType;
import org.springframework.data.repository.CrudRepository;

public interface PatternRepo extends CrudRepository<Pattern, Long> {

}
