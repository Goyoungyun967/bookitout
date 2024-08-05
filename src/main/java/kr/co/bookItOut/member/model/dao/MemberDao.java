package kr.co.bookItOut.member.model.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import kr.co.bookItOut.member.model.dto.Member;
import kr.co.bookItOut.member.model.dto.MemberRowMapper;

@Repository
public class MemberDao {
	@Autowired
	private JdbcTemplate jdbc;
	@Autowired
	private MemberRowMapper memberRowMapper;
	public Member selectOneMember(String memberId, String memberPw) {
		String query = "select * from member_tbl where member_id=? and member_pw=?";
		Object[] params = {memberId, memberPw};
		List list = jdbc.query(query, memberRowMapper,params);
		if(list.isEmpty()) {
			return null;
		}else {
			return (Member)list.get(0);
		}
	}
	public Member selectOneMember(String checkId) {
		String query = "select * from member_tbl where member_id=?";
		Object[] params = {checkId};
		List list = jdbc.query(query, memberRowMapper,params);
		if(list.isEmpty()) {
			return null;
		}else {
			return (Member)list.get(0);
		}
	}
	public int insertMember(Member m) {
		String query = "insert into member_tbl values(member_seq.nextval,?,?,?,?,?,?,?,?,to_char(sysdate,'yyyy-mm-dd'))";
		Object[] params = {m.getMemberId(),m.getMemberPw(),m.getMemberName(), m.getMemberGender(), m.getMemberAge(), m.getMemberAddr(),m.getMemberPhone(),m.getMemberImg()};
		int result = jdbc.update(query, params);
		return result;
	}
	
}
