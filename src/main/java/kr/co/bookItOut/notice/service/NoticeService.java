package kr.co.bookItOut.notice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.bookItOut.notice.dao.NoticeDao;
import kr.co.bookItOut.notice.dto.Notice;
import kr.co.bookItOut.notice.dto.NoticeData;
import kr.co.bookItOut.notice.dto.NoticeListData;

@Service
public class NoticeService {
	@Autowired
	private NoticeDao noticeDao;

	public NoticeListData selectAllNotice(int reqPage) {
		int numPerPage = 10;
		int end = reqPage*numPerPage;
		int start = end - numPerPage + 1;
		
		List list = noticeDao.selectAllNotice(start,end);
		
		int totalCount = noticeDao.totalCount();
		int totalPage = 0;
		if(totalCount%numPerPage ==0) {
			totalPage = totalCount/numPerPage;
		}else {
			totalPage = totalCount/numPerPage + 1;
		}
		int pageNaviSize = 5;
		int pageNo = (reqPage-1)/pageNaviSize*pageNaviSize + 1;
		String pageNavi = "<div class='pagination'><ul>";
		if(pageNo !=1) {
			pageNavi += "<li>";
			pageNavi += "<a class ='page-item page-btn' href='/notice/noticeFrm?reqPage="+(pageNo-1)+"'>";
			pageNavi += "<span><i class='fa-solid fa-angle-left'></i></span>";
			pageNavi += "</a></li>";
		}
		for(int i =0;i<pageNaviSize;i++) {
			pageNavi += "<li>";
			if(pageNo==reqPage) {
				pageNavi += "<a class ='page-item active-page' href='/notice/noticeFrm?reqPage="+pageNo+"'>";
			}else {
				pageNavi += "<a class ='page-item' href='/notice/noticeFrm?reqPage="+pageNo+"'>";
			}
			pageNavi += pageNo;
			pageNavi += "</a></li>"; 
			pageNo++;
			if(pageNo>totalPage) {
				break;
			}
		}
		if(pageNo <= totalPage) {
			pageNavi += "<li>";
			pageNavi += "<a class ='page-item page-btn' href='/notice/noticeFrm?reqPage="+pageNo+"'>";
			pageNavi += "<span><i class='fa-solid fa-angle-right'></i></span>";
			pageNavi += "</a></li>";
		}
		pageNavi +="</ul>";
		pageNavi +="</ul></div>";
		NoticeListData nld = new NoticeListData(list, pageNavi,totalCount);
		return nld;
	}

	public Notice selectOneNotice(int noticeNo) {
		int result = noticeDao.readCount(noticeNo);
		Notice notice = noticeDao.selectOneNotice(noticeNo);
		NoticeData noticeData = noticeDao.selectTwoNotice(noticeNo);
		notice.setNoticeData(noticeData);
		return notice;
	}
	@Transactional
	public int insertNotice(Notice n) {
		int result = noticeDao.insertNotice(n);
		if(result >0) {
			return result;
		}
		return 0;
	}
	@Transactional
	public int updateNotice(Notice n) {
		int result = noticeDao.updateNotice(n);
		System.out.println("서비스 result : "+result);
		if(result>0) {
			return result;
		}
		return 0;
	}
	@Transactional
	public int deleteNotice(int noticeNo) {
		int result = noticeDao.deleteNotice(noticeNo);
		if(result>0) {
			return result;
		}
		return 0;
	}

	public NoticeListData searchNotice(int reqPage, String keyword) {
		int numPerPage = 10;
		int end = reqPage*numPerPage;
		int start = end - numPerPage + 1;
		
		List list = noticeDao.selectAllNotice(start,end,keyword);
		
		int totalCount = noticeDao.totalCount(keyword);
		int totalPage = 0;
		if(totalCount%numPerPage ==0) {
			totalPage = totalCount/numPerPage;
		}else {
			totalPage = totalCount/numPerPage + 1;
		}
		int pageNaviSize = 5;
		int pageNo = (reqPage-1)/pageNaviSize*pageNaviSize + 1;
		String pageNavi = "<div class='pagination'><ul>";
		if(pageNo !=1) {
			pageNavi += "<li>";
			pageNavi += "<a class ='page-item page-btn' href='/notice/noticeFrm?keyword="+keyword+"&reqPage="+(pageNo-1)+"'>";
			pageNavi += "<span><i class='fa-solid fa-angle-left'></i></span>";
			pageNavi += "</a></li>";
		}
		for(int i =0;i<pageNaviSize;i++) {
			pageNavi += "<li>";
			if(pageNo==reqPage) {
				pageNavi += "<a class ='page-item active-page' href='/notice/noticeFrm?keyword="+keyword+"reqPage="+pageNo+"'>";
			}else {
				pageNavi += "<a class ='page-item' href='/notice/noticeFrm?keyword="+keyword+"reqPage="+pageNo+"'>";
			}
			pageNavi += pageNo;
			pageNavi += "</a></li>"; 
			pageNo++;
			if(pageNo>totalPage) {
				break;
			}
		}
		if(pageNo <= totalPage) {
			pageNavi += "<li>";
			pageNavi += "<a class ='page-item page-btn' href='/notice/noticeFrm?keyword="+keyword+"reqPage="+pageNo+"'>";
			pageNavi += "<span><i class='fa-solid fa-angle-right'></i></span>";
			pageNavi += "</a></li>";
		}
		pageNavi +="</ul>";
		pageNavi +="</ul></div>";
		NoticeListData nld = new NoticeListData(list, pageNavi,totalCount);
		return nld;
	}

	public List selectAllNotice() {
		List list = noticeDao.selectThreeNotice();
		return list;
	}
	
}
