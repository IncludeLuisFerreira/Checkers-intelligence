package View;

import javax.swing.JOptionPane;
import java.awt.Desktop;
import java.net.URI;
import java.util.Random;

public class PopUp {

    Object[] option = {"Ver propaganda", "Prefiro perder"};
    String[] videos = {
            "https://youtu.be/RYtPcMezXf0?si=w01VLgVy-Lccix8A",
            "https://www.youtube.com/watch?v=8NyxfNbGC8o&pp=ygUnaXN1bGRlbWluYXMgcG87b3MgZGUgY2FsZGFzIGVuZ2VuaGFyaWEg",
            "https://youtu.be/dzf6G69OV-A?si=DaDBOMqDY-Eo6BWb"
    };


    public PopUp() {
        int choice = setPopUp();
        handleChoice(choice);
    }

    private int setPopUp() {
        return JOptionPane.showOptionDialog(
                null,
                "Receba uma peça extra assinstindo uma propaganda do IF",
                "Message",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                option,
                option[1]
        );
    }

    private void handleChoice(int choice) {
        Random aleatorio = new Random();
        switch (choice) {
            case 0 : {
                System.out.println("Escolha 0");
                playVideo(videos[aleatorio.nextInt(videos.length)]);
                break;
            }
            case 1 : {
                System.out.println("Escolha 1");
                System.exit(0);
            }
        }
    }

    private void playVideo(String url) {
        if  (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        PopUp popUp = new PopUp();

    }
}