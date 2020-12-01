package client.screen;

import javax.swing.*;

public class NoSelectionModel extends DefaultListSelectionModel {
    @Override
    public void setAnchorSelectionIndex(final int anchorIndex) {
        // Do nothing.
    }

    @Override
    public void setLeadAnchorNotificationEnabled(final boolean flag) {
        // Do nothing.
    }

    @Override
    public void setLeadSelectionIndex(final int leadIndex) {
        // Do nothing.
    }

    @Override
    public void setSelectionInterval(final int index0, final int index1) {
        // Do nothing.
    }
}
