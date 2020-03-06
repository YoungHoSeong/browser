package com.browser;

public class Browser {
    public Browser() {
        System.out.println("I certify that this program is my own work");
        System.out.println("and is not the work of other. I agree not");
        System.out.println("to share my solution with others.");
        System.out.println("Caroline Kim\n");
    }

    public void run(String website) {
        try {
            HtmlHandler handler = new HtmlHandler(website);
            handler.startBrowser();

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
