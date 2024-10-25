package com.art.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.art.dto.WorkDto;
import com.art.vo.WorkArtistVO;

@Repository
public class workDao {
	
	@Autowired
	private SqlSession sqlSession;
	
	public int sequence() {
		return sqlSession.selectOne("work.sequence");
	}
	
	public List<WorkArtistVO> selectList() {
		return sqlSession.selectList("work.list");
	}
	
	public void insert(WorkArtistVO workArtistVO) {
		
		sqlSession.insert("work.insert",workArtistVO);
	}
	
	public boolean delete(int workNo) {
		return sqlSession.delete("work.delete", workNo)>0;
	}
	
	public boolean update(WorkArtistVO workArtistVO) {
		return sqlSession.update("work.update", workArtistVO) > 0;
	}
	
	//연결기능
	public void connect(int work, int attachment) {
        Map<String, Object> params = new HashMap<>();
        params.put("work", work);
        params.put("attachment", attachment);
        sqlSession.insert("work_image.connect", params);
	}
	//이미지 번호 찾기 기능
//	public Integer findImage(int poketmonNo) {
//		String sql = "select attachment from poketmon_image where poketmon=?";
//		Object[] data = {poketmonNo};
//	}
	
}
