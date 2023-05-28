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
    private JButton decryptButton;
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
        frame = new JFrame("DEcrypt Image");
        decryptButton = new JButton("Choose File To DEcrypt");
        message = new JTextField("--> DEcrypted Message <--");


        message.setBounds(120, 150, 300, 50);
        decryptButton.setBounds(170,50,200,40);
        frame.add(decryptButton);
        frame.add(message);
        frame.setSize(600, 500);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        ActionListener decryptActionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e){
                File selectedFile = fileChooser();
                if(selectedFile!=null){
                    String DecodedMessage = Decptor.decrypt(selectedFile);
                    message.setText(DecodedMessage);
                }
            }
        };
        decryptButton.addActionListener(decryptActionListener);
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
