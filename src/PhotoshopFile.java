// Photoshop program that can run several manipulations on 
// an image
// filler code by Mr. David

import java.awt.Color;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

public class PhotoshopFile extends Component {

	// the name of the output file. will be determined by which methods are called
    private String outputName;
    
    // the 2d array of colors representing the image
    private Color[][] pixels;
    
    // the width and height of the image 
    private int w,h;
    

    // this method increases each color's rgb value by a given amount.
    // don't forget that rgb values are limited to the range [0,255]
    public void brighten(int amount) {
        outputName = "brightened_" + outputName;
        
        for(int i = 0; i < pixels.length; i++) {
        	for(int j = 0; j < pixels[i].length; j++) {
	        	Color c = pixels[i][j];
	        	
	        	int r = c.getRed() + amount;
	        	int g = c.getGreen() + amount;
	        	int b = c.getBlue() + amount;
	        	
	        	if(r > 255) {
	        		r = 255;
	        	}
	        	if(g > 255) {
	        		g = 255;
	        	}
	        	if(b > 255) {
	        		b = 255;
	        	}
	        	if(amount < 0) {
	        		if(r < 0) {
	        			r = 0;
	        		}
	        		if(g < 0) {
	        			g = 0;
	        		}
	        		if(b < 0) {
	        			b = 0;
	        		}
	        	}
	        	
	        	pixels[i][j] = new Color(r, g, b);
        	}
        }
    }
    
    // flip an image either horizontally or vertically.
    public void flip(boolean horizontally) {
        outputName = (horizontally?"h":"v") + "_flipped_" + outputName;
        
        if(horizontally) {
	        for(int i = 0; i < pixels.length; i++) {
	        	for(int j = 0; j < pixels[i].length/2; j++) {
	        		Color c = pixels[i][j];
	        		Color c2 = pixels[i][pixels[i].length-j-1];
	        		pixels[i][pixels[i].length-j-1] = new Color(c.getRed(), c.getGreen(), c.getBlue());
	        		pixels[i][j] = new Color(c2.getRed(), c2.getGreen(), c2.getBlue());
	        	}
        	}
        }
        
        else {
        	for(int i = 0; i < pixels.length/2; i++) {
        		for(int j = 0; j < pixels[i].length; j++) {
        			Color c = pixels[i][j];
    	        	Color c2 = pixels[pixels.length-1-i][j]; 
        			pixels[pixels.length-1-i][j] = new Color(c.getRed(),c.getGreen(),c.getBlue());
        			pixels[i][j] = new Color(c2.getRed(), c2.getGreen(), c2.getBlue());
        		}
        	}
        }
    }
    
