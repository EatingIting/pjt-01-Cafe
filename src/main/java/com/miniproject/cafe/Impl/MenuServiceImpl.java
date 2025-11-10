package com.miniproject.cafe.Impl;

import com.miniproject.cafe.Mapper.MenuMapper;
import com.miniproject.cafe.Service.MenuService;
import com.miniproject.cafe.VO.MenuVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<MenuVO> getAllMenu() {
        return menuMapper.getAllMenu();
    }
}
