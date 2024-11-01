package com.art.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.art.dto.InventoryDto;

@Repository
public class InventoryDao {
	
	@Autowired
	private SqlSession sqlSession;
	
	public void insert(InventoryDto dto) {
		sqlSession.insert("inventory.insert",dto);
	}
	public InventoryDto selectOne(String memberId) {
		return sqlSession.selectOne("inventory.detail",memberId);
	}
	public boolean delete(int inventoryId) {
		return sqlSession.delete("inventory.delete",inventoryId)>0;
	}
	public List<InventoryDto> selectList(String memberId){
		return sqlSession.selectList("inventory.detailList",memberId);
	}
}
