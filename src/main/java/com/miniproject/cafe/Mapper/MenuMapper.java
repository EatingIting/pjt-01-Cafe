package com.miniproject.cafe.Mapper;

import com.miniproject.cafe.VO.MenuVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuMapper {
    List<MenuVO> getAllMenu();
}
