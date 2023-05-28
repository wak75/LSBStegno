import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;


public class App {

    private JFrame frame;
    private JButton encryptButton;
    private JTextField message;
    
    public static void main(String[] args) throws Exception {


        EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App window = new App();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
        
    public App(){
        startEncryption();
    }

    private void startEncryption(){
        frame = new JFrame("ENcrypt Image");
        encryptButton = new JButton("Choose File To Encrypt");
        message = new JTextField("--> Enter your message here <--");


        message.setBounds(120, 50, 300, 50);
        encryptButton.setBounds(200,150,100,40);
        frame.add(message);
        frame.add(encryptButton);
        frame.setSize(600, 500);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        ActionListener EncryptAction = new ActionListener() {
            public void actionPerformed(ActionEvent e){
                File selectedImage = fileChooser();
                if(selectedImage!=null){
                    System.out.println("Valid file selected");
                    Encryptor.doEncrypt(selectedImage, message.getText());
                }

            }
        };
        encryptButton.addActionListener(EncryptAction);
    }


     
    public File fileChooser(){
        JFileChooser fileChooser =new JFileChooser();

        int isOpen = fileChooser.showOpenDialog(null);
        if(isOpen == JFileChooser.APPROVE_OPTION){
            File selectedFile = fileChooser.getSelectedFile();
            return selectedFile;
        }
        else{
            System.out.println("Failed to open Filechooser");
            return null;
        }
        
    }

    

}
