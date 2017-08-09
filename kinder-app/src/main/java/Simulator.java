
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Simulator {

	public static String getHtmlContent(String file) {
		ClassLoader cl = Simulator.class.getClassLoader();
		InputStream inputStream = cl.getResourceAsStream(file);
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
	    String line;

	    StringBuilder sb = new StringBuilder();
	    try {
			while ((line = br.readLine()) != null) {
				sb.append(line + System.lineSeparator());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	    try {
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return sb.toString();
	}

}