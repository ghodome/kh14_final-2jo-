package com.art.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.art.dto.ItemsDto;

@Repository
public class ItemsDao {

	@Autowired
	private SqlSession sqlSession;
	
	public void insert(ItemsDto dto) {
		sqlSession.insert("items.insert",dto);
	}
	public boolean update(ItemsDto dto) {
		return sqlSession.update("items.update",dto)>0;
	}
	public boolean delete(int itemId) {
		return sqlSession.delete("items.delete",itemId)>0;
	}
	public List<ItemsDto> list(){
		return sqlSession.selectList("items.list");
	}
	public List<ItemsDto> rarityList(){
		return sqlSession.selectList("items.rarityList");
	}
	public int itemSequence() {
		return sqlSession.selectOne("items.itemSequence");
	}
	
}
