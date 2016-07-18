/**
 * @author: Artjoms Porss
 * 
 * Description: Application that deletes a portion of string between START_AFTER and END_BEFORE after each SEARCH_AFTER piece of text
 *  in a file.
 * Works where SEARCH_AFTER, START_AFTER and END_BEFORE are unique and never used for anything else. Else - watch for bugs.
 * Myself used to edit big portions of HTML with 1-5K lines of markup.
 * 
 * Example: Use to remove <option>s in <select> between START_AFTER and END_BEFORE where class is SEARCH_AFTER.
 */


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class CodeRemover {
	static final String SEARCH_AFTER = "class_name";
	static final String START_AFTER = "begin_tag";
	static final String END_BEFORE = "end_tag";

	public static void main(String[] args) {
		
		String testString = loadText();

		int deleted = 0;
		int startPos = 0;
		int endPos = 0;

		String temp = null;
		
		System.out.println("Deletion Started");
		
		long startTime = System.currentTimeMillis();
		
		doSearch: do {
			startPos = 0;
			endPos = 0;

			// iterate for each deletion done to get next deletion starting point
			for (int i = 0; i <= deleted; ++i) {
				// get position to start searching from
				startPos = testString.indexOf(SEARCH_AFTER,
						startPos);

				// if currentStartPos is -1, which means no SEARCH_AFTER left, break and move to "saveText" part.
				if (startPos == -1)
					break doSearch;

				// get the current position of start of deletion
				startPos = testString.indexOf(START_AFTER,
						startPos);
				startPos += START_AFTER.length();
			}

			// get the end position
			endPos = testString.indexOf(END_BEFORE, startPos);

			// get the substrings of text except what has to be gone using currentStartPos and currentEndPos we found
			temp = testString.substring(0, startPos);
			temp += testString.substring(endPos, testString.length());

			// store the string we got
			testString = temp;

			System.out.format("Deletion No: %d   Deleted: %d characters%n", ++deleted, endPos-startPos);// one deletion done
		} while (true);
		
		long endTime = System.currentTimeMillis();
		System.out.format("Deletion Complete. Deletion occured in %d places, time to complete: %.2f seconds%n", deleted, (endTime-startTime)/100.0);

		// save when done
		saveText(testString);
	}

	// loads text from file
	public static String loadText() {
		//TODO set end of filepath and filename as desired - in this case it is Eclipse project, so it's in /bin/ folder
		//TODO file to read from must be created manually and match the name specified here.
		String filePath = System.getProperty("user.dir") + "\\bin\\text.txt";

		StringBuilder text = new StringBuilder();
		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(filePath));
			Scanner scan = new Scanner(br);

			while (scan.hasNext()) {
				text.append(scan.nextLine() + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return text.toString();
	}

	//saves text to file
	public static void saveText(String textToSave) {
		String filePath = System.getProperty("user.dir")
				+ "\\bin\\text modified.txt";
		File saveFile = new File(filePath);
		FileWriter writer = null;

		// delete file if exists
		if (saveFile.exists()) {
			saveFile.delete();
		}

		try {
			saveFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			writer = new FileWriter(saveFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (writer == null)
			return;
		try {
			writer.write(textToSave);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
