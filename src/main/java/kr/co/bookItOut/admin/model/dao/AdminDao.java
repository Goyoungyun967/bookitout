package kr.co.bookItOut.admin.model.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import kr.co.bookItOut.admin.model.dto.Admin;
import kr.co.bookItOut.admin.model.dto.AdminCenterBook;
import kr.co.bookItOut.admin.model.dto.AdminCenterBookRowMapper;
import kr.co.bookItOut.admin.model.dto.AdminOrderBook;
import kr.co.bookItOut.admin.model.dto.AdminOrderBookRowMapper;
import kr.co.bookItOut.admin.model.dto.AdminRowMapper;
import kr.co.bookItOut.admin.model.dto.OrderBookListData;
import kr.co.bookItOut.book.model.dto.AdminBook;
import kr.co.bookItOut.book.model.dto.AdminBookRowMapper;
import kr.co.bookItOut.book.model.dto.Book;
import kr.co.bookItOut.book.model.dto.BookRowMapper;
import kr.co.bookItOut.centerInventory.model.dto.CenterInventory;
import kr.co.bookItOut.centerInventory.model.dto.CenterInventoryRowMapper;

@Repository
public class AdminDao {
	@Autowired
	private JdbcTemplate jdbc;
	@Autowired
	private BookRowMapper bookRowMapper;
	@Autowired
	private AdminRowMapper adminRowMapper;
	@Autowired
	private AdminBookRowMapper adminBookRowMapper;
	@Autowired
	private AdminOrderBookRowMapper adminOrderBookRowMapper;
	@Autowired
	private AdminCenterBookRowMapper adminCenterBookRowMapper;
	@Autowired 
	private CenterInventoryRowMapper centerInventoryRowMapper;
	//-판매자 리스트
	//총관리자 페이지
	public List selectAdminList1(int start, int end) {
		String query = "select * from(select rownum as rnum, n.*from (select * from admin_tbl order by 1 desc)n) where rnum between ? and ?";
		Object[] params = {start,end};
		List list = jdbc.query(query,adminRowMapper,params);
		return list;
	}
	//판매자 리스트
	public List selectAdminList2(Admin admin) {
		String query = "select * from admin_tbl where admin_no =?";
		Object[] params = {admin.getAdminNo()};
		List list = jdbc.query(query,adminRowMapper,params);
		return list;
	}


	
	
	public int selectAdminTotoalCount() {
		String qurey = "select count(*) from admin_tbl";
		
		int totalCount = jdbc.queryForObject(qurey,Integer.class);
		return totalCount;
	}
	//--판매자리스트 끝
	
	//--로그인//
	public Admin selectOneMember(String memberId, String memberPw) {
		String query = "select * from admin_tbl where admin_id=? and admin_pw=?";
		Object[] params = {memberId, memberPw};
		List list = jdbc.query(query, adminRowMapper,params);
		if(list.isEmpty()) {
			return null;
		}else {
			return (Admin)list.get(0);
		}
	}
	//-로그인 끝
	
	//-책 리스트 시작===========================================================
	public List selectBookList(int start, int end) {
		String query = "select * from(select rownum as rnum, n.*from (select * from book order by 1 desc)n) where rnum between ? and ?";
		Object[] params = {start,end};
		List list = jdbc.query(query,bookRowMapper,params);
		return list;
	}
	
	//판매자 책리스트====================================================
	public List selectBookList2(int start, int end, Book book, Admin admin) {
		String query = "select * from " + 
				"(select rownum as rnum, b.* from " + 
				"(SELECT * FROM book " + 
				" JOIN center_inventory ON book.book_no = center_inventory.book_no2 " + 
				" JOIN admin_tbl ON center_inventory.admin_no = admin_tbl.admin_no " + 
				"WHERE admin_tbl.admin_no = ? order by book.book_no desc)b) where rnum between ? and ?";
		Object[] params = {admin.getAdminNo(),start,end};
		List list = jdbc.query(query,adminCenterBookRowMapper,params);
		return list;
	}
	
