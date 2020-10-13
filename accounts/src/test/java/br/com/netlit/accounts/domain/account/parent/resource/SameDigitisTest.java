package br.com.netlit.accounts.domain.account.parent.resource;

public class SameDigitisTest {

	
	public static void main(String[] args) {
		
		System.out.println(digitosIguais("000000000000"));
		System.out.println(digitosIguais("000002000000"));
		System.out.println(digitosIguais("000000000000"));
		System.out.println(digitosIguais("000000000000"));
		System.out.println(digitosIguais("000000000000"));
		System.out.println(digitosIguais("000000000000"));
		System.out.println(digitosIguais("000000000000"));
		
		System.out.println(String.format("%05d", Integer.valueOf("12300")));
		
	}
	
	
	static boolean digitosIguais(String numero) {
	    return numero.chars().distinct().count() == 1;
	}

}
