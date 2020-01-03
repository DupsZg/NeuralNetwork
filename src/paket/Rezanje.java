package paket;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.encog.Encog;


public class Rezanje {
	
	public static int BROJ_EMOTIKONA=8;
	public static int brojac=1;
	public static int brojTreningSeta=1;
	public static String PUTANJA_KONFIGURACIJE="./Config.txt";
	public static int iteracija = 0;
	
	public static void main(final String[] args) {
		
		long Time = 0;
		
		Time -= System.currentTimeMillis();

		for (int i = 0; i <BROJ_EMOTIKONA; i++) {
			// rezanje slike za trening
			try {
				rezanjeSlike();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			final ImageNeuralNetwork program = new ImageNeuralNetwork();
			program.execute(PUTANJA_KONFIGURACIJE);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	
	Encog.getInstance().shutdown();
	
	Time += System.currentTimeMillis();

	System.out.println("\nUkupan zbroj iteracija: " + iteracija);
	System.out.println("\nUkupno vrijeme trajanja : "
			+ ((double) Time / 1000) + " sekundi");
	}
	
	public static void rezanjeSlike() throws IOException{
		
		File file= new File("SpojeneSlike/slika" + brojac + ".jpg");
		brojac++;
		FileInputStream fis = new FileInputStream(file); 
		// Pozivanje metode za čitanje slike
		BufferedImage image = ImageIO.read(fis);
		// Definiranje broja redaka i stupaca slike 
		// 1 redak i 14 stupaca za broj slova u „neuralnetworks“ 
		int rows = 1;
		int cols = 7;
		int chunks = rows * cols;
		// određivanje veličine jednog dijela
		int chunkWidth = image.getWidth() / cols;
		int chunkHeight = image.getHeight() / rows;
		// Definiranje polja koje sadrži dijelove slika 
		int count = 0;
		BufferedImage imgs[] = new BufferedImage[chunks];
		for (int x = 0; x < rows; x++) {
			for (int y = 0; y < cols; y++) { 
				// Inicijalizacija polja s dijelovima slike 
				imgs[count] = new BufferedImage(chunkWidth, chunkHeight, image.getType()); 
				// "Crtanje" pojedinih dijelova slike 
				Graphics2D gr = imgs[count++].createGraphics();
				gr.drawImage(image, 0, 0, chunkWidth, chunkHeight, chunkWidth * y, chunkHeight * x, chunkWidth * y + chunkWidth, chunkHeight * x + chunkHeight, null); 
				gr.dispose(); 
			} 
		} 
		System.out.println("Rezanje slike je završeno.");
		
		boolean success = (new File("RezaneSlike/TreningSet" + brojTreningSeta + "/")).mkdirs();
		
		if (!success) {
		    System.out.println("Datoteka vec postoji!");
		}
		
		for (int i = 0; i < imgs.length; i++) { 
			ImageIO.write(imgs[i], "jpg", new File("RezaneSlike/TreningSet"+brojTreningSeta +"/"+ "izrezanaSlika" + i + ".jpg"));
		} 
		brojTreningSeta++;
		System.out.println("Kreiranje slika je završeno.");
	}
}



