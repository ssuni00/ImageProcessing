import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;


public class mainBoard extends JFrame implements ActionListener, MouseListener{
    JPanel menuP = new JPanel();
    JButton btn_black = new JButton("B&W");
    JButton btn_clear = new JButton("ORIGINAL");
    JButton btn_mag = new JButton("MAGNIFIER");
    JButton btn_open = new JButton("OPEN");
    JLabel beforeP = new JLabel(); // 처리 전 이미지 불러오는 Panel
    JLabel afterP = new JLabel(); // 처리 후 이미지 불러오는 Panel
    Color color = new Color(245, 209, 186);
    JSlider slider = new JSlider(JSlider.HORIZONTAL, -100, 100, 0); // 밝기조절용
    JSlider slider2 = new JSlider(JSlider.HORIZONTAL, -100, 100, 0); // 명암대비용 (contrast)
    JLabel brightL = new JLabel("BRIGHTNESS: ");
    JLabel contrastL = new JLabel("CONTRAST: ");

    int bright; // 밝기조절용
    FileOpen fileOpen;
    Image image;
    BufferedImage bfi;
    BufferedImage Bimage;
    BufferedImage cropped;
    Image resized;
    private Point clicked = new Point();
    String menu = "";


    mainBoard() {
        setTitle("IMAGE PROCESSING 📸");
        setLayout(null);
        menuP.add(btn_black);
        btn_black.setBounds(10, 22, 70, 30);
        btn_black.setBackground(color);
        menuP.add(btn_open);
        btn_open.setBounds(870, 22, 70, 30);
        btn_open.setBackground(color);
        menuP.add(btn_mag);
        btn_mag.setBounds(661, 22, 100, 30);
        btn_mag.setBackground(color);
        menuP.add(btn_clear);
        btn_clear.setBounds(770, 22, 92, 30);
        btn_clear.setBackground(color);
        menuP.add(brightL);
        brightL.setBounds(90,23,100,30);
        menuP.add(contrastL);
        contrastL.setBounds(370,23,100,30);
        menuP.setLayout(null);
        menuP.add(slider);
        menuP.add(slider2);
        menuP.setBounds(13, 10, 950, 70);
        menuP.setBorder(new TitledBorder(new LineBorder(Color.GRAY, 1), "MENU"));

        btn_black.addActionListener(this);
        btn_clear.addActionListener(this);
        btn_open.addActionListener(this);
        btn_mag.addActionListener(this);
        addMouseListener(this);
        slider.setMinorTickSpacing(10);
        slider.setMajorTickSpacing(40);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setBounds(175, 17, 180, 50);
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (slider.getValueIsAdjusting()) {
                    Image im = bfi;
                    JSlider s = (JSlider) e.getSource();
                    BufferedImage t_buffer = imageToBufferedImage(im);
                    bright = (int) s.getValue();
                    for (int y = 0; y < t_buffer.getHeight(); y++) {
                        for (int x = 0; x < t_buffer.getWidth(); x++) {
                            Color color = new Color(t_buffer.getRGB(x, y));
                            int red = color.getRed();
                            int green = color.getGreen();
                            int blue = color.getBlue();
                            red += bright;
                            green += bright;
                            blue += bright;
                            if (red > 255) red = 255;
                            if (green > 255) green = 255;
                            if (blue > 255) blue = 255;
                            if (red < 0) red = 0;
                            if (green < 0) green = 0;
                            if (blue < 0) blue = 0;
                            t_buffer.setRGB(x, y, new Color(red, green, blue).getRGB());
                        }
                    }
                    afterP.setIcon(new ImageIcon(t_buffer));
                }
            }
        });
        slider2.setMinorTickSpacing(10);
        slider2.setMajorTickSpacing(40);
        slider2.setPaintTicks(true);
        slider2.setPaintLabels(true);
        slider2.setBounds(440, 17, 180, 50);
        slider2.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (slider2.getValueIsAdjusting()) {
                    Image im = bfi;
                    JSlider s = (JSlider) e.getSource();
                    BufferedImage t_buffer = imageToBufferedImage(im);
                    bright = (int) s.getValue();
                    for (int y = 0; y < t_buffer.getHeight(); y++) {
                        for (int x = 0; x < t_buffer.getWidth(); x++) {
                            Color color = new Color(t_buffer.getRGB(x, y));
                            int red = color.getRed();
                            int green = color.getGreen();
                            int blue = color.getBlue();
                            if(red < 128) red -= bright;
                            if(red > 128) red += bright;
                            if(green < 128) green -= bright;
                            if(green > 128) green += bright;
                            if(blue < 128) blue -= bright;
                            if(blue > 128) blue += bright;
                            if (red > 255) red = 255;
                            if (green > 255) green = 255;
                            if (blue > 255) blue = 255;
                            if (red < 0) red = 0;
                            if (green < 0) green = 0;
                            if (blue < 0) blue = 0;
                            t_buffer.setRGB(x, y, new Color(red, green, blue).getRGB());
                        }
                    }
                    afterP.setIcon(new ImageIcon(t_buffer));
                }
            }
        });

        beforeP.setLayout(null);
        beforeP.setBounds(25, 90, 460, 600);
        beforeP.setBorder(new TitledBorder(new LineBorder(Color.GRAY, 5), "Before"));
        afterP.setLayout(null);
        afterP.setBounds(500, 90, 460, 600);
        afterP.setBorder(new TitledBorder(new LineBorder(Color.GRAY, 5), "After"));

        getContentPane().setBackground(new Color(245, 243, 243));
        setSize(1000, 750);
        setLocationRelativeTo(null);
        setResizable(false);
        add(menuP);
        add(beforeP);
        add(afterP);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        menu = e.getActionCommand();
        if (menu.equals("OPEN")) {
            fileOpen = new FileOpen();
            beforeP.setIcon(new ImageIcon(fileOpen.image2)); // 크기에 맞춰 자른 이미지 before에 붙히기
         //   image = fileOpen.image2;
            bfi = imageToBufferedImage(fileOpen.image2);// 자른 이미지를 bufferedimage로 만들기
            Bimage = imageToBufferedImage(fileOpen.image2);// 자른 이미지를 bufferedimage로 만들기
            //   afterP.setIcon(new ImageIcon(bfi)); // afterP에 buffered image 붙히기
        } else if (menu.equals("B&W")) {
            for (int y = 0; y < Bimage.getHeight(); y++) {
                for (int x = 0; x < Bimage.getWidth(); x++) {
                    Color colour = new Color(Bimage.getRGB(x, y));
                    int Y = (int) (0.2126 * colour.getRed() + 0.7152 * colour.getGreen() + 0.0722 * colour.getBlue());
                    Bimage.setRGB(x, y, new Color(Y, Y, Y).getRGB());
                }
            }
            afterP.setIcon(new ImageIcon(Bimage));
        }else if(menu.equals("ORIGINAL")){
           // afterP.setIcon(null);
            afterP.setIcon(new ImageIcon(bfi)); // afterP에 buffered image 붙히기
        }
//        else if(b.getText().equals("MAGNIFIER")){
//            afterP.setIcon(new ImageIcon(cropped)); // afterP에 buffered image 붙히기
//        }
    }

    public BufferedImage imageToBufferedImage (Image im){
        BufferedImage bi = new BufferedImage
                (im.getWidth(null), im.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics bg = bi.getGraphics();
        bg.drawImage(im, 0, 0, null);
        bg.dispose();
        return bi;
    }

    // 돋보기 할 부분 이미지 가져오기 위해 마우스 x,y좌표 가져오기
    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println("마우스 눌림");
        if(menu.equals("MAGNIFIER")){
            clicked=e.getPoint();
            System.out.println(clicked);
            int x = ((int) clicked.x);
            int y = ((int) clicked.y);
           cropped=bfi.getSubimage((x-100),(y-210),110,150);   // 이미지 crop
           resized = cropped.getScaledInstance(440,570,Image.SCALE_SMOOTH); // 잘린부분 액자 크기에 맞게 조정
           afterP.setIcon(new ImageIcon(resized)); // 액자에 붙히기
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
