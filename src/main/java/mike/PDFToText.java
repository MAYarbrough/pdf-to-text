package mike;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDFToText {
	String fn, fnOut;
	
	public PDFToText(String[] args) {
		try {
			readArgs(args);
			toText();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void toText() throws FileNotFoundException, IOException{
		PDFParser parser = new PDFParser(new RandomAccessFile(new File(fn), "r"));
		parser.parse();
		try(PDDocument doc = parser.getPDDocument()) {
			PDFTextStripper stripper = new PDFTextStripper();
			Path outPath = Paths.get(fnOut);
			System.out.println("writing to " + outPath);
			Files.write(outPath, stripper.getText(doc).getBytes(StandardCharsets.UTF_8));
		}
	}
	
	private void readArgs(String[] args) {
		if(args.length > 0) {
			fn = args[0];
		} else {
			fn = "somePdfFile.pdf";
		}
		if(args.length > 1) {
			fnOut = args[1];
		} else {
			fnOut = "output.txt";
		}
	}

	public static void main(String[] args) {
		new PDFToText(args);
	}
}
