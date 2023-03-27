package com.zhuxingyi;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;

/**
 * @author zhuxingyi
 * @date 27/3/2023 下午3:49
 */
public class ImageUploader extends JFrame {
    private final S3Client s3Client;
    private final JTextField bucketNameField;
    private final JTextField keyField;
    private final JButton browseButton;
    private final JButton uploadButton;
    private final JLabel imageLabel;
    private final JButton downloadButton;

    public ImageUploader(S3Client s3Client) {
        super("Image Uploader");
        this.s3Client = s3Client;

        // 设置布局和组件
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 5, 5, 5);

        JLabel bucketLabel = new JLabel("Bucket Name:");
        c.gridx = 0;
        c.gridy = 0;
        mainPanel.add(bucketLabel, c);

        bucketNameField = new JTextField(20);
        c.gridx = 1;
        c.gridy = 0;
        mainPanel.add(bucketNameField, c);

        JLabel keyLabel = new JLabel("Key:");
        c.gridx = 0;
        c.gridy = 1;
        mainPanel.add(keyLabel, c);

        keyField = new JTextField(20);
        c.gridx = 1;
        c.gridy = 1;
        mainPanel.add(keyField, c);

        browseButton = new JButton("Browse");
        browseButton.addActionListener(e -> selectImageFile());
        c.gridx = 0;
        c.gridy = 2;
        mainPanel.add(browseButton, c);

        uploadButton = new JButton("Upload");
        uploadButton.addActionListener(e -> uploadImage());
        c.gridx = 1;
        c.gridy = 2;
        mainPanel.add(uploadButton, c);

        JLabel imageLabelTitle = new JLabel("Image Preview:");
        c.gridx = 0;
        c.gridy = 3;
        mainPanel.add(imageLabelTitle, c);

        imageLabel = new JLabel();
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 2;
        c.insets = new Insets(5, 5, 20, 5);
        mainPanel.add(imageLabel, c);

        downloadButton = new JButton("Download Image");
        downloadButton.addActionListener(e -> downloadImage());
        c.gridx = 1;
        c.gridy = 5;
        c.gridwidth = 1;
        c.insets = new Insets(5, 5, 5, 5);
        mainPanel.add(downloadButton, c);

        add(mainPanel);

        // 设置窗口属性
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 350);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    private void selectImageFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Image File");
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            keyField.setText(file.getName());
        }
    }

    private void uploadImage() {
        String bucketName = bucketNameField.getText().trim();
        String key = keyField.getText().trim();

        if (bucketName.isEmpty() || key.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Bucket Name and Key.");
            return;
        }

        File file = new File(key);
        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "File not found: " + key);
            return;
        }

        try {
            s3Client.uploadImage(bucketName, key, file);
            JOptionPane.showMessageDialog(this, "Image uploaded successfully.");

            // 更新图像预览
            InputStream inputStream = s3Client.downloadImage(bucketName, key);
            BufferedImage image = ImageIO.read(inputStream);
            imageLabel.setIcon(new ImageIcon(image));
            imageLabel.setText(null);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error uploading image: " + ex.getMessage());
        }
    }

    private void downloadImage() {
        String bucketName = bucketNameField.getText().trim();
        String key = keyField.getText().trim();

        if (bucketName.isEmpty() || key.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Bucket Name and Key.");
            return;
        }

        try {
            InputStream inputStream = s3Client.downloadImage(bucketName, key);
            BufferedImage image = ImageIO.read(inputStream);
            imageLabel.setIcon(new ImageIcon(image));
            imageLabel.setText(null);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error downloading image: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        S3Client s3Client = new S3Client();
        ImageUploader uploader = new ImageUploader(s3Client);
        uploader.setVisible(true);
    }
}