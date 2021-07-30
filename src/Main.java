
import org.dunzo.sde2.test.AbstractTest;
import org.dunzo.sde2.test.Test1;
import org.dunzo.sde2.test.Test2;
import org.dunzo.sde2.test.Test3;

public class Main {
	public static void main(String[] args) throws Exception {
		String filePath = "./inputs/inp1.json";
		
		AbstractTest t1 = new Test1();
		t1.run(filePath);

//		AbstractTest t2 = new Test2();
//		t2.run(filePath);
////		
//		AbstractTest t3 = new Test3();
//		t3.run(filePath);
	}
}
