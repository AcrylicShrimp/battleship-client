package client.screen;


import java.awt.*;

public interface HasContent {
    Container getContentPane();
    int preferredWidth();
    int preferredHeight();
}
