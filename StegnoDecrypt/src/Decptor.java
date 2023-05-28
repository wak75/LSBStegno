import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Decptor{
    public static String decrypt(File SelectedFile){
        

        BufferedImage EncryptedImage;
        try {
            EncryptedImage = ImageIO.read(SelectedFile);
            Pixel[] pixels = GettingPixelArray(EncryptedImage);
            String finalMessage = (FinalDecodedMessage(pixels));
            return finalMessage;
        } catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
        
    }

    private static Pixel[] GettingPixelArray(BufferedImage theImage){
        int weidth = theImage.getWidth();
        int height  = theImage.getHeight();

        Pixel[] pixels =  new Pixel[weidth* height];

        int count =0 ;
        for(int i=0;i<weidth;i++){
            for(int j = 0;j<height;j++){
                Color color = new Color(theImage.getRGB(i, j));
                pixels[count] = new Pixel(i, j, color);
                count++;
            }
        }
    
        return pixels;
    }

    private static String FinalDecodedMessage(Pixel[] pixels){
        boolean isCompleted = false;
        int pixelArrayIndex = 0;

        StringBuilder messageBuilder  = new StringBuilder("");
        while(isCompleted==false){
            Pixel[] readingPixels = new Pixel[3];
            for(int i=0;i<3;i++){
                readingPixels[i] = pixels[pixelArrayIndex];
                pixelArrayIndex++;
            }
            messageBuilder.append(ConvertPixelstoCharacter(readingPixels));
            if(isMessageEnd(readingPixels[2])==true){
                isCompleted = true;
            }
        }
        return messageBuilder.toString();
    }



    private static char ConvertPixelstoCharacter(Pixel[] readPixels){
        ArrayList<String> binaryValues = new ArrayList<>();
        for(int i=0;i<readPixels.length;i++){
            String[] currentBinary = TurnPixelIntegerToBinary(readPixels[i]);
            binaryValues.add(currentBinary[0]);
            binaryValues.add(currentBinary[1]);
            binaryValues.add(currentBinary[2]);
        }
        return ConvertedBinaryToChar(binaryValues);
    }

    private static String[] TurnPixelIntegerToBinary(Pixel pixels){
        String[] data = new String[3];
        data[0]=Integer.toBinaryString(pixels.getColor().getRed());
        data[1]=Integer.toBinaryString(pixels.getColor().getGreen());
        data[2]=Integer.toBinaryString(pixels.getColor().getBlue());

        return data;
    }

    private static boolean isMessageEnd(Pixel pixel){
        if(TurnPixelIntegerToBinary(pixel)[2].endsWith("1")){
            return false;
        }
        return true;
    }

    private static char ConvertedBinaryToChar(ArrayList<String> binaryValue){
        StringBuilder endBinary = new StringBuilder("");
        for(int i=0;i<binaryValue.size()-1;i++){
            endBinary.append(binaryValue.get(i).charAt(binaryValue.get(i).length()-1));
        }
        String endBinaryString = endBinary.toString();
        String noZeros = RemovePaddedZeros(endBinaryString);
        int ascii  = Integer.parseInt(noZeros,2);

        return (char) ascii;
    }

    private static String RemovePaddedZeros(String endBianry){
        StringBuilder builder = new StringBuilder(endBianry);
        int paddedZero = 0;

        for(int i=0;i<builder.length();i++){
            if(builder.charAt(i)=='0'){
                paddedZero++;
            }
            else{
                break;
            }
        }

        for(int i=0;i<paddedZero;i++){
            builder.deleteCharAt(0);
        }
        return builder.toString();
    }
}