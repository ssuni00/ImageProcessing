
import javax.swing.*;
import java.awt.*;
import java.io.File;


public class FileOpen extends Component {
    File file;
    Image image2;

    FileOpen() {
        JFileChooser jfc = new JFileChooser();
        int returnVal = jfc.showOpenDialog(null);
        if (returnVal == 0) {
            file = jfc.getSelectedFile();
        }else{
            System.out.println("파일열기 취소");
        }

        String path = String.valueOf(file);
        ImageIcon icon = new ImageIcon(path);
        Image image = icon.getImage(); // original image
        image2 = image.getScaledInstance(440,570,Image.SCALE_DEFAULT); // resized image
    }


}
