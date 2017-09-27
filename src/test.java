import java.io.FileNotFoundException;
import java.io.PrintWriter;


public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//changing B
		try {
			int n = 1000000;
			for (int B = 100000; B <= n; B+=100000){
				PrintWriter out = new PrintWriter("data/changingB/test3,B="+B/10+",n="+n+".txt");
				for (int i = 1; i < n; i += 10) {
					if (i < B){
						out.println(i + " gaattcaaaa");
					} else {
						out.println(i + " aaaaaaaaaa");
					}
				}
				out.close();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//changing n
		try {
			int B = 1000000;
			for (int n = 10000000; n <= 100000000; n+= 10000000) {
				PrintWriter out = new PrintWriter("data/changingn2/test3,B="+B/10+"n="+n+".txt");
				for (int i = 1; i < n; i+=10){
					if (i < B){
						out.println(i + " gaattcaaaa");
					} else {
						out.println(i + " aaaaaaaaaa");
					}
				}
				out.close();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
