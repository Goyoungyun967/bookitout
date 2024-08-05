package kr.co.bookItOut.member.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.bookItOut.member.model.dao.MemberDao;
import kr.co.bookItOut.member.model.dto.Member;

@Service
public class MemberService {

	@Autowired
	private MemberDao memberDao;

	public Member selectOneMember(String memberId, String memberPw) {
		Member member = memberDao.selectOneMember(memberId, memberPw);
		return member;
	}

	public Member selectOneMember(String checkId) {
		Member member = memberDao.selectOneMember(checkId);
		return member;
	}

	public int insertMember(Member m) {
		int result = memberDao.insertMember(m);
		return result;
	}
	
}
