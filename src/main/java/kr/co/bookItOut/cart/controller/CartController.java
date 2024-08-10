package kr.co.bookItOut.cart.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import kr.co.bookItOut.cart.model.dto.Cart;
import kr.co.bookItOut.cart.model.service.CartService;
import kr.co.bookItOut.member.model.dto.Member;
import kr.co.bookItOut.member.model.service.MemberService;
import kr.co.bookItOut.pay.model.dto.Pay;
import kr.co.bookItOut.util.FileUtils;


@Controller
@RequestMapping(value = "/cart")
public class CartController {
	@Autowired
	private CartService cartService;

	@Value("${file.root}")
	private String root;

	@Autowired
	private FileUtils fileUtils;// 파일업로드 처리 객체
	
	@Autowired
	private MemberService memberService;

	@GetMapping(value = "/main")
	public String loginFrm(Model model, @SessionAttribute(required=false) Member member) {
		int memberNo = member.getMemberNo();
		List list = cartService.selectAllCart(memberNo);
		model.addAttribute("list", list);
		
		return "cart/main";
	}
	
	@GetMapping("/paySuccess")
	public String paySuccess() {
		
		return "cart/paySuccess";
	}
	@GetMapping("/selectPayNames")
    @ResponseBody
    public List selectPayNames(@RequestParam("payNo") int payNo) {
        System.out.println("테스트");
        System.out.println("받은 payNo 값: " + payNo);
        
        // payNo에 해당하는 상품명을 조회
        List list = cartService.selectPayNames(payNo);
        List bookName = new ArrayList<String>();
        
        for(int i=0; i<list.size(); i++) {
        	bookName.add(((Pay)(list.get(i))).getBookName());
        }
        
        System.out.println(bookName);
        return bookName;  // JSON 형태로 반환됨
    }
	
	@ResponseBody
	@GetMapping(value="/addCart")
	public int addCart(int bookNo, @SessionAttribute Member member) {
		System.out.println(bookNo);
		int memberNo = member.getMemberNo();		
		System.out.println(memberNo);
		int result = cartService.insertCart(bookNo,memberNo);			
		return result;
	}
	@ResponseBody
	@GetMapping(value="/selectCart")
	public int selectCart (int bookNo, @SessionAttribute Member member) {
		int memberNo = member.getMemberNo();
		int result = cartService.selectCart(bookNo, memberNo);
		return result;
	}

	
	@ResponseBody
	@GetMapping(value="/plusCart")
	public int plusCart (int cartNo) {
		int result = cartService.plusCart(cartNo);				
		return result;
	}
	

	@GetMapping("/selDel")
	public String selDel(String name, @SessionAttribute(required=false) Member member, Model model) {
		
		int memberNo = member.getMemberNo();
		boolean result = cartService.selDel(name, memberNo);
		System.out.println(name);
		
		if(result) {
			System.out.println("삭제 성공");
			return "redirect:/cart/main";
		}else {
			System.out.println("삭제 실패");
			return "redirect:/cart/main";
		}
		
	}
	
	@PostMapping("/success")
	public String success(String cartNoStr, int price, @SessionAttribute(required=false) Member member, String name, String addr) {
		System.out.println("카트 넘버는 : "+cartNoStr);
		System.out.println(addr);
		System.out.println(name);
		boolean preResult = cartService.success1(price, member, addr, name);
		
		
		boolean result = cartService.success(cartNoStr, price, member);
		return "redirect:/cart/main";
	}
	
	
	@GetMapping("/selPay")
	public String selPay(String name, String bookCount, String totalPrice, Model model, @SessionAttribute(required=false) Member member) {
		System.out.println("책 이름은 : "+name);
		System.out.println("수량은 : "+bookCount);
		//장바구니 수량 변경 시 결제화면에 반영 안됨
		
		
		int memberNo = member.getMemberNo();
		List list = cartService.selPay(memberNo, name, bookCount);
		
//		System.out.println("선택한 책 이름 /로 구분 -- "+name);
//		System.out.println("총 가격 -- "+totalPrice);
		
		
		
		model.addAttribute("totalPrice", totalPrice);
		model.addAttribute("list", list);
		
		model.addAttribute("member",member);
		
		List cartNo = new ArrayList<Integer>();
		
		//System.out.println("카트넘버가져오기"+((Cart)list.get(0)).getCartNo());
		
		for(int i=0; i<list.size(); i++) {
			cartNo.add(((Cart)list.get(i)).getCartNo());
		}
		model.addAttribute("cartNo",cartNo);
		
		return "cart/selPay";
	}
	
	

}
