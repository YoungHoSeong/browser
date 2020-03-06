package com.browser;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HtmlHandler extends JFrame implements ActionListener, HyperlinkListener {

    private JEditorPane html;
    private String website = null;
    private List<URL> history = new ArrayList<>();
    private int position = 0;
    JTextField address;

    public HtmlHandler(String website) {
        this.website = website;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Copyright 2020, Caroline Kim");
    }

    public void startBrowser() throws IOException {
        if(website != null) {
            URL url = new URL(website);

            html = new JEditorPane(website);
            html.setEditable(false);
            html.addHyperlinkListener(this);

            // adding back & forward button & address bar
            JPanel panel = new JPanel();
            JButton backBtn = new JButton("Back");
            JButton forwardBtn = new JButton("Forward");
            address = new JTextField(website);

            backBtn.setActionCommand("Back");
            forwardBtn.setActionCommand("Forward");
            backBtn.addActionListener(this);
            forwardBtn.addActionListener(this);

            panel.add(backBtn);
            panel.add(address);
            panel.add(forwardBtn);

            // adding scroll pane
            JScrollPane scroller = new JScrollPane();
            JViewport vp = scroller.getViewport();
            vp.add(html);

            this.getContentPane().add(panel, BorderLayout.NORTH);
            this.getContentPane().add(scroller, BorderLayout.CENTER);
            this.history.add(url); // adding default starting page.  The stack of toBack will always have the default page as starter
            this.setSize(669,669);
            this.setVisible(true);
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        try{
            URL url = null;
            if (event.getActionCommand().equals("Back")) {

                // 첫페이지가 아닐 경우에만 뒤로가기 버튼 활성화
                // 아닐경우 Exception 발생
                if ((history.size() - 1) > 0) {
                    url = this.history.get(this.position - 1);
                    address.setText(url.toString());
                    this.position = this.position - 1;
                } else {
                    throw new Exception("이전 페이지가 존재하지 않습니다.");
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

            this.html.setPage(url);
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
                if(this.position != (this.history.size() - 1)){
                    for(int i=this.position+1; i<this.history.size(); i++){
                        this.history.remove(i);
                    }
                }

                this.history.add(e.getURL());
                address.setText(e.getURL().toString());
                this.html.setPage(e.getURL());

                this.position = this.position + 1;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
