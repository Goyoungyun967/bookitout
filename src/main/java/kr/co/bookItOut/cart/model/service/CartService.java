package kr.co.bookItOut.cart.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.bookItOut.cart.model.dao.CartDao;

@Service
public class CartService {
	@Autowired
	private CartDao cartDao;
}
