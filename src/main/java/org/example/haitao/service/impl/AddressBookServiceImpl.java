package org.example.haitao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.haitao.entity.AddressBook;
import org.example.haitao.mapper.AddressBookMapper;
import org.example.haitao.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