	//총관리자 책 리스트======================================
	public List selectBookList1(int start, int end) {
		String query = "select * from(select rownum as rnum, n.*from (select * from book order by 1 desc)n) where rnum between ? and ?";
		//많이 바꿔야함
		Object[] params = {start,end};
		List list = jdbc.query(query,adminCenterBookRowMapper,params);
		return list;
	}
	
	
	public int selectBookTotoalCount() {
		String qurey = "select count(*) from book";
		int totalCount = jdbc.queryForObject(qurey,Integer.class);
		return totalCount;
	}
	//-책 리스트 끝
	//-책 등록//
	public int insertBook(Book book ,Admin admin) {
		String qurey = "insert into book values(book_seq.nextval,?,?,?,?,?,to_char(sysdate,'yyyy-mm-dd'),?,?,?,?,?,?,0)";
//				"insert all " + 
//				"into admin_tbl(admin_no) values (?);" + 
//				"into center_inventory(center_book_count,) values(1);" + 
//				"into book(book_no,book_name,book_writer,book_price,book_publisher,publication_date,enroll_date,"
//				+ "book_img,book_detail_content,book_detail_writer,book_detail_img,"
//				+ "book_type,book_genre,read_count) values "
//				+"(book_seq.nextval,?,?,?,?,?,to_char(sysdate,'yyyy-mm-dd'),?,?,?,?,?,?,0)" + 
//				"select * from dual";
		Object[] params = {
							book.getBookName(),book.getBookWriter(),
							book.getBookPrice(),book.getBookPublisher(),
							book.getPublicationDate(),book.getBookImg(),
							book.getBookDetailContent(),
							book.getBookDetailWriter(),book.getBookDetailImg(),
							book.getBookType(),book.getBookGenre()};
		int result = jdbc.update(qurey,params);
		return result;
//		insert all 
//		into 테이블이름X(컬럼이름AX,컬럼이름BX) values (값X1, 값X2)
//		into 테이블이름Y(컬럼이름AY,컬럼이름BY) values (값Y1, 값Y2)
//	    select * from dual;
	}
	//-삭제
	public int deleteBook(int bookNo) {
		String query = "delete from (select * from book join center_inventory on (book_no = book_no2)) where book_no=?";
		Object[] params = {bookNo};
		int result = jdbc.update(query,params);
		return result;
	}
	public Book selectAdminFile(int bookNo) {
		String query = "select * from book where book_no=?";
		Object[] params= {bookNo};
		List list = jdbc.query(query,bookRowMapper,params);
		return (Book)list.get(0);
	}
	//수정
	public Book selectbook(Book book) {
		String query = "select * from book where book_no=?";
		Object[] params = {book.getAdminNo()};
		List list = jdbc.query(query,bookRowMapper,params);
		return (Book)list.get(0);
	}
	//수정 장르 타입 책이름 저자 출판사 
	public int updateBook(Book book) {
 	String query = "update book set book_type=?,book_genre=?,book_price=? where book_no=?";
 	Object[] params = {book.getBookType(),book.getBookGenre(),book.getBookPrice(),book.getBookNo()};
 	int result = jdbc.update(query,params);
		return result;
	}
	//--상세 수정 책 하나 정보 가져오기
	public Book selectbook(int bookNo) {
	String query = "select * from book where book_no=?";
	Object[] params = {bookNo};
	List list = jdbc.query(query,bookRowMapper,params);
	
		return (Book)list.get(0);
	}
	public int updateDetailBook(Book b) {
		String query = "UPDATE book " + 
				"SET book_type=?, " + 
				"    book_genre=?, " + 
				"    book_price=?, " + 
				"    book_publisher=?, " + 
				"    publication_date=?, " + 
				"    book_img=?," + 
				"    book_detail_content=?, " + 
				"    book_detail_writer=?, " + 
				"    book_detail_img=?, " + 
				"    book_name=?, " + 
				"    book_writer=?  " + 
				"WHERE book_no=?";
		Object[] params= {b.getBookType(),b.getBookGenre(),b.getBookPrice(),b.getBookPublisher(),b.getPublicationDate(),
							b.getBookImg(),b.getBookDetailContent(),b.getBookDetailWriter(),b.getBookDetailImg(),b.getBookName(),
								b.getBookWriter(),b.getBookNo()};
		int result = jdbc.update(query,params);					
		return result;
	}
	//발주------------------
	//센터 인벤에서 값 가져오기
	
