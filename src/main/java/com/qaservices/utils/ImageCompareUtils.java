package com.qaservices.utils;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.sikuli.basics.Settings;
import org.sikuli.script.Finder;
import org.sikuli.script.Image;
import org.sikuli.script.Match;

import io.appium.java_client.AppiumDriver;

public class ImageCompareUtils {

	/**
	 * To determine the difference between two identically sized regions
	 * 
	 * @param image1
	 * @param image2
	 * @return double - variation factor that ranges between 0 and 1
	 */
	public static double findVariationFactor(BufferedImage image1, BufferedImage image2) {
		assert image1.getHeight() == image2.getHeight() && image1.getWidth() == image2.getWidth();
		double variation = 0.0;
		for (int x = 0; x < image1.getWidth(); x++) {
			for (int y = 0; y < image1.getHeight(); y++) {
				variation += compareARGB(image1.getRGB(x, y), image2.getRGB(x, y)) / Math.sqrt(3);
			}
		}
		return variation / (image1.getWidth() * image1.getHeight());
	}

	/**
	 * To calculate the difference between two ARGB colors
	 * (BufferedImage.TYPE_INT_ARGB).
	 * 
	 * @param rgb1
	 * @param rgb2
	 * @return double
	 */
	public static double compareARGB(int rgb1, int rgb2) {
		double r1 = ((rgb1 >> 16) & 0xFF) / 255.0;
		double r2 = ((rgb2 >> 16) & 0xFF) / 255.0;
		double g1 = ((rgb1 >> 8) & 0xFF) / 255.0;
		double g2 = ((rgb2 >> 8) & 0xFF) / 255.0;
		double b1 = (rgb1 & 0xFF) / 255.0;
		double b2 = (rgb2 & 0xFF) / 255.0;
		double a1 = ((rgb1 >> 24) & 0xFF) / 255.0;
		double a2 = ((rgb2 >> 24) & 0xFF) / 255.0;
		return a1 * a2 * Math.sqrt((r1 - r2) * (r1 - r2) + (g1 - g2) * (g1 - g2) + (b1 - b2) * (b1 - b2));
	}

	/**
	 * To find an expected image within actual image with maximum possible
	 * similarity using pixel matching and return the coordinate of the same.
	 * Note: This method will be slower in execution when the pixel ratio of the
	 * images are high.
	 * 
	 * @param actualImage
	 *            - Image in which the another image will be searched.
	 * @param expectedImage
	 *            - Image which will be searched for.
	 * @return double[] - {x-coord, y-coord, lowestDiff}
	 */
	public static double[] findImageByPixelMatching(BufferedImage actualImage, BufferedImage expectedImage) {
		int w1 = actualImage.getWidth();
		int h1 = actualImage.getHeight();
		int w2 = expectedImage.getWidth();
		int h2 = expectedImage.getHeight();
		assert w2 <= w1 && h2 <= h1;
		int bestX = 0;
		int bestY = 0;
		double lowestDiff = Double.POSITIVE_INFINITY;
		for (int x = 0; x < w1 - w2; x++) {
			for (int y = 0; y < h1 - h2; y++) {
				double comp = findVariationFactor(actualImage.getSubimage(x, y, w2, h2), expectedImage);
				if (comp < lowestDiff) {
					bestX = x;
					bestY = y;
					lowestDiff = comp;
				}
			}
		}
		return new double[] { bestX, bestY, lowestDiff };
	}

	/**
	 * To find an expected image on the borders of actual image with maximum
	 * possible similarity using pixel matching and return the coordinate of the
	 * same. Note: This method will be slower in execution when the pixel ratio
	 * of the images are high.
	 * 
	 * @param actualImage
	 *            - Image in which the another image will be searched.
	 * @param expectedImage
	 *            - Image which will be searched for.
	 * @return double[] - {x-coord, y-coord, lowestDiff}
	 */
	public static double[] findImageAtBorder(BufferedImage actualImage, BufferedImage expectedImage) {
		int w1 = actualImage.getWidth();
		int h1 = actualImage.getHeight();
		int w2 = expectedImage.getWidth();
		int h2 = expectedImage.getHeight();
		assert w2 <= w1 && h2 <= h1;
		int bestX = 0;
		int bestY = 0;
		double lowestDiff = Double.POSITIVE_INFINITY;
		double comp = 0.0;
		for (int x = 0; x < w1 - w2; x++) {
			comp = findVariationFactor(actualImage.getSubimage(x, 0, w2, h2), expectedImage);
			if (comp < lowestDiff) {
				bestX = x;
				bestY = 0;
				lowestDiff = comp;
			}
		}
		for (int x = 0; x < w1 - w2; x++) {
			comp = findVariationFactor(actualImage.getSubimage(x, h1 - h2, w2, h2), expectedImage);
			if (comp < lowestDiff) {
				bestX = x;
				bestY = h1 - h2;
				lowestDiff = comp;
			}
		}
		for (int y = h2; y < h1 - h2; y++) {
			comp = findVariationFactor(actualImage.getSubimage(0, y, w2, h2), expectedImage);
			if (comp < lowestDiff) {
				bestX = 0;
				bestY = y;
				lowestDiff = comp;
			}
		}
		for (int y = h2; y < h1 - h2; y++) {
			comp = findVariationFactor(actualImage.getSubimage(w1 - w2, y, w2, h2), expectedImage);
			if (comp < lowestDiff) {
				bestX = w1 - w2;
				bestY = y;
				lowestDiff = comp;
			}
		}
		return new double[] { bestX, bestY, lowestDiff };
	}

