package kr.co.bookItOut.FAQ.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FaqListData {
	private List list;
	private String pageNavi;
	private String faqType;
	
}