    // negates an image
    // to do this: subtract each pixel's rgb value from 255 
    // and use this as the new value
    public void negate() {
        outputName = "negated_" + outputName;
        
        for(int i = 0; i < pixels.length; i++) {
        	for(int j = 0; j < pixels[i].length; j++) {
        		Color c = pixels[i][j];
                pixels[i][j] = new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue());
                
        	}
        }
    }
    
    // this makes the image 'simpler' by redrawing it using only a few colors
    // to do this: for each pixel, find the color in the list that is closest to
    // the pixel's rgb value. 
    // use this predefined color as the rgb value for the changed image.
    public void simplify() {
    
    		// the list of colors to compare to. Feel free to change/add colors
    		Color[] colorList = {Color.BLUE, Color.RED,Color.ORANGE, Color.MAGENTA,
                Color.BLACK, Color.WHITE, Color.GREEN, Color.YELLOW, Color.CYAN};
        outputName = "simplified_" + outputName;
        
        for(int i = 0; i < pixels.length; i++) {
        	for(int j = 0; j < pixels[i].length; j++) {
        		Color c = pixels[i][j];
        		int r = c.getRed();
        		int g = c.getGreen();
        		int b = c.getBlue();
        		Color similarColor = colorList[0];
        		double smallestDifference = Math.sqrt(3*Math.pow(255, 2));
        		for(int x = 0; x < colorList.length; x++) {
        			Color test = colorList[x];
        			int testR = test.getRed();
        			int testG = test.getGreen();
        			int testB = test.getBlue();
        			double difference = Math.sqrt(Math.pow(testR-r, 2) + Math.pow(testG-g, 2) + Math.pow(testB-b, 2));
        			if(difference < smallestDifference) {
        				smallestDifference = difference;
        				similarColor = colorList[x];
        			}
        			
        		}
        		pixels[i][j] = similarColor;
        	}
        }
         
    }
    
    // optional helper method (recommended) that finds the 'distance' 
    // between two colors.
    // use the 3d distance formula to calculate
    public double distance(Color c1, Color c2) {
    	
    		return 0;	// fix this
    }
    
    // this blurs the image
    // to do this: at each pixel, sum the 8 surrounding pixels' rgb values 
    // with the current pixel's own rgb value. 
    // divide this sum by 9, and set it as the rgb value for the blurred image
    public void blur() {
		outputName = "blurred_" + outputName;
		
		for(int i = 1; i < pixels.length-1; i++) {
			for(int j = 1; j < pixels[i].length-1; j++) {
				int sumRed = 0;
				int sumGreen = 0;
				int sumBlue = 0;
				for(int x = -1; x < 2; x++) {
					for(int y = -1; y < 2; y++) {
						sumRed += pixels[i+x][j+y].getRed();
						sumGreen += pixels[i+x][j+y].getGreen();
						sumBlue += pixels[i+x][j+y].getBlue();
					}
				}
				pixels[i][j] = new Color(sumRed/9, sumGreen/9, sumBlue/9);
			}
		}
	}
    
    // this highlights the edges in the image, turning everything else black. 
    // to do this: at each pixel, sum the 8 surrounding pixels' rgb values. 
    // now, multiply the current pixel's rgb value by 8, then subtract the sum.
    // this value is the rgb value for the 'edged' image
    public void edge() {
        outputName = "edged_" + outputName;
        Color originalPixels [][];
        originalPixels = new Color [pixels.length][];
        for(int i = 0; i < pixels.length; i++) {
        	originalPixels[i] = new Color[pixels[i].length];
        	for(int j = 0; j < pixels[i].length; j++) {
        		originalPixels[i][j] = pixels[i][j];
        	}
        }
        for(int i = 1; i < originalPixels.length-1; i++) {
        	for(int j = 1; j < originalPixels[i].length-1; j++) {
        		int sumRed = 0;
				int sumGreen = 0;
				int sumBlue = 0;
        		for(int x = -1; x < 2; x++) {
        			for(int y = -1; y < 2; y++) {
        				sumRed += originalPixels[i+x][j+y].getRed();
						sumGreen += originalPixels[i+x][j+y].getGreen();
						sumBlue += originalPixels[i+x][j+y].getBlue();
        			}
        		}
        		int r = originalPixels[i][j].getRed()*9-sumRed;
        		int g = originalPixels[i][j].getGreen()*9-sumGreen;
        		int b = originalPixels[i][j].getBlue()*9-sumBlue;
        		if(r > 255) {
	        		r = 255;
	        	}
	        	if(g > 255) {
	        		g = 255;
	        	}
	        	if(b > 255) {
	        		b = 255;
	        	}
	        	if(r < 0) {
	        		r = 0;
	        	}
	        	if(g < 0) {
	        		g = 0;
	        	}
	        	if(b < 0) {
	        		b = 0;
	        	}
        		pixels[i][j] = new Color (r, g, b);
        	}
        }
        
    }
    
    // this method just turns the image into black-and-white
    // I found the numbers in which I had to multiply by and stuff online
    public void monochrome() {
    	outputName = "monochrome_" + outputName;
    	for(int i = 0; i < pixels.length; i++) {
    		for(int j = 0; j < pixels[i].length; j++) {
    			Color c = pixels[i][j];
    			int r = (int)(c.getRed()*0.299);
    			int g = (int)(c.getGreen()*0.587);
    			int b = (int)(c.getBlue()*0.144);
    			int sum = r+g+b;
    			if(sum > 255) {
    				sum = 255;
    			}
    			pixels[i][j] = new Color(sum, sum, sum);
    		}
    	}
    }  
    
    // *************** DON'T MESS WITH THE BELOW CODE **************** //
    
    // feel free to check it out, but don't change it unless you've consulted 
    // with Mr. David and understand what the code's doing
    
    

    public void run() {
    	JFileChooser fc = new JFileChooser();
		File workingDirectory = new File(System.getProperty("user.dir")+System.getProperty("file.separator")+ "Images");
		//fc.setCurrentDirectory(workingDirectory);
		fc.showOpenDialog(null);
		File my_file = fc.getSelectedFile();
		if (my_file == null)
			System.exit(-1);
		
		// reads the image file and creates our 2d array
        BufferedImage image;
		try {
			image = ImageIO.read(my_file);
		
	        BufferedImage new_image = new BufferedImage(image.getWidth(),
	                        image.getHeight(), BufferedImage.TYPE_INT_ARGB);
	        create_pixel_array(image);
			outputName = my_file.getName();
			
			// runs the manipulations determined by the user
			System.out.println("Enter the manipulations you would like to run on the image.\nYour "
					+ "choices are: brighten, flip, negate, blur, edge, monochrome, or simplify.\nEnter each "
					+ "manipulation you'd like to run, then type in 'done'.");
			Scanner in = new Scanner(System.in);
			String action = in.next().toLowerCase();
			while (!action.equals("done")) {
	    			try {
		    			if (action.equals("brighten")) {
		    				System.out.println("enter an amount to increase the brightness by");
		    				int brightness = in.nextInt();
		        			Method m = getClass().getDeclaredMethod(action, int.class);
		        			m.invoke(this, brightness);
		    			}
		    			else if (action.equals("flip")) {
		    				System.out.println("enter \"h\" to flip horizontally, anything else to flip vertically.");
		        			Method m = getClass().getDeclaredMethod(action, boolean.class);
		        			m.invoke(this, in.next().equals("h"));
		    			}
		    			else {
		        			Method m = getClass().getDeclaredMethod(action);
		        			m.invoke(this, new Object[0]);
		    			}
		    			System.out.println("done. enter another action, or type 'done'");
	    			}
	    			catch (NoSuchMethodException e) {
	    				System.out.println("not a valid action, try again");
	    			} catch (IllegalAccessException e) {e.printStackTrace();System.exit(1);} 
	    			catch (IllegalArgumentException e) {e.printStackTrace();System.exit(1);}
	    			catch (InvocationTargetException e) {e.printStackTrace();System.exit(1);}
	    			
	    			action = in.next().toLowerCase();
	    		} 
	        in.close();
	        
	        // turns our 2d array of colors into a new png file
	        create_new_image(new_image);
	        File output_file = new File("Images/" + outputName);
	        ImageIO.write(new_image, "png", output_file);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
		
    
    public void create_pixel_array(BufferedImage image) {
        w = image.getWidth();
        h = image.getHeight();
        pixels = new Color[h][];
        for (int i = 0; i < h; i++) {
            pixels[i] = new Color[w];
            for (int j = 0; j < w; j++) {
                pixels[i][j] = new Color(image.getRGB(j,i));
            }
        }
    }

    public void create_new_image(BufferedImage new_image) {
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
            		new_image.setRGB(j, i, pixels[i][j].getRGB());
            }
        }
    }

    public static void main(String[] args) {
		new PhotoshopFile();
	}

    public PhotoshopFile() {
		run();
    }
}