	/**
	 * To find an expected image within actual image with given similarity and
	 * return the coordinate of the same.
	 * 
	 * @param actualImage
	 *            - Image in which the another image will be searched.
	 * @param expectedImage
	 *            - Image which will be searched for.
	 * @param minimumSimilarity
	 *            - 0.0 to 0.99 (More the value, more the similarity)
	 * @return double[] - {x-coord, y-coord}
	 */
	public static double[] findImage(BufferedImage actualImage, BufferedImage expectedImage, double minimumSimilarity) {
		double[] iconCoords = null;
		Finder screen = new Finder(actualImage);
		double defaultMinSimilarity = Settings.MinSimilarity;
		Settings.MinSimilarity = minimumSimilarity;
		screen.find(new Image(expectedImage));
		if (screen.hasNext()) {
			Match match = screen.next();
			iconCoords = new double[] { match.getX(), match.getY() };
			ReportLog.debug("Found expectedImage at (" + iconCoords[0] + ", " + iconCoords[1] + ") of actualImage with similarity " + minimumSimilarity);
		} else {
			ReportLog.debug("Cannot find expectedImage in actualImage with minimum similarity " + Settings.MinSimilarity);
		}
		screen.destroy();
		Settings.MinSimilarity = defaultMinSimilarity;
		return iconCoords;
	}

	/**
	 * To find an expected image within actual image with default similarity
	 * (0.7) and return the coordinate of the same.
	 * 
	 * @param actualImage
	 *            - Image in which the another image will be searched.
	 * @param expectedImage
	 *            - Image which will be searched for.
	 * @return double[] - {x-coord, y-coord}
	 */
	public static double[] findImage(BufferedImage actualImage, BufferedImage expectedImage) {
		return findImage(actualImage, expectedImage, Settings.MinSimilarity);
	}

	/**
	 * To find all instances of an image within another image with given
	 * similarity and return the coordinate of the same.
	 * 
	 * @param actualImage
	 *            - Image in which the another image will be searched.
	 * @param expectedImage
	 *            - Image which will be searched for.
	 * @param minimumSimilarity
	 *            - 0.0 to 0.99 (More the value, more the similarity)
	 * @return HashMap of HashMap in format -> {"1"={"x"=x-coord, "y"=y-coord},
	 *         "2"={"x"=x-coord, "y"=y-coord}, ...}
	 */
	public static HashMap<String, HashMap<String, Double>> findAllImageInstances(BufferedImage actualImage, BufferedImage expectedImage, double minimumSimilarity) {
		HashMap<String, HashMap<String, Double>> allInstances = new HashMap<String, HashMap<String, Double>>();
		HashMap<String, Double> singleInstance = null;
		Finder screen = new Finder(actualImage);
		double defaultMinSimilarity = Settings.MinSimilarity;
		Settings.MinSimilarity = minimumSimilarity;
		screen.findAll(new Image(expectedImage));
		int totalInstances = 0;
		while (screen.hasNext()) {
			totalInstances++;
			Match match = screen.next();
			singleInstance = new HashMap<String, Double>();
			singleInstance.put("x", Double.valueOf(match.getX()));
			singleInstance.put("y", Double.valueOf(match.getY()));
			allInstances.put(String.valueOf(totalInstances), singleInstance);
		}
		screen.destroy();
		Settings.MinSimilarity = defaultMinSimilarity;
		ReportLog.debug("Found " + totalInstances + " instance(s) of expectedImage in actualImage for similarity of " + minimumSimilarity);
		return allInstances;
	}

	/**
	 * To find all instances of an image within another image with default
	 * similarity (0.7) and return the coordinate of the same.
	 * 
	 * @param actualImage
	 *            - Image in which the another image will be searched.
	 * @param expectedImage
	 *            - Image which will be searched for.
	 * @return HashMap of HashMap in format -> {"1"={"x"=x-coord, "y"=y-coord},
	 *         "2"={"x"=x-coord, "y"=y-coord}, ...}
	 */
	public static HashMap<String, HashMap<String, Double>> findAllImageInstances(BufferedImage actualImage, BufferedImage expectedImage) {
		return findAllImageInstances(actualImage, expectedImage, Settings.MinSimilarity);
	}

