package cn.abalone.service;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientGUIService{
    private BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();
    JTextArea showArea, inputArea;
    JFrame frame;
    JPanel mainPanel, buttonPanel;
    JButton submit;
    public void createGUI() {
        frame = new JFrame("聊天室客户端");
        frame.setLayout(new BorderLayout());
        mainPanel = new JPanel();
        mainPanel.setBackground(new Color(61,66,66));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // 左右各20像素边距
        initShowArea();
        initInputArea();

// 底部按钮（右对齐）
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(61,66,66));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(-5, 0, 20, 10)); // 左右各20像素边距
        submit = new JButton("提交");
        submit.addActionListener(e -> {
            String input = inputArea.getText();
            inputQueue.offer(input); // 将输入放入队列
            inputArea.setText(""); // 清空输入框
        });
        buttonPanel.add(submit);

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void addMessage(String msg) {
        showArea.append(msg + "\n");
    }

    private void initShowArea() {
        showArea = new JTextArea(23, 37);
        showArea.setLineWrap(true);
        showArea.setWrapStyleWord(true);
        showArea.setEditable(false);
        showArea.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
        showArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        showArea.setBackground(new Color(80,80,80));
        // 将showArea放入JScrollPane
        JScrollPane scrollPane = new JScrollPane(showArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainPanel.add(scrollPane);
        mainPanel.add(Box.createVerticalStrut(20)); // 添加间距
    }

    private void initInputArea() {
        inputArea = new JTextArea(6, 37);
        inputArea.setEditable(true);
        inputArea.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
        inputArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        inputArea.setBackground(new Color(97,97,97));
        // 将inputArea放入JScrollPane
        JScrollPane scrollPane = new JScrollPane(inputArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainPanel.add(scrollPane); // 替换原来的mainPanel.add(inputArea)

    }

    public String getNextInput() throws InterruptedException {
        return inputQueue.take(); // 阻塞直到有输入
    }
}
