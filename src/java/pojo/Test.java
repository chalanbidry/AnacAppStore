package pojo;


public class Test {

	public static void main(String[] args) throws Exception {
		long t1 = System.currentTimeMillis();
//		for (int i = 10000; i < 3000; i++) {
			System.out.println(Nombre.CALCULATE.getValue(10.50,"euro"));	
			System.out.println(Nombre.CALCULATE.getValue(6191));
//		}
		long t2 = System.currentTimeMillis();
		System.out.println(t2-t1 +" ms");
	}
}
