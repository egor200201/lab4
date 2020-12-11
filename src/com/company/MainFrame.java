package com.company;
import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;

public class MainFrame extends JFrame {

    private JFileChooser fileChooser;

    private boolean fileLoaded;

    private GraphicsDisplay display = new GraphicsDisplay();

    static private final int HEIGHT = 500;
    static private final int WIDTH = 700;

    private JCheckBoxMenuItem showAxisMenuItem;
    private JCheckBoxMenuItem showGridMenuItem;
    private JCheckBoxMenuItem showMarkersMenuItem;
    private JCheckBoxMenuItem turnGraphMenuItem;

    public MainFrame(){
        super("построение графиков функций на основе изначально заявленных файлов");
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - WIDTH)/2,
                (kit.getScreenSize().height - HEIGHT)/2);
//        setExtendedState(MAXIMIZED_BOTH);
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("файл");
        menuBar.add(fileMenu);
        Action openGraphicsAction = new AbstractAction("выбрать файл"){
            public void actionPerformed(ActionEvent arg0) {
                if (fileChooser==null) {
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if (fileChooser.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION);
                openGraphics(fileChooser.getSelectedFile());
            }
        };
        fileMenu.add(openGraphicsAction);
        JMenu graphicsMenu = new JMenu("график");
        menuBar.add(graphicsMenu);

        Action showAxisAction = new AbstractAction("показывать оси") {
            public void actionPerformed(ActionEvent e) {
                display.setShowAxis(showAxisMenuItem.isSelected());
                if(showGridMenuItem.isSelected())
                    display.setShowGrid(showAxisMenuItem.isSelected());
            }
        };
        showAxisMenuItem = new JCheckBoxMenuItem(showAxisAction);
        graphicsMenu.add(showAxisMenuItem);
        showAxisMenuItem.setSelected(true);

        Action showGridAction = new AbstractAction("показывать решётку") {
            public void actionPerformed(ActionEvent e) {
                if (showAxisMenuItem.isSelected())
                    display.setShowGrid(showGridMenuItem.isSelected());
            }
        };
        showGridMenuItem = new JCheckBoxMenuItem(showGridAction);
        graphicsMenu.add(showGridMenuItem);
        showGridMenuItem.setSelected(true);

        graphicsMenu.addSeparator();

        Action showMarkersAction = new AbstractAction("показывать маркера") {

            public void actionPerformed(ActionEvent e) {
                display.setShowMarkers(showMarkersMenuItem.isSelected());
            }
        };
        showMarkersMenuItem = new JCheckBoxMenuItem(showMarkersAction);
        graphicsMenu.add(showMarkersMenuItem);
        showMarkersMenuItem.setSelected(true);
        graphicsMenu.addMenuListener(new GraphicsMenuListener());
        getContentPane().add(display, BorderLayout.CENTER);
    }

    protected void openGraphics(File selectedFile) {
        try {
            DataInputStream in = new DataInputStream(new FileInputStream(selectedFile));
            Double[][] graphicsData = new
                    Double[in.available()/(Double.SIZE/8)/2][];
            int i = 0;
            while (in.available() > 0) {
                Double x = Double.valueOf(in.readDouble());
                Double y = Double.valueOf(in.readDouble());
                graphicsData[i++] = new Double[] {x, y};
            }
            if (graphicsData!=null && graphicsData.length>0) {
                fileLoaded = true;
                display.showGraphics(graphicsData);
            }
            in.close();
        }catch (FileNotFoundException e){
            JOptionPane.showMessageDialog(MainFrame.this,
                    "указанный файл не найден", "ошибка загрузки данных",
                    JOptionPane.WARNING_MESSAGE);
            return;

        }catch (IOException e){
            JOptionPane.showMessageDialog(MainFrame.this,
                    "Ошибка чтения координат точек из файла" ,
                    "Ошибка загрузки данных", JOptionPane.WARNING_MESSAGE);
            return;
        }
    }
    private class GraphicsMenuListener implements MenuListener {
        public void menuSelected(MenuEvent e) {
            showAxisMenuItem.setEnabled(fileLoaded);
            showMarkersMenuItem.setEnabled(fileLoaded);
            showGridMenuItem.setEnabled(fileLoaded);
        }

        public void menuDeselected(MenuEvent e) {
        }
        public void menuCanceled(MenuEvent e) {

        }
    }

    public static void main(String[] args) {

        MainFrame frame = new MainFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }

}