	/**
	 * To check whether an image is present within another image with given
	 * similarity
	 * 
	 * @param actualImage
	 *            - Image in which the another image will be searched.
	 * @param expectedImage
	 *            - Image which will be searched for.
	 * @param minimumSimilarity
	 *            - 0.0 to 0.99 (More the value, more the similarity)
	 * @return boolean - true, if present
	 */
	public static boolean containsImage(BufferedImage actualImage, BufferedImage expectedImage, double minimumSimilarity) {
		boolean isImagePresent = false;
		Finder screen = new Finder(actualImage);
		double defaultMinSimilarity = Settings.MinSimilarity;
		Settings.MinSimilarity = minimumSimilarity;
		screen.find(new Image(expectedImage));
		if (screen.hasNext()) {
			isImagePresent = true;
		}
		screen.destroy();
		Settings.MinSimilarity = defaultMinSimilarity;
		return isImagePresent;
	}

	/**
	 * To check whether an image is present within another image with default
	 * similarity (0.7)
	 * 
	 * @param actualImage
	 *            - Image in which the another image will be searched.
	 * @param expectedImage
	 *            - Image which will be searched for.
	 * @return boolean - true, if present
	 */
	public static boolean containsImage(BufferedImage actualImage, BufferedImage expectedImage) {
		return containsImage(actualImage, expectedImage, Settings.MinSimilarity);
	}

	/**
	 * To take screenshot of current screen of the given driver instance and
	 * convert it to BufferedImage object. Note: For devices, the image will be
	 * resized based on the dimension of the driver instance
	 * 
	 * @param driver
	 * @return BufferedImage object of current driver screen
	 */
	public static BufferedImage getScreenShotAsBufferedImage(WebDriver driver) {
		try {
			File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			BufferedImage fullscreen = ImageIO.read(screenshot);
			if (driver instanceof AppiumDriver<?>) {
				Dimension screen = driver.manage().window().getSize();
				int screenWidth = screen.getWidth();
				int screenHeight = screen.getHeight();
				fullscreen = resizeImage(fullscreen, screenWidth, screenHeight);
			}
			return fullscreen;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * To convert an image to black and white
	 * 
	 * @param image
	 *            - BufferedImage object of the image that is to be converted
	 * @return BufferedImage - Black and white variant of the image
	 */
	public static BufferedImage convertToBlackAndWhite(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int p = image.getRGB(x, y);
				int a = (p >> 24) & 0xff;
				int r = (p >> 16) & 0xff;
				int g = (p >> 8) & 0xff;
				int b = p & 0xff;
				int avg = (r + g + b) / 3;
				p = (a << 24) | (avg << 16) | (avg << 8) | avg;
				image.setRGB(x, y, p);
			}
		}
		return image;
	}

	/**
	 * To resize an image to given width and height
	 * 
	 * @param originalImage
	 *            - BufferedImage object of the image that is to be resized
	 * @param newImageWidth
	 *            - Width to which the image must be resized
	 * @param newImageHeight
	 *            - Height to which the image must be resized
	 * @return BufferedImage - resized image
	 */
	public static BufferedImage resizeImage(BufferedImage originalImage, int newImageWidth, int newImageHeight) {
		int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
		BufferedImage resizedImage = new BufferedImage(newImageWidth, newImageHeight, type);
		Graphics2D g = resizedImage.createGraphics();

		g.setComposite(AlphaComposite.Src);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.drawImage(originalImage, 0, 0, newImageWidth, newImageHeight, null);
		g.dispose();
		return resizedImage;
	}

	/**
	 * To read the image file represented by given file name as BufferedImage
	 * 
	 * @param fileName
	 *            - absolute path of the file
	 * @return BufferedImage
	 */
	public static BufferedImage getFileAsBufferedImage(String fileName) {
		try {
			return ImageIO.read(new File(fileName));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * To read the image file represented by given resource name as
	 * BufferedImage
	 * 
	 * @param resourceFileName
	 * @return BufferedImage
	 */
	public static BufferedImage getResourceAsBufferedImage(String resourceFileName) {
		try {
			InputStream inputStreamExpected = ImageCompareUtils.class.getResourceAsStream(resourceFileName);
			if (inputStreamExpected == null) {
				throw new RuntimeException("Unable to find resource file:" + resourceFileName);
			}
			return ImageIO.read(ImageIO.createImageInputStream(inputStreamExpected));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * To save a BufferedImage object as an image file in given format.
	 * 
	 * @param imageToBeSaved
	 *            - BufferedImage object of the image that is to be saved
	 * @param fileName
	 *            - absolute path of the file
	 */
	public static void saveAsFile(BufferedImage imageToBeSaved, String fileName) {
		String formatName = fileName.split(".")[fileName.split(".").length - 1];
		File output = new File(fileName);
		try {
			if (output.exists()) {
				ReportLog.debug("Overwriting existing file " + fileName);
			}
			ImageIO.write(imageToBeSaved, formatName, output);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
