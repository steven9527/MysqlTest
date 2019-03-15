package com.steven.mysql.controller;

import com.steven.mysql.entity.Contact;
import com.steven.mysql.entity.ContactExample;
import com.steven.mysql.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by steven.zhao on 2019/3/12.
 */

@Controller
public class HelloController {

    @Autowired
    private ContactService contactService;

    @RequestMapping("/hello")
    @ResponseBody
    private Map hello() {
        Map map = new HashMap();
        map.put("id",1);
        map.put("name", "hello,world");

        return  map;
    }

    @RequestMapping("/add")
    @ResponseBody
    private Map add() {

        Contact contact=new Contact();
//        contact.setId(1L);
        contact.setEmail("steven.1@qq.com");

        contactService.insert(contact);

        Map map = new HashMap();
        map.put("id",contact.getId());
        map.put("name", contact.getEmail());

        return  map;
    }

    @RequestMapping("/list")
    @ResponseBody
    private List<Contact> list() {

        ContactExample example=new ContactExample();
        ContactExample.Criteria criteria = example.createCriteria();
//        criteria.andEmailLike("qq.com");

        criteria.andIdGreaterThan(18);
        List<Contact> contacts = contactService.selectByExample(example);


//        Contact contact=new Contact();
////        contact.setId(1L);
//        contact.setEmail("steven.1@qq.com");
//
//        contactService.insert(contact);
//
//        Map map = new HashMap();
//        map.put("id",contact.getId());
//        map.put("name", contact.getEmail());

        return  contacts;
    }


}
