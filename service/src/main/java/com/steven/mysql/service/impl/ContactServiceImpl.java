package com.steven.mysql.service.impl;

import com.steven.mysql.Mapper.ContactMapper;
import com.steven.mysql.entity.Contact;
import com.steven.mysql.entity.ContactExample;
import com.steven.mysql.service.ContactService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.JDBCType;
import java.util.List;

/**
 * Created by steven.zhao on 2019/3/12.
 */

@Service("contactService")
public class ContactServiceImpl implements ContactService {

    @Autowired
    ContactMapper contactMapper;

    @Override
    public int countByExample(ContactExample example) {
        return contactMapper.countByExample(example);
    }

    @Override
    public int deleteByExample(ContactExample example) {
        return contactMapper.deleteByExample(example);
    }

    public int insert(Contact record) {
        return contactMapper.insert(record);


    }

    public int insertSelective(Contact record) {
        return 0;
    }

    @Override
    public List<Contact> selectByExample(ContactExample example) {
        return contactMapper.selectByExample(example);
    }

    @Override
    public int updateByExampleSelective(@Param("record") Contact record, @Param("example") ContactExample example) {
        return 0;
    }

    @Override
    public int updateByExample(@Param("record") Contact record, @Param("example") ContactExample example) {
        return 0;
    }
}
