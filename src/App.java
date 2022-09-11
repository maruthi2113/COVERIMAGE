
import javax.imageio.ImageIO;         // provides lots of utility method related to images processing in Java      
import java.awt.image.BufferedImage;  // used to handle and manipulate the image data
import java.awt.image.WritableRaster;
import java.io.*;                     // input and output methods
import java.awt.*;                    //Provides the classes necessary to create an applet and the classes an applet uses to communicate with its applet context.
import java.net.URL;                  //that the protocol to use is http (HyperText Transfer Protocol) 
import java.util.*;                   //collection framework, collection classes, classes related to date and time, event model, internationalization, and miscellaneous utility classes.

public class App {
    public static void main(String[] args) throws IOException {
        // Setting Chrome as an agent
        System.setProperty("http.agent", "Chrome");   // the setProperty method enables QAs to set the properties for the desired browser to be used in test automation.

        // reading the original image file
        // File file = new File("C:\Users\91812\OneDrive\Pictures\Screenshots\Screenshot (192).png");
        // FileInputStream sourceFile = new FileInputStream(file);
        
        // reading the file from a URL
    //    URL url= new URL("http://commondatastorage.googleapis.com/codeskulptor-assets/gutenberg.jpg");
    //     InputStream is = url.openStream();
    //     BufferedImage image = ImageIO.read(is);  // to perform operations on images
           
          BufferedImage image =ImageIO.read(new File("C:/Users/91812/OneDrive/Pictures/140459769_709623966383145_5577420263835915942_n.jpg"));
         try (Scanner input = new Scanner(System.in)) {     // to read the data
             System.out.println("enter n*m size of blocks to be chunked ");
             int n,m;
             n=input.nextInt();   //to read n rows;
             m=input.nextInt();   // to read m columns;
    
        
        int rows = n;
        int columns = m;
        BufferedImage imgs[] = new BufferedImage[n*m];     // initializing array to hold subimages

        
        int subimage_Width = image.getWidth() / columns;   // Equally dividing original image into subimages
        int subimage_Height = image.getHeight() / rows;
        int current_img = 0;                             // to count image splitted
        
        // iterating over rows and columns for each sub-image
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                // Creating sub image
                imgs[current_img] = new BufferedImage(subimage_Width, subimage_Height, image.getType());
                Graphics2D img_creator = imgs[current_img].createGraphics();   //Graphics2D used to extract sub image

                // coordinates of source image
                int src_first_x = subimage_Width * j;
                int src_first_y = subimage_Height * i;

                // coordinates of sub-image
                int dst_corner_x = subimage_Width * j + subimage_Width;
                int dst_corner_y = subimage_Height * i + subimage_Height;
                
                img_creator.drawImage(image, 0, 0, subimage_Width, subimage_Height, src_first_x, src_first_y, dst_corner_x, dst_corner_y, null);
                current_img++;
            }
        }
        Entophy e= new Entophy(); // calling entrophy class
        System.out.println("enter the entrophy you like to consider entrophy should be less than 8.00");
        double minentrophy= input.nextDouble();
       
         double minvar;
    
         int countentrophyimages=0;
         int countminvarimages=0;
         //writing sub-images into image files
        for (int i = 0; i < n*m; i++)
        {
            File outputFile = new File("img" + i + ".jpg");
            ImageIO.write(imgs[i], "jpg", outputFile);
            System.out.println("entrophy of an image "+ i+" is "+e.getShannonEntropy_Image(imgs[i]));
            if(e.getShannonEntropy_Image(imgs[i])>=minentrophy)
             {
                 countentrophyimages++;
             }
            
        double meanofimage=e.mean(imgs[i]);  
        System.out.println("mean of image ids "+i+"is "+meanofimage);
        //minvar=Math.pow(meanofimage,2);
        minvar=(meanofimage*5);
        if(e.varianceRed(imgs[i],meanofimage)>minvar)
        {
           countminvarimages++;
        }
        System.out.println("variance of an image "+i+"is "+e.varianceRed(imgs[i],meanofimage));
        }
        System.err.println(countentrophyimages);
        System.out.println(countminvarimages);
        if(countentrophyimages>(n*m/2)&&countminvarimages>(n*m/2))
        {
        System.out.println("IMAGE CAN BE USED AS COVER IMAGE");
        }
        else
        {
            System.out.println("IMAGE CANNOT BE USED AS COVER IMAGE");
        }
    }
    }
}
class Entophy
{
    public  double getShannonEntropy_Image(BufferedImage actualImage){
        ArrayList<String> values= new ArrayList<String>();
                    int n = 0;
                    Map<Integer, Integer> occ = new HashMap<>(); // declared to map pixels
                    for(int i=0;i<actualImage.getHeight();i++){    //dimension of image
                        for(int j=0;j<actualImage.getWidth();j++)   // dimension of image
                        {
                          int pixel = actualImage.getRGB(j, i);
                          //int alpha = (pixel >> 24) & 0xff;
                          int red = (pixel >> 16) & 0xff;
                          int green = (pixel >> 8) & 0xff;
                          int blue = (pixel) & 0xff;
                          int d= (int)Math.round(0.2989 * red + 0.5870 * green + 0.1140 * blue);
                         if(!values.contains(String.valueOf(d)))    //locating the pixel
                             values.add(String.valueOf(d));
                         if (occ.containsKey(d)) {
                             occ.put(d, occ.get(d) + 1);        //maping the pixel
                         } else {
                             occ.put(d, 1);
                         }
                         ++n;
                  }
               }
               double e = 0.0;
               double entrophy=0.0;
               for (Map.Entry<Integer, Integer> entry : occ.entrySet()) // iterate throw the mapped pixels
               {
                   // int cx = entry.getKey();
                    double p = (double) entry.getValue() / n;
                    e += p * (Math.log(p)/Math.log(2));    //entropy formula
                    entrophy=e;
            
               }
             
        return Math.abs(entrophy);
       }
       public double mean(BufferedImage image)
       {
        WritableRaster raster = image.getRaster();
        double sum = 0.0;
    
        for (int y = 0; y < image.getHeight(); ++y){
          for (int x = 0; x < image.getWidth(); ++x){
            sum += raster.getSample(x, y, 0);
          }
        }
        return sum / (image.getWidth() * image.getHeight());
       }
       public  double varianceRed(BufferedImage image,double mean){
        
        double sumOfDiff = 0.0;
       
         for (int y = 0; y < image.getHeight(); ++y){
               for (int x = 0; x < image.getWidth(); ++x){
                   Color r = new Color(image.getRGB(0, 0));
                               double colour = r.getRed() - mean;
                               sumOfDiff += Math.pow(colour, 2); 
                             }                   
                           }
        return sumOfDiff / ((image.getWidth() * image.getHeight()) - 1);
        }
   
        
}