	public CenterInventory selectCenterInventory(CenterInventory centerInventory) {
		String query = "select * from center_inventory where center_book_no=?";
		Object[] params = {centerInventory.getCenterBookNo()};
		List list = jdbc.query(query,centerInventoryRowMapper,params);
		return (CenterInventory)list.get(0);
	}
		
	public int inserOrderAdmin(int centerBookNo,int orderBookCount,Admin admin) {
		String query = "insert into order_tbl values(order_tbl_seq.nextval,1,to_char(sysdate,'yyyy-mm-dd'),1,?,?)";
		System.out.println(centerBookNo);
		System.out.println(orderBookCount);
		System.out.println(admin);
		Object[] params = {admin.getAdminNo(),centerBookNo};
		int result = jdbc.update(query,params);
		System.out.println(result);
		return result;
	}
	public int selectOrderTotoalCount(Admin admin,int type) {
		String query = "select count(*) from order_tbl where admin_no =? and order_check = ?";
		Object[] params = {admin.getAdminNo(),type};
		int totalCount = jdbc.queryForObject(query,Integer.class,params);
		return totalCount;
	}
	public List selectOrderList(int start, int end, Admin admin, int type) {
		String query = "select * from "
				+ "(select rownum as rnum, o.* from "
				+ "(SELECT * " + 
				"FROM book " + 
				"JOIN order_tbl ON book.book_no = order_tbl.book_no " + 
				"JOIN admin_tbl ON order_tbl.admin_no = admin_tbl.admin_no " + 
				"WHERE admin_tbl.admin_no = ? and order_check=?)o) where rnum between ? and ?";
		Object[] params = {admin.getAdminNo(),type,start,end};
		List list = jdbc.query(query,adminOrderBookRowMapper,params);
		System.out.println(list);
		return list;
	}
	//가맹점 발주버튼 눌렀을때 
	public Book selectOneAdminBook(int bookNo) {
		String query = "select * from book where book_no = ?";
		Object[] params = {bookNo};
		List list = jdbc.query(query,bookRowMapper,params);
		return (Book)list.get(0);
	}
	//발주 버튼 누르고 나오는 화면
	public AdminCenterBook selectOneOrder(int bookNo, Admin admin) {
		String query = "SELECT * " + 
				"FROM book " + 
				"left JOIN center_inventory ON book.book_no = center_inventory.book_no2 " + 
				"left JOIN admin_tbl ON center_inventory.admin_no = admin_tbl.admin_no " + 
				"WHERE admin_tbl.admin_no = ? and book.book_no= ?";
		Object[] params = {admin.getAdminNo(),bookNo};
		List list = jdbc.query(query,adminCenterBookRowMapper,params);
		return (AdminCenterBook)list.get(0);
	}
	public List selectAllOrderList(int start, int end) {
	String query =	"select * from " + 
			"(select rownum as rnum, o.* from " + 
			"(SELECT * " + 
			"FROM book " + 
			"JOIN order_tbl ON book.book_no = order_tbl.book_no " + 
			"JOIN admin_tbl ON order_tbl.admin_no = admin_tbl.admin_no order by order_check asc)o) where rnum between ? and ?";
		Object[] params = {start,end};
		List list = jdbc.query(query,adminOrderBookRowMapper,params);
		
		return list;
	}
	public int selectOrderListTotoalCount() {
		String qurey = "select count(*) from " + 
				"(select rownum as rnum, o.* from " + 
				"(SELECT * " + 
				"FROM book " + 
				"JOIN order_tbl ON book.book_no = order_tbl.book_no " + 
				"JOIN admin_tbl ON order_tbl.admin_no = admin_tbl.admin_no )o)";
		int totalCount = jdbc.queryForObject(qurey,Integer.class);
		return totalCount;
	}
	@Transactional
	public int updateOrderCheck(int orderAllCheck, int orderNo) {
		String query =  "update order_tbl set order_check=? where order_no=?";
		Object[] params = {orderAllCheck,orderNo};
		int result = jdbc.update(query,params);
		System.out.println("list+"+result);
		return result;
	}
	@Transactional
	public int updateCount(int totalCount,int adminNo,int bookNo) {
		String query = "update center_inventory set CENTER_BOOK_COUNT=center_book_count + ? where admin_no=? and book_no2=?";
		Object[] params = {totalCount,adminNo,bookNo};
		int result = jdbc.update(query,params);
		return result;
	}
}
