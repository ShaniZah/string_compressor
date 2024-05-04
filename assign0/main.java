package assign0;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StringEncoderDecoder str = new StringEncoderDecoder();
		String[] s = new String[1];
		String[] t = new String[1];

		s[0] = "romeo , and then, and juliet\r\n";
		System.out.println(s[0]);
		str.Compress(s, t);
		System.out.println(t[0]);
		str.Decompress(s, t);
		System.out.println(t[0]);

	}

}
