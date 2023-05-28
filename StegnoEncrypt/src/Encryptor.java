import java.awt.Color;
import java.awt.Image;
import java.awt.Panel;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

public class Encryptor {
    public static void doEncrypt(File selectedImage, String message){
        String placeToSave = new JFileChooser().getFileSystemView().getDefaultDirectory().toString();
        String saveLocationName = placeToSave + "\\EncryptedImage.png";

        File newImage = new File(saveLocationName);


        BufferedImage image;
        try{
            image = ImageIO.read(selectedImage);
            BufferedImage FinalImageToEncrypt = GetEncryptedImage(image);
            Pixel[] imagePixels = GetPixelArray(FinalImageToEncrypt);
            String[] messageConvertedToBinary = ConvertMessageToBinary(message);
            EncodeBinaryMessageIntoPixels(imagePixels, messageConvertedToBinary);
            ReplacePixelsFromImage(imagePixels, image);
            SaveTheFinalFile(image, newImage);

        }
        catch(IOException e){
           e.printStackTrace(); 
        }
    }


    private static BufferedImage GetEncryptedImage(BufferedImage image){ //getting an editable image copy
        ColorModel colorModel = image.getColorModel();
        WritableRaster raster = image.copyData(null);
        Boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();

        return new BufferedImage(colorModel, raster, isAlphaPremultiplied, null);
    }

    private static Pixel[] GetPixelArray(BufferedImage FinalImageToEncrypt){
        int weidth = FinalImageToEncrypt.getWidth();
        int height = FinalImageToEncrypt.getHeight();
        Pixel[] pixels = new Pixel[height*weidth];
        int count=0;
        for(int i=0;i<weidth;i++){
            for(int j=0;j<height;j++){
                Color currentColor = new Color(FinalImageToEncrypt.getRGB(i, j));
                pixels[count] = new Pixel(i, j, currentColor);
                count++;
            }
        }

        return pixels;
    }


    
    private static String[] ConvertMessageToBinary(String message){
        int[] messageInAscii = ConververtMessageToAscii(message);
        String[] messageInBinaryString  = ConvertAsciiMessageToBinary(messageInAscii);
        return messageInBinaryString;
    }

    private static int[] ConververtMessageToAscii(String message){
        int[] finalMessageinAscii = new int[message.length()];
        for(int i=0;i<message.length();i++){
            finalMessageinAscii[i] = (int)message.charAt(i);
        }

        return finalMessageinAscii;
    }

    private static String[] ConvertAsciiMessageToBinary(int[] messageInAscii){
        String[] finalMessageInBinary = new String[messageInAscii.length];
        for(int i=0;i<messageInAscii.length;i++){
            finalMessageInBinary[i] = LeftPadZeros(Integer.toBinaryString(messageInAscii[i]));
        }
        return finalMessageInBinary;
    }

        //LEFTPADZERO is used to make each character to get convert into perfect 8 bit binary digit.
        

        
    private static String LeftPadZeros(String data) {
        StringBuilder finalValueAfterAddingZero = new StringBuilder("00000000");
        int currData =  8-data.length();
        for(int i=0;i<data.length();i++){
            finalValueAfterAddingZero.setCharAt(i+currData, data.charAt(i));
        }
        return finalValueAfterAddingZero.toString();
    } 
    

    public static void EncodeBinaryMessageIntoPixels(Pixel[] imagePixels, String[] messageConvertedToBinary){
        int pixelIndex = 0;
        boolean isLastChar = false;
        for(int i = 0;i<messageConvertedToBinary.length;i++){
            Pixel[] currPixel = new Pixel[]{imagePixels[pixelIndex], imagePixels[pixelIndex+1],imagePixels[pixelIndex+2]};
            if(i+1 == messageConvertedToBinary.length){
                isLastChar = true;
            }
            ChangingPixelColor(messageConvertedToBinary[i],currPixel,isLastChar);
            pixelIndex+=3;
        }
    }

    private static void ChangingPixelColor(String messageConvertedToBinary,Pixel[] pixels, boolean isLastChar){
        int messageBinaryIndex = 0;
        for(int i =0;i<pixels.length-1;i++){
            char[] messageBinatyToChars = new char[]{messageConvertedToBinary.charAt(messageBinaryIndex),messageConvertedToBinary.charAt(messageBinaryIndex+1),messageConvertedToBinary.charAt(messageBinaryIndex+2)};

            String[] binaryRGBPixel = GetPixelsBinary(pixels[i],messageBinatyToChars);
            pixels[i].setColor(GetNewPixelColor(binaryRGBPixel));
            messageBinaryIndex+=3;
        }

            //this is pending
            if(isLastChar==false){
                char[] messageBinaryToChars = new char[]{messageConvertedToBinary.charAt(messageBinaryIndex),messageConvertedToBinary.charAt(messageBinaryIndex+1),'1'};
                String[] binaryRGBPixel = GetPixelsBinary(pixels[pixels.length-1], messageBinaryToChars);
                pixels[pixels.length-1].setColor(GetNewPixelColor(binaryRGBPixel));
            }
            else{
                char[] messageBinaryToChars = new char[]{messageConvertedToBinary.charAt(messageBinaryIndex),messageConvertedToBinary.charAt(messageBinaryIndex+1),'0'};
                String[] binaryRGBPixel = GetPixelsBinary(pixels[pixels.length-1], messageBinaryToChars);
                pixels[pixels.length-1].setColor(GetNewPixelColor(binaryRGBPixel));
            }
        
    }


    private static String[] GetPixelsBinary(Pixel pixels, char[] messageBinaryToChars){
        String[] RGBPixelBinary = new String[3];
        RGBPixelBinary[0] = ChangePixelToBinary(Integer.toBinaryString(pixels.getColor().getRed()),messageBinaryToChars[0]);
        RGBPixelBinary[1] = ChangePixelToBinary(Integer.toBinaryString(pixels.getColor().getRed()),messageBinaryToChars[1]);
        RGBPixelBinary[2] = ChangePixelToBinary(Integer.toBinaryString(pixels.getColor().getRed()),messageBinaryToChars[2]);
        return RGBPixelBinary;
    }

    private static String ChangePixelToBinary(String pixelsBinaryForm, char messageBinaryToChars){//Getting the binary value of the pixels and replaceing the last bit with the character converted to binary of the message.
        StringBuilder sb = new StringBuilder(pixelsBinaryForm);
        sb.setCharAt(pixelsBinaryForm.length()-1, messageBinaryToChars);
        return sb.toString();
    }

    private static Color GetNewPixelColor(String[] binaryRGBPixel){
        return new Color(Integer.parseInt(binaryRGBPixel[0],2), Integer.parseInt(binaryRGBPixel[1],2) , Integer.parseInt(binaryRGBPixel[2],2)); //returning the color in the form of RGB
    }

    private static void ReplacePixelsFromImage(Pixel[] newPixels, BufferedImage finalImage){
        for(int i = 0;i< newPixels.length;i++){
            finalImage.setRGB(newPixels[i].getX(), newPixels[i].getY(), newPixels[i].getColor().getRGB()); 
        }
    }

    private static void SaveTheFinalFile(BufferedImage finalImage, File newImage){
        try {
            ImageIO.write(finalImage, "png", newImage);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

}
