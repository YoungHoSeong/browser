package com.browser;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HtmlHandler extends JFrame implements ActionListener, HyperlinkListener {

    private JEditorPane html;
    private String website = null;
    private List<URL> history = new ArrayList<>();
    private int position = 1;
    JTextField address;
    JScrollPane scroller;
    JViewport vp;
    private boolean isFail = false;

    public HtmlHandler(String website) throws IOException {
        scroller = new JScrollPane();
        vp = scroller.getViewport();
        this.html = new JEditorPane(website);
        this.html.setEditable(false);
        this.html.addHyperlinkListener(this);

        this.website = website;
        address = new JTextField(website);
        address.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                URL url = null;
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    try {
                        url = new URL(address.getText());
                        html.setPage(url);
                        vp.add(html);
                        historyManagement(url);
                    } catch (IOException ex) {
                        html.setText(ex.getMessage());
                        vp.add(html);
                        isFail = true;
                    }
                }
            }
        });
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Copyright 2020, Caroline Kim");

    }

    public void startBrowser() throws IOException {
        if(website != null) {
            // adding back & forward button & address bar
            JPanel panel = new JPanel();
            JButton backBtn = new JButton("Back");
            JButton forwardBtn = new JButton("Forward");

            backBtn.setActionCommand("Back");
            forwardBtn.setActionCommand("Forward");
            backBtn.addActionListener(this);
            forwardBtn.addActionListener(this);

            panel.add(backBtn);
            panel.add(address);
            panel.add(forwardBtn);

            vp.add(html);
            this.historyManagement(new URL(website));
            this.getContentPane().add(panel, BorderLayout.NORTH);
            this.getContentPane().add(scroller, BorderLayout.CENTER);
            this.setSize(669,669);
            this.setVisible(true);
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        try{
            System.out.println(event.getActionCommand());
            URL url = null;
            if (event.getActionCommand().equals("Back")) {

                if(!isFail){
                    // 첫페이지가 아닐 경우에만 뒤로가기 버튼 활성화
                    // 아닐경우 Exception 발생
                    if ((this.history.size() - 1) > 0) {
                        url = this.history.get(this.position - 1);
                        address.setText(url.toString());
                        this.position = this.position - 1;
                    } else {
                        throw new Exception("이전 페이지가 존재하지 않습니다.");
                    }
                } else {
                    url = this.history.get(this.position);
                    address.setText(url.toString());
                    isFail = false;
                }

            } else {
                // 다음페이지가 있을경우에만 이동
                //없을경우 Exception 발생
                if(this.position < (this.history.size()-1)){
                    url = this.history.get(this.position + 1);
                    address.setText(url.toString());
                    this.position = this.position + 1;
                }else{
                    throw new Exception("다음 페이지가 존재하지 않습니다.");
                }
            }
            this.html = new JEditorPane(url);
            this.html.setEditable(false);
            vp.add(html);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            try {
                // 현재 페이지 위치와 히스토리 목록을 비교
                // 같다면 마지막 페이지
                // 아니라면 뒤로가기로 이동한 페이지
                // 마지막 페이지라면 기존그대로 히스토리에 URL을 등록
                // 아니라면 현재페이지 이후에 있는 히스토리 목록을 삭제 후 등록
                this.historyManagement(e.getURL());

                address.setText(e.getURL().toString());
                this.html.setPage(e.getURL());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void historyManagement(URL url){
        if(this.position != (this.history.size() - 1)){
            for(int i=this.position + 1; i<this.history.size(); i++){
                this.history.remove(i);
            }
        }else{
            this.position = this.position + 1;
        }
        this.history.add(url);

    }
}
