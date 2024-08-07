package kr.co.bookItOut.book.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import kr.co.bookItOut.book.model.dao.BookDao;
import kr.co.bookItOut.book.model.dto.BookContent;
import kr.co.bookItOut.book.model.dto.BookListData;

@Service
public class BookService {
	@Autowired
	private BookDao bookDao;

	public List selectAllCenterInventory() {
		List list = bookDao.selectAllCenterInventory();
		return list;
	}

//	public int insertComment(BookContent bc) {
//		int result = bookDao.insertComment(bc);
//		return 0;
//	}

	public BookListData selectBookList(int reqPage) {
		int numPerPage = 10;
		int end = reqPage * numPerPage;
		int start = end - numPerPage +1;
		
		List list = bookDao.selectBookList(start, end);
				
		int totalCount = bookDao.selectBookTotalCount();
		int totalPage = 0;
		if(totalCount % numPerPage == 0) {
			totalPage = totalCount / numPerPage;			
		}else {
			totalPage = totalCount / numPerPage + 1;			
		}
		
		int pageNaviSize = 10;		
		int pageNo = ((reqPage - 1) / pageNaviSize) * pageNaviSize + 1;
		
		String pageNavi = "<div class='inner'><ul>";
		if(pageNo !=1) {
			pageNavi += "<li>";
			pageNavi += "<a class='page-item' href='book/list?reqPage=" + (pageNo - 1) +"'>";
			pageNavi += "<span class='material-icons'>chevron_left</span>";
			pageNavi += "</a></li>";			
		}
		
		for(int i = 0; i< pageNaviSize; i++) {
			pageNavi += "<li>";
			if(pageNo == reqPage) {
				pageNavi += "<a class='page-item active-page' href='/book/list?reqPage=" + pageNo + "'>"; 
			} else {
				pageNavi += "<a class='page-item' href='/book/list?reqPage=" +pageNo + "'>";
			}
			pageNavi += pageNo;
			pageNavi += "</a></li>";
			pageNo++;
			
			if(pageNo > totalPage) {
				break;
			}			
		}
		
		if(pageNo <= totalPage) {
			pageNavi += "<li>";
			pageNavi += "<a class='page-item' href='/book/list?reqPage=" + pageNo + "'>";
			pageNavi += "<span class='material-icons'>chevron_right</span>";
			pageNavi += "</a></li>";
		}
		pageNavi += "</ul></div>";
		
		BookListData bld = new BookListData(list, pageNavi);		
		
		return bld;
	}

}
