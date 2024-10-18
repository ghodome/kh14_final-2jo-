package com.art.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.art.dto.CertDto;

@Repository
public class CertDao {

	 @Autowired
	    private SqlSession sqlSession;

	    public void insert(CertDto certDto) {
	        sqlSession.insert("cert.insert", certDto); 
	    }

	    public void delete(String memberEmail) {
	        sqlSession.delete("cert.delete", memberEmail); 
	    }

}
