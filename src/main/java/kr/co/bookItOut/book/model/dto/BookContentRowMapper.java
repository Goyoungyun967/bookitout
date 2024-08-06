package kr.co.bookItOut.book.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class BookContentRowMapper implements RowMapper<BookContent> {

	@Override
	public BookContent mapRow(ResultSet rs, int rowNum) throws SQLException {
		BookContent content = new BookContent();
		content.setBookCommentNo(rs.getInt("book_comment_no"));
		content.setBookNo(rs.getInt("book_no"));
		content.setMemberNo(rs.getInt("member_no"));
		content.setBookCommentContent(rs.getString("book_comment_content"));
		content.setBookCommentDate(rs.getNString("book_comment_date"));
		return content;
	}

